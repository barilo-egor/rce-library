package tgb.btc.library.interfaces.service.bot.deal;

import tgb.btc.library.constants.enums.bot.CryptoCurrency;

import java.math.BigDecimal;

public interface IModifyDealService {

    void updateCryptoCurrencyByPid(Long pid, CryptoCurrency cryptoCurrency);

    void updateCryptoAmountByPid(BigDecimal cryptoAmount, Long pid);

    void updateAmountByPid(BigDecimal amount, Long pid);
}
