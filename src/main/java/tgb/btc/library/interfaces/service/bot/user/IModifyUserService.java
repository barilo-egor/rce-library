package tgb.btc.library.interfaces.service.bot.user;

import org.springframework.data.jpa.repository.Modifying;
import tgb.btc.library.bean.bot.PaymentType;
import tgb.btc.library.constants.enums.bot.CryptoCurrency;
import tgb.btc.library.constants.enums.bot.DealStatus;
import tgb.btc.library.constants.enums.bot.DeliveryType;
import tgb.btc.library.constants.enums.bot.FiatCurrency;

import java.math.BigDecimal;
import java.util.List;

public interface IModifyUserService {

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

    @Modifying
    void deleteByUser_ChatId(Long chatId);

    @Modifying
    void deleteByPidIn(List<Long> pidList);
}
