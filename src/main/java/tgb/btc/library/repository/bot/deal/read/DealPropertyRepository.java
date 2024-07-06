package tgb.btc.library.repository.bot.deal.read;

import org.springframework.data.jpa.repository.Query;
import tgb.btc.library.bean.bot.Deal;
import tgb.btc.library.constants.enums.bot.*;
import tgb.btc.library.repository.BaseRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface DealPropertyRepository extends BaseRepository<Deal> {

    @Query("select cryptoCurrency from Deal where pid=:pid")
    CryptoCurrency getCryptoCurrencyByPid(Long pid);

    @Query("select commission from Deal where pid=:pid")
    BigDecimal getCommissionByPid(Long pid);

    @Query("select amount from Deal where pid=:pid")
    BigDecimal getAmountByPid(Long pid);

    @Query("select cryptoAmount from Deal where pid=:pid")
    BigDecimal getCryptoAmountByPid(Long pid);

    @Query("select discount from Deal where pid=:pid")
    BigDecimal getDiscountByPid(Long pid);

    @Query("select dealType from Deal where pid=:pid")
    DealType getDealTypeByPid(Long pid);

    @Query(value = "select dateTime from Deal where pid=:pid")
    LocalDateTime getDateTimeByPid(Long pid);

    @Query(value = "select fiatCurrency from Deal where pid=:pid")
    FiatCurrency getFiatCurrencyByPid(Long pid);

    @Query(value = "select additionalVerificationImageId from Deal where pid=:pid")
    String getAdditionalVerificationImageIdByPid(Long pid);

    @Query(value = "select deliveryType from Deal where pid=:pid")
    DeliveryType getDeliveryTypeByPid(Long pid);

    @Query("select creditedAmount from Deal where pid=:pid")
    BigDecimal getCreditedAmountByPid(Long pid);

    @Query(value = "select dealStatus from Deal where pid=:pid")
    DealStatus getDealStatusByPid(Long pid);

    @Query(value = "select isUsedPromo from Deal where pid=:pid")
    Boolean getIsUsedPromoByPid(Long pid);
}
