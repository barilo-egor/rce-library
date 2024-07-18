package tgb.btc.library.interfaces.util;

import tgb.btc.library.constants.enums.bot.FiatCurrency;

import java.util.List;

public interface IFiatCurrencyService {
    List<FiatCurrency> getFiatCurrencies();

    boolean isFew();

    FiatCurrency getFirst();
}
