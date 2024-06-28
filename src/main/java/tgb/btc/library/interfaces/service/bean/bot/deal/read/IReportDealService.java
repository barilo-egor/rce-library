package tgb.btc.library.interfaces.service.bean.bot.deal.read;

import tgb.btc.library.bean.bot.Deal;
import tgb.btc.library.constants.enums.bot.CryptoCurrency;
import tgb.btc.library.constants.enums.bot.DealStatus;
import tgb.btc.library.constants.enums.bot.DealType;
import tgb.btc.library.constants.enums.bot.FiatCurrency;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface IReportDealService {

    BigDecimal getCryptoAmountSum(DealType dealType, LocalDate date, CryptoCurrency cryptoCurrency);

    BigDecimal getCryptoAmountSumByDate(DealType dealType, LocalDateTime startDateTime, LocalDateTime endDateTime, CryptoCurrency cryptoCurrency);

    BigDecimal getCryptoAmountSum(DealType dealType, LocalDateTime dateFrom, LocalDateTime dateTo, CryptoCurrency cryptoCurrency);

    BigDecimal getTotalAmountSum(DealType dealType, LocalDateTime dateTime, CryptoCurrency cryptoCurrency);

    BigDecimal getTotalAmountSum(DealType dealType, LocalDateTime dateTime, CryptoCurrency cryptoCurrency, FiatCurrency fiatCurrency);

    BigDecimal getTotalAmountSum(DealType dealType, LocalDateTime dateFrom, LocalDateTime dateTo, CryptoCurrency cryptoCurrency);

    BigDecimal getTotalAmountSum(DealType dealType, LocalDateTime dateFrom, LocalDateTime dateTo, CryptoCurrency cryptoCurrency, FiatCurrency fiatCurrency);

    Integer getCountPassedByChatId(Long chatId);

    Long getCountByChatIdAndStatus(Long chatId, DealStatus dealStatus);

    Integer getCountByPeriod(LocalDateTime startDateTime, LocalDateTime endDateTime);

    BigDecimal getUserCryptoAmountSum(Long chatId, CryptoCurrency cryptoCurrency, DealType dealType);

    BigDecimal getUserAmountSum(Long chatId, DealType dealType);

    BigDecimal getUserAmountSumByDealTypeAndFiatCurrency(Long chatId, DealType dealType, FiatCurrency fiatCurrency);

    List<Deal> getByUser_ChatId(Long chatId);

    List<Object[]> findAllForUsersReport();
}
