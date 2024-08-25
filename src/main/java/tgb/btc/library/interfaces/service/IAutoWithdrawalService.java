package tgb.btc.library.interfaces.service;

import tgb.btc.library.constants.enums.bot.CryptoCurrency;

import java.math.BigDecimal;

public interface IAutoWithdrawalService {

    BigDecimal getBalance(CryptoCurrency cryptoCurrency);

    void withdrawal(Long dealPid);
}
