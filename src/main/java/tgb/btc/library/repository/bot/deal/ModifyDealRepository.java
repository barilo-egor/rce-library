package tgb.btc.library.repository.bot.deal;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import tgb.btc.library.bean.bot.Deal;
import tgb.btc.library.bean.bot.PaymentType;
import tgb.btc.library.constants.enums.bot.CryptoCurrency;
import tgb.btc.library.constants.enums.bot.DealStatus;
import tgb.btc.library.constants.enums.bot.DeliveryType;
import tgb.btc.library.constants.enums.bot.FiatCurrency;
import tgb.btc.library.repository.BaseRepository;

import java.math.BigDecimal;
import java.util.List;

public interface ModifyDealRepository extends BaseRepository<Deal> {

    @Modifying
    @Query("update Deal set cryptoCurrency=:cryptoCurrency where pid=:pid")
    void updateCryptoCurrencyByPid(Long pid, CryptoCurrency cryptoCurrency);

    @Modifying
    @Query("update Deal set cryptoAmount=:cryptoAmount where pid=:pid")
    void updateCryptoAmountByPid(BigDecimal cryptoAmount, Long pid);

    @Modifying
    @Query("update Deal set amount=:amount where pid=:pid")
    void updateAmountByPid(BigDecimal amount, Long pid);

    @Modifying
    @Query("update Deal set discount=:discount where pid=:pid")
    void updateDiscountByPid(BigDecimal discount, Long pid);

    @Modifying
    @Query("update Deal set commission=:commission where pid=:pid")
    void updateCommissionByPid(BigDecimal commission, Long pid);

    @Modifying
    @Query("update Deal set isUsedReferralDiscount=:isUsedReferralDiscount where pid=:pid")
    void updateUsedReferralDiscountByPid(Boolean isUsedReferralDiscount, Long pid);

    @Modifying
    @Query("update Deal set wallet=:wallet where pid=:pid")
    void updateWalletByPid(String wallet, Long pid);

    @Modifying
    @Query("update Deal set paymentType=:paymentType where pid=:pid")
    void updatePaymentTypeByPid(PaymentType paymentType, Long pid);

    @Modifying
    @Query("update Deal set isUsedPromo=:isUsedPromo where pid=:pid")
    void updateIsUsedPromoByPid(Boolean isUsedPromo, Long pid);

    @Modifying
    @Query("update Deal set dealStatus=:dealStatus where pid=:pid")
    void updateDealStatusByPid(DealStatus dealStatus, Long pid);

    @Modifying
    @Query(value = "update Deal set fiatCurrency=:fiatCurrency where pid=:pid")
    void updateFiatCurrencyByPid(Long pid, FiatCurrency fiatCurrency);

    @Modifying
    @Query(value = "update Deal set isPersonalApplied=:isPersonalApplied where pid=:pid")
    void updateIsPersonalAppliedByPid(Long pid, Boolean isPersonalApplied);

    @Modifying
    @Query(value = "update Deal set paymentType=null where paymentType.pid=:paymentTypePid")
    void updatePaymentTypeToNullByPaymentTypePid(Long paymentTypePid);

    @Modifying
    @Query(value = "update Deal set additionalVerificationImageId=:additionalVerificationImageId where pid=:pid")
    void updateAdditionalVerificationImageIdByPid(Long pid, String additionalVerificationImageId);

    @Modifying
    @Query("update Deal set deliveryType=:deliveryType where pid=:pid")
    void updateDeliveryTypeByPid(Long pid, DeliveryType deliveryType);

    /**
     * DELETE
     */

    @Modifying
    void deleteByUser_ChatId(Long chatId);

    @Modifying
    void deleteByPidIn(List<Long> pidList);
}
