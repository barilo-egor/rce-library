package tgb.btc.library.repository.bot.deal.read;

import org.springframework.data.jpa.repository.Query;
import tgb.btc.library.bean.bot.Deal;
import tgb.btc.library.constants.enums.bot.CryptoCurrency;
import tgb.btc.library.constants.enums.bot.DealStatus;
import tgb.btc.library.constants.enums.bot.DealType;
import tgb.btc.library.constants.enums.bot.FiatCurrency;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ReportDealRepository {

    default BigDecimal getCryptoAmountSum(DealType dealType, LocalDate date, CryptoCurrency cryptoCurrency) {
        return getCryptoAmountSumByDate(dealType, date.atStartOfDay(), date.plusDays(1).atStartOfDay(), cryptoCurrency);
    }

    @Query(value = "select sum(cryptoAmount) from Deal where dealStatus='CONFIRMED' and dealType=:dealType and dateTime between :startDateTime and :endDateTime and cryptoCurrency=:cryptoCurrency")
    BigDecimal getCryptoAmountSumByDate(DealType dealType, LocalDateTime startDateTime, LocalDateTime endDateTime, CryptoCurrency cryptoCurrency);

    @Query(value = "select sum(cryptoAmount) from Deal where dealStatus='CONFIRMED' and dealType=:dealType and (dateTime between :dateFrom and :dateTo) and cryptoCurrency=:cryptoCurrency")
    BigDecimal getCryptoAmountSum(DealType dealType, LocalDateTime dateFrom, LocalDateTime dateTo, CryptoCurrency cryptoCurrency);

    @Query(value = "select sum(amount) from Deal where dealStatus='CONFIRMED' and dealType=:dealType and dateTime=:dateTime and cryptoCurrency=:cryptoCurrency")
    BigDecimal getTotalAmountSum(DealType dealType, LocalDateTime dateTime, CryptoCurrency cryptoCurrency);

    @Query(value = "select sum(amount) from Deal where dealStatus='CONFIRMED' and dealType=:dealType and dateTime=:dateTime and cryptoCurrency=:cryptoCurrency and fiatCurrency=:fiatCurrency")
    BigDecimal getTotalAmountSum(DealType dealType, LocalDateTime dateTime, CryptoCurrency cryptoCurrency, FiatCurrency fiatCurrency);


    @Query(value = "select sum(amount) from Deal where dealStatus='CONFIRMED' and dealType=:dealType and (dateTime between :dateFrom and :dateTo) and cryptoCurrency=:cryptoCurrency")
    BigDecimal getTotalAmountSum(DealType dealType, LocalDateTime dateFrom, LocalDateTime dateTo, CryptoCurrency cryptoCurrency);

    @Query(value = "select sum(amount) from Deal where dealStatus='CONFIRMED' and dealType=:dealType and (dateTime between :dateFrom and :dateTo) and cryptoCurrency=:cryptoCurrency and fiatCurrency=:fiatCurrency")
    BigDecimal getTotalAmountSum(DealType dealType, LocalDateTime dateFrom, LocalDateTime dateTo, CryptoCurrency cryptoCurrency, FiatCurrency fiatCurrency);

    @Query(value = "select count(pid) from Deal where user.chatId=:chatId and dealStatus='CONFIRMED'")
    Integer getCountPassedByChatId(Long chatId);

    @Query(value = "select count (pid) from Deal where user.chatId=:chatId and dealStatus=:dealStatus")
    Long getCountByChatIdAndStatus(Long chatId, DealStatus dealStatus);

    @Query(value = "select count(pid) from Deal where dateTime between :startDateTime and :endDateTime and dealStatus='CONFIRMED'")
    Integer getCountByPeriod(LocalDateTime startDateTime, LocalDateTime endDateTime);

    @Query(value = "select sum(cryptoAmount) from Deal where user.chatId=:chatId and dealStatus='CONFIRMED' and cryptoCurrency=:cryptoCurrency and dealType=:dealType")
    BigDecimal getUserCryptoAmountSum(Long chatId, CryptoCurrency cryptoCurrency, DealType dealType);

    @Query(value = "select sum(amount) from Deal where user.chatId=:chatId and dealStatus='CONFIRMED' and dealType=:dealType")
    BigDecimal getUserAmountSum(Long chatId, DealType dealType);

    @Query(value = "select sum(amount) from Deal where user.chatId=:chatId and dealStatus='CONFIRMED' and dealType=:dealType and fiatCurrency=:fiatCurrency")
    BigDecimal getUserAmountSumByDealTypeAndFiatCurrency(Long chatId, DealType dealType, FiatCurrency fiatCurrency);

    List<Deal> getByUser_ChatId(Long chatId);

    @Query(value = "select pid,user.pid,dealType,cryptoCurrency,cryptoAmount,fiatCurrency,amount from Deal where dealStatus='CONFIRMED'")
    List<Object[]> findAllForUsersReport();
}
