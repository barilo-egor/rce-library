package tgb.btc.library.interfaces.scheduler;

import tgb.btc.library.constants.enums.bot.CryptoCurrency;
import tgb.btc.library.constants.enums.bot.FiatCurrency;

import java.math.BigDecimal;

public interface ICurrencyGetter {
    BigDecimal getCourseCurrency(CryptoCurrency cryptoCurrency);

    BigDecimal getCourseCurrency(FiatCurrency fiatCurrency, CryptoCurrency cryptoCurrency);
}
