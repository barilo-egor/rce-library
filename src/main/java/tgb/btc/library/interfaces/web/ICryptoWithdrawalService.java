package tgb.btc.library.interfaces.web;

import tgb.btc.library.constants.enums.bot.CryptoCurrency;
import tgb.btc.library.vo.web.PoolDeal;

import java.math.BigDecimal;
import java.util.List;

public interface ICryptoWithdrawalService {
    String getAutoName();

    boolean isAutoFeeRate(CryptoCurrency cryptoCurrency);

    String getFeeRate(CryptoCurrency cryptoCurrency);

    void putFeeRate(CryptoCurrency cryptoCurrency, String feeRate);

    void putAutoFeeRate(CryptoCurrency cryptoCurrency);

    BigDecimal getBalance(CryptoCurrency cryptoCurrency);

    String withdrawal(CryptoCurrency cryptoCurrency, BigDecimal amount, String address);

    boolean isOn(CryptoCurrency cryptoCurrency);

    List<PoolDeal> getAllPoolDeals();

    Integer addPoolDeal(PoolDeal poolDeal);

    Boolean clearPool();

    Long deleteFromPool(String bot, Long pid);

    Long deleteFromPool(Long id);

    Long deleteFromPool(PoolDeal poolDeal);

    String complete();

    void changeWallet(CryptoCurrency cryptoCurrency, String seedPhrase);
}
