package tgb.btc.library.interfaces.web;

import tgb.btc.library.constants.enums.bot.CryptoCurrency;
import tgb.btc.library.vo.web.PoolDeal;

import java.math.BigDecimal;
import java.util.List;

public interface ICryptoWithdrawalService {
    BigDecimal getBalance(CryptoCurrency cryptoCurrency);

    String withdrawal(CryptoCurrency cryptoCurrency, BigDecimal amount, String address);

    boolean isOn(CryptoCurrency cryptoCurrency);

    List<PoolDeal> getAllPoolDeals();

    Integer addPoolDeal(PoolDeal poolDeal);

    Boolean clearPool();

    Long deleteFromPool(Long id);

    String complete();
}
