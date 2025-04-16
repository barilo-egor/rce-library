package tgb.btc.library.service.web.merchant;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tgb.btc.library.bean.bot.Deal;
import tgb.btc.library.constants.enums.Merchant;
import tgb.btc.library.constants.enums.properties.VariableType;
import tgb.btc.library.service.properties.VariablePropertiesReader;
import tgb.btc.library.service.web.merchant.error.MerchantErrorLoggingService;
import tgb.btc.library.vo.RequisiteVO;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MerchantRequisiteService {

    private final Map<Merchant, IMerchantRequisiteService> merchantIMerchantRequisiteServiceMap;

    private final VariablePropertiesReader variablePropertiesReader;

    private final MerchantErrorLoggingService merchantErrorLoggingService;

    public MerchantRequisiteService(VariablePropertiesReader variablePropertiesReader,
                                    List<IMerchantRequisiteService> merchantRequisiteServices,
                                    MerchantErrorLoggingService merchantErrorLoggingService) {
        this.variablePropertiesReader = variablePropertiesReader;
        this.merchantErrorLoggingService = merchantErrorLoggingService;
        this.merchantIMerchantRequisiteServiceMap = new HashMap<>();
        for (IMerchantRequisiteService merchantService : merchantRequisiteServices) {
            this.merchantIMerchantRequisiteServiceMap.put(merchantService.getMerchant(), merchantService);
        }
    }

    public RequisiteVO getRequisites(Deal deal) {
        log.debug("Получение реквизитов для сделки №{}. Сумма={}, тип оплаты={}.", deal.getPid(), deal.getAmount(), deal.getPaymentType().getName());
        RequisiteVO requisiteVO = null;
        List<String> merchants = variablePropertiesReader.getStringList("merchant.list");
        List<Merchant> merchantList = new ArrayList<>();
        for (String merchantName : merchants) {
            try {
                Merchant merchant = Merchant.valueOf(merchantName);
                Long maxAmount = variablePropertiesReader.getLong(merchant.getMaxAmount().getKey(), 5000L);
                if (deal.getAmount().longValue() <= maxAmount) {
                    merchantList.add(Merchant.valueOf(merchantName));
                }
            } catch (IllegalArgumentException ignored) {}
        }
        log.debug("Список мерчантов для сделки №{}: ", merchantList.stream().map(Merchant::getDisplayName).collect(Collectors.joining(", ")));
        int attemptsCount = variablePropertiesReader.getInt(VariableType.NUMBER_OF_MERCHANT_ATTEMPTS, 1);
        int attemptsDelay = variablePropertiesReader.getInt(VariableType.DELAY_MERCHANT_ATTEMPTS, 3);
        log.debug("Количество попыток {}, секунд задержки {} для сделки №{}.", attemptsCount, attemptsDelay, deal.getPid());
        for (int i = 0; i < attemptsCount; i++) {
            for (Merchant merchant : merchantList) {
                try {
                    int merchantAttemptsCount = variablePropertiesReader.getInteger(VariableType.NUMBER_OF_MERCHANT_ATTEMPTS.getKey() + "." + merchant.name(), attemptsCount);
                    if (merchantAttemptsCount < i + 1) {
                        continue;
                    }
                    log.debug("Попытка №{} мерчанта {} для сделки №{}.", i + 1, merchant.getDisplayName(), deal.getPid());
                    requisiteVO = merchantIMerchantRequisiteServiceMap.get(merchant).getRequisite(deal);
                    if (Objects.nonNull(requisiteVO)) {
                        log.debug("Реквизиты для сделки №{} получены. Мерчант={}, реквизиты={}.",
                                deal.getPid(), merchant.getDisplayName(), requisiteVO.getRequisite());
                        break;
                    } else {
                        log.debug("Реквизиты для сделки №{} мерчанта {} с попытки №{} не получены.", deal.getPid(), merchant.getDisplayName(), i + 1);
                    }
                } catch (Exception e) {
                    log.debug("Ошибка получения реквизитов мерчанта {} для сделки №{} c попытки №{}.", merchant.getDisplayName(), deal.getPid(), i + 1);
                    merchantErrorLoggingService.log(deal.getPid(), e);
                }
            }
            if (Objects.nonNull(requisiteVO)) break;
            try {
                if (i < attemptsCount - 1) {
                    Thread.sleep(attemptsDelay * 1000L);
                }
            } catch (InterruptedException ignored) {
            }
        }
        if (Objects.isNull(requisiteVO)) {
            log.debug("Реквизиты для сделки {} у мерчантов получены не были.", deal.getPid());
        }
        return requisiteVO;
    }
}
