package tgb.btc.library.service.schedule;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import tgb.btc.api.web.INotificationsAPI;
import tgb.btc.api.web.INotifier;
import tgb.btc.library.constants.enums.CryptoApi;
import tgb.btc.library.constants.enums.bot.CryptoCurrency;
import tgb.btc.library.constants.enums.bot.FiatCurrency;
import tgb.btc.library.constants.enums.properties.VariableType;
import tgb.btc.library.exception.*;
import tgb.btc.library.interfaces.enums.ICryptoApiService;
import tgb.btc.library.interfaces.scheduler.ICurrencyGetter;
import tgb.btc.library.util.properties.VariablePropertiesUtil;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class CurrencyGetter implements ICurrencyGetter {

    public static final String USD = "USD";

    public static final String RUB = "RUB";

    private final Map<CryptoCurrency, CryptoApi> currentCryptoUSDApis = new EnumMap<>(CryptoCurrency.class);

    private final Map<CryptoCurrency, CryptoApi> currentCryptoRUBApis =  new EnumMap<>(CryptoCurrency.class);

    private LocalDateTime lastErrorSendTime;

    private INotifier notifier;

    private INotificationsAPI notificationsAPI;

    private ICryptoApiService cryptoApiService;

    private boolean isStartMessageSent = false;

    @Autowired
    public void setCryptoApiService(ICryptoApiService cryptoApiService) {
        this.cryptoApiService = cryptoApiService;
    }

    @Autowired(required = false)
    public void setNotifier(INotifier notifier) {
        this.notifier = notifier;
    }

    @Autowired
    public void setNotificationsAPI(INotificationsAPI notificationsAPI) {
        this.notificationsAPI = notificationsAPI;
    }

    @PostConstruct
    public void init() {
        try {
            setAvailableApis();
        } catch (ReadCourseException e) {
            log.error("Ошибка при первичной установке АПИ курсов.", e);
            throw new BaseException(e.getMessage());
        }
        cryptoCourses.put(CryptoCurrency.USDT, BigDecimal.valueOf(Double.parseDouble(
                VariablePropertiesUtil.getVariable(VariableType.USDT_COURSE)))
        );
        updateCourses();
        log.debug("Автоматическое получения курса загружено в контекст.");
    }

    @Scheduled(fixedRate = 30000)
    @Async
    public void updateCourses() {
        for (CryptoCurrency cryptoCurrency : CryptoCurrency.values()) {
            try {
                switch (cryptoCurrency) {
                case BITCOIN:
                case LITECOIN:
                    cryptoCourses.put(cryptoCurrency, getCryptoCourse(cryptoCurrency));
                    fiatCryptoCourses.put(cryptoCurrency, getFiatCryptoCourse(cryptoCurrency));
                    break;
                case MONERO:
                    cryptoCourses.put(cryptoCurrency, getCryptoCourse(cryptoCurrency));
                    break;
                }
            } catch (ReadCourseException readCourseException) {
                log.error("Ошибка при получении курса для {}:", cryptoCurrency.name());
                log.error("Ошибка при получении курса.", readCourseException);
                try {
                    setAvailableApis();
                } catch (ReadUsdCourseException readUsdEx) {
                    notifyError(readUsdEx.getCryptoCurrency(), cryptoCourses.get(readUsdEx.getCryptoCurrency()), USD);
                } catch (ReadRubCourseException readRubEx) {
                    notifyError(readRubEx.getCryptoCurrency(), fiatCryptoCourses.get(readRubEx.getCryptoCurrency()), RUB);
                }
            }
        }
        if (!isStartMessageSent) {
            StringBuilder message = new StringBuilder();
            message.append("Курсы на старте приложения:\n");
            for (CryptoCurrency cryptoCurrency : CryptoCurrency.values()) {
                message.append(cryptoCurrency.name()).append(" = ").append(cryptoCourses.get(cryptoCurrency)).append("\n");
            }
            notifier.notifyAdmins(message.toString());
            isStartMessageSent = true;
        }
    }

    public BigDecimal getCryptoCourse(CryptoCurrency cryptoCurrency) {
        try {
            return cryptoApiService.getCourse(currentCryptoUSDApis.get(cryptoCurrency));
        } catch (ReadFromUrlException e) {
            throw new ReadUsdCourseException(cryptoCurrency);
        }
    }

    public BigDecimal getFiatCryptoCourse(CryptoCurrency cryptoCurrency) {
        try {
            return cryptoApiService.getCourse(currentCryptoUSDApis.get(cryptoCurrency));
        } catch (ReadFromUrlException e) {
            throw new ReadUsdCourseException(cryptoCurrency);
        }
    }

    public void notifyError(CryptoCurrency cryptoCurrency, BigDecimal course, String fiat) {
        log.error("Для {}-{} ошибка при получении курса. Текущее значение {} .", cryptoCurrency.name(), fiat,
                course);
        if (Objects.isNull(lastErrorSendTime) || LocalDateTime.now().minusMinutes(5)
                .isAfter(lastErrorSendTime)) {
            String message =
                    "⚠️ Внимание! Возникли проблемы с получением " + cryptoCurrency + "-" + fiat + " курса. "
                            + "Предыдущее значение курса, по которому сейчас будет происходить обмен равно "
                            + course
                            + "\nВыключите обмен для " + cryptoCurrency.name() + ", если курс сильно устарел.";
            notificationsAPI.sendNotify(message);
            if (Objects.nonNull(notifier))
                notifier.notifyAdmins(message);
            lastErrorSendTime = LocalDateTime.now();
        }
    }

    private void setAvailableApis() {
        log.debug("Проверка связи для получения курсов по API.");
        List<CryptoCurrency> cryptoCurrencies = List.of(CryptoCurrency.BITCOIN, CryptoCurrency.LITECOIN,
                CryptoCurrency.MONERO);
        for (CryptoCurrency cryptoCurrency : cryptoCurrencies) {
            for (CryptoApi cryptoApi : CryptoApi.getCryptoApis(cryptoCurrency)) {
                if (tryConnect(cryptoApi, cryptoCurrency, currentCryptoUSDApis))
                    break;
            }
            if (!currentCryptoUSDApis.containsKey(cryptoCurrency))
                throw new ReadUsdCourseException(cryptoCurrency);
        }
        List<CryptoCurrency> fiatCryptoCurrencies = List.of(CryptoCurrency.BITCOIN, CryptoCurrency.LITECOIN);
        for (CryptoCurrency cryptoCurrency : fiatCryptoCurrencies) {
            for (CryptoApi cryptoApi : CryptoApi.getFiatCryptoApis(cryptoCurrency)) {
                if (tryConnect(cryptoApi, cryptoCurrency, currentCryptoRUBApis))
                    break;
            }
            if (!currentCryptoRUBApis.containsKey(cryptoCurrency))
                throw new ReadRubCourseException(cryptoCurrency);
        }
    }

    private boolean tryConnect(CryptoApi cryptoApi, CryptoCurrency cryptoCurrency, Map<CryptoCurrency, CryptoApi> apis) {
        log.debug("Попытка получения курса для {}.", cryptoApi.name());
        try {
            cryptoApiService.getCourse(cryptoApi);
            apis.put(cryptoCurrency, cryptoApi);
            log.debug("Курс {} получен, выбран для дальнейшего использования.", cryptoApi);
            return true;
        } catch (ReadFromUrlException ignored) {
            log.debug("Не получилось взять курс для {}.", cryptoApi.name());
            return false;
        }
    }

    private final Map<CryptoCurrency, BigDecimal> cryptoCourses = new ConcurrentHashMap<>();

    @Override
    public BigDecimal getCourseCurrency(CryptoCurrency cryptoCurrency) {
        return cryptoCourses.get(cryptoCurrency);
    }

    private final Map<CryptoCurrency, BigDecimal> fiatCryptoCourses = new ConcurrentHashMap<>();

    @Override
    public BigDecimal getCourseCurrency(FiatCurrency fiatCurrency, CryptoCurrency cryptoCurrency) {
        if (!FiatCurrency.RUB.equals(fiatCurrency))
            throw new BaseException("Реализация предусмотрена только для " + FiatCurrency.RUB.name());
        switch (cryptoCurrency) {
        case BITCOIN:
        case LITECOIN:
            return fiatCryptoCourses.get(cryptoCurrency);
        default:
            throw new BaseException("Для данной криптовалюты не предусмотрена реализация.");
        }
    }

}
