package tgb.btc.library.interfaces.service;

import tgb.btc.library.constants.enums.bot.CryptoCurrency;

import java.math.BigDecimal;
import java.util.List;

public interface IAutoWithdrawalService {

    BigDecimal getBalance(CryptoCurrency cryptoCurrency);

    void withdrawal(Long dealPid);

    void withdrawal(List<Long> dealPids);

    boolean isAutoWithdrawalOn(CryptoCurrency cryptoCurrency);
}
