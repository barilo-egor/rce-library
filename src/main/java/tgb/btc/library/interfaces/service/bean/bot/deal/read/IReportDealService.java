package tgb.btc.library.interfaces.service.bean.bot.deal.read;

import tgb.btc.library.constants.enums.bot.CryptoCurrency;
import tgb.btc.library.constants.enums.bot.DealType;
import tgb.btc.library.constants.enums.bot.FiatCurrency;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface IReportDealService {

    BigDecimal getConfirmedCryptoAmountSum(DealType dealType, LocalDateTime dateFrom, LocalDateTime dateTo, CryptoCurrency cryptoCurrency);

    BigDecimal getConfirmedTotalAmountSum(DealType dealType, LocalDateTime dateFrom, CryptoCurrency cryptoCurrency,
                                          FiatCurrency fiatCurrency);

    BigDecimal getConfirmedTotalAmountSum(DealType dealType, LocalDateTime dateFrom, LocalDateTime dateTo, CryptoCurrency cryptoCurrency, FiatCurrency fiatCurrency);

    List<Object[]> findAllForUsersReport();
}
