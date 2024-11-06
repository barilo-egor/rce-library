package tgb.btc.library.interfaces.web;

import tgb.btc.library.constants.enums.bot.CryptoCurrency;

import java.math.BigDecimal;

public interface ICryptoWithdrawalService {
    BigDecimal getBalance(CryptoCurrency cryptoCurrency);

    String withdrawal(CryptoCurrency cryptoCurrency, BigDecimal amount, String address);

    boolean isOn(CryptoCurrency cryptoCurrency);
}
