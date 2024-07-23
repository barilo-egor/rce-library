package tgb.btc.library.repository.bot.deal.read;

import org.springframework.data.jpa.repository.Query;
import tgb.btc.library.bean.bot.Deal;
import tgb.btc.library.constants.enums.bot.CryptoCurrency;
import tgb.btc.library.constants.enums.bot.DealType;
import tgb.btc.library.constants.enums.bot.FiatCurrency;
import tgb.btc.library.repository.BaseRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface ReportDealRepository extends BaseRepository<Deal> {

    @Query(value = "select sum(cryptoAmount) from Deal where dealStatus='CONFIRMED' and dealType=:dealType and (dateTime between :dateFrom and :dateTo) and cryptoCurrency=:cryptoCurrency")
    BigDecimal getCryptoAmountSum(DealType dealType, LocalDateTime dateFrom, LocalDateTime dateTo, CryptoCurrency cryptoCurrency);

    @Query(value = "select sum(amount) from Deal where dealStatus='CONFIRMED' and dealType=:dealType and (dateTime between :dateFrom and :dateTo) and cryptoCurrency=:cryptoCurrency and fiatCurrency=:fiatCurrency")
    BigDecimal getTotalAmountSum(DealType dealType, LocalDateTime dateFrom, LocalDateTime dateTo, CryptoCurrency cryptoCurrency, FiatCurrency fiatCurrency);

    @Query(value = "select pid,user.pid,dealType,cryptoCurrency,cryptoAmount,fiatCurrency,amount from Deal where dealStatus='CONFIRMED'")
    List<Object[]> findAllForUsersReport();
}
