package tgb.btc.library.service.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tgb.btc.library.constants.enums.bot.FiatCurrency;
import tgb.btc.library.exception.BaseException;
import tgb.btc.library.interfaces.util.IFiatCurrencyService;
import tgb.btc.library.service.properties.ConfigPropertiesReader;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FiatCurrencyService implements IFiatCurrencyService {
    private List<FiatCurrency> fiatCurrencies;

    private ConfigPropertiesReader configPropertiesReader;

    @Autowired
    public void setConfigPropertiesReader(ConfigPropertiesReader configPropertiesReader) {
        this.configPropertiesReader = configPropertiesReader;
    }

    private boolean isFew;

    @PostConstruct
    private void init() {
        fiatCurrencies = Arrays.stream(configPropertiesReader.getStringArray("bot.fiat.currencies"))
                .map(FiatCurrency::valueOf)
                .collect(Collectors.toList());
        log.info("Загружено " + fiatCurrencies.size() + " фиатных валют: "
                + fiatCurrencies.stream()
                .map(FiatCurrency::getGenitive)
                .collect(Collectors.joining(", ")));
        if (CollectionUtils.isEmpty(fiatCurrencies)) throw new BaseException("Не найдена ни одна фиатная валюта");
        isFew = fiatCurrencies.size() > 1;
    }

    @Override
    public List<FiatCurrency> getFiatCurrencies() {
        return new ArrayList<>(fiatCurrencies);
    }

    @Override
    public boolean isFew() {
        return isFew;
    }

    @Override
    public FiatCurrency getFirst() {
        return fiatCurrencies.get(0);
    }
}
