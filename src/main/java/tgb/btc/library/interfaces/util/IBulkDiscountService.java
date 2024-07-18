package tgb.btc.library.interfaces.util;

import tgb.btc.library.constants.enums.bot.CryptoCurrency;
import tgb.btc.library.constants.enums.bot.DealType;
import tgb.btc.library.constants.enums.bot.FiatCurrency;
import tgb.btc.library.vo.BulkDiscount;

import java.math.BigDecimal;

public interface IBulkDiscountService {

    BigDecimal getPercentBySum(BigDecimal sum, FiatCurrency fiatCurrency, DealType dealType, CryptoCurrency cryptoCurrency);

    void clear();

    void add(BulkDiscount bulkDiscount);

    void reverse();
}
