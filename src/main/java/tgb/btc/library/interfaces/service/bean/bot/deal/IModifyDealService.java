package tgb.btc.library.interfaces.service.bean.bot.deal;

import org.springframework.transaction.annotation.Transactional;
import tgb.btc.library.bean.bot.Deal;
import tgb.btc.library.bean.bot.PaymentType;
import tgb.btc.library.constants.enums.bot.*;

import java.math.BigDecimal;
import java.util.List;

public interface IModifyDealService {

    Deal save(Deal deal);

    void deleteById(Long pid);

    Deal createNewDeal(DealType dealType, Long chatId);

    void deleteDeal(Long dealPid, Boolean isBanUser);

    void confirm(Long dealPid);

    @Transactional
    void confirm(Long dealPid, String hash);

    /**
     * UPDATE
     */

    void updateCryptoCurrencyByPid(Long pid, CryptoCurrency cryptoCurrency);

    void updateCryptoAmountByPid(BigDecimal cryptoAmount, Long pid);

    void updateAmountByPid(BigDecimal amount, Long pid);

    void updateDiscountByPid(BigDecimal discount, Long pid);

    void updateCommissionByPid(BigDecimal commission, Long pid);

    void updateUsedReferralDiscountByPid(Boolean isUsedReferralDiscount, Long pid);

    void updateWalletByPid(String wallet, Long pid);

    void updatePaymentTypeByPid(PaymentType paymentType, Long pid);

    void updateIsUsedPromoByPid(Boolean isUsedPromo, Long pid);

    void updateDealStatusByPid(DealStatus dealStatus, Long pid);

    void updateFiatCurrencyByPid(Long pid, FiatCurrency fiatCurrency);

    void updateIsPersonalAppliedByPid(Long pid, Boolean isPersonalApplied);

    void updatePaymentTypeToNullByPaymentTypePid(Long paymentTypePid);

    void updateAdditionalVerificationImageIdByPid(Long pid, String additionalVerificationImageId);

    void updateDeliveryTypeByPid(Long pid, DeliveryType deliveryType);

    /**
     * DELETE
     */

    void deleteByUser_ChatId(Long chatId);

    void deleteByPidIn(List<Long> pidList);
}
