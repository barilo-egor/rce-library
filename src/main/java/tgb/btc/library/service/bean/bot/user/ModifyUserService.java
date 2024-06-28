package tgb.btc.library.service.bean.bot.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tgb.btc.library.bean.bot.PaymentType;
import tgb.btc.library.bean.bot.User;
import tgb.btc.library.constants.enums.bot.CryptoCurrency;
import tgb.btc.library.constants.enums.bot.DealStatus;
import tgb.btc.library.constants.enums.bot.DeliveryType;
import tgb.btc.library.constants.enums.bot.FiatCurrency;
import tgb.btc.library.interfaces.service.bot.user.IModifyUserService;
import tgb.btc.library.repository.BaseRepository;
import tgb.btc.library.repository.bot.deal.ModifyDealRepository;
import tgb.btc.library.service.bean.BasePersistService;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ModifyUserService extends BasePersistService<User> implements IModifyUserService {

    private ModifyDealRepository modifyDealRepository;

    @Autowired
    public void setModifyDealRepository(ModifyDealRepository modifyDealRepository) {
        this.modifyDealRepository = modifyDealRepository;
    }

    @Autowired
    public ModifyUserService(BaseRepository<User> baseRepository) {
        super(baseRepository);
    }

    @Override
    public void updateCryptoCurrencyByPid(Long pid, CryptoCurrency cryptoCurrency) {
        modifyDealRepository.updateCryptoCurrencyByPid(pid, cryptoCurrency);
    }

    @Override
    public void updateCryptoAmountByPid(BigDecimal cryptoAmount, Long pid) {
        modifyDealRepository.updateCryptoAmountByPid(cryptoAmount, pid);
    }

    @Override
    public void updateAmountByPid(BigDecimal amount, Long pid) {
        modifyDealRepository.updateAmountByPid(amount, pid);
    }

    @Override
    public void updateDiscountByPid(BigDecimal discount, Long pid) {
        modifyDealRepository.updateDiscountByPid(discount, pid);
    }

    @Override
    public void updateCommissionByPid(BigDecimal commission, Long pid) {
        modifyDealRepository.updateCommissionByPid(commission, pid);
    }

    @Override
    public void updateUsedReferralDiscountByPid(Boolean isUsedReferralDiscount, Long pid) {
        modifyDealRepository.updateUsedReferralDiscountByPid(isUsedReferralDiscount, pid);
    }

    @Override
    public void updateWalletByPid(String wallet, Long pid) {
        modifyDealRepository.updateWalletByPid(wallet, pid);
    }

    @Override
    public void updatePaymentTypeByPid(PaymentType paymentType, Long pid) {
        modifyDealRepository.updatePaymentTypeByPid(paymentType, pid);
    }

    @Override
    public void updateIsUsedPromoByPid(Boolean isUsedPromo, Long pid) {
        modifyDealRepository.updateIsUsedPromoByPid(isUsedPromo, pid);
    }

    @Override
    public void updateDealStatusByPid(DealStatus dealStatus, Long pid) {
        modifyDealRepository.updateDealStatusByPid(dealStatus, pid);
    }

    @Override
    public void updateFiatCurrencyByPid(Long pid, FiatCurrency fiatCurrency) {
        modifyDealRepository.updateFiatCurrencyByPid(pid, fiatCurrency);
    }

    @Override
    public void updateIsPersonalAppliedByPid(Long pid, Boolean isPersonalApplied) {
        modifyDealRepository.updateIsPersonalAppliedByPid(pid, isPersonalApplied);
    }

    @Override
    public void updatePaymentTypeToNullByPaymentTypePid(Long paymentTypePid) {
        modifyDealRepository.updatePaymentTypeToNullByPaymentTypePid(paymentTypePid);
    }

    @Override
    public void updateAdditionalVerificationImageIdByPid(Long pid, String additionalVerificationImageId) {
        modifyDealRepository.updateAdditionalVerificationImageIdByPid(pid, additionalVerificationImageId);
    }

    @Override
    public void updateDeliveryTypeByPid(Long pid, DeliveryType deliveryType) {
        modifyDealRepository.updateDeliveryTypeByPid(pid, deliveryType);
    }

    @Override
    public void deleteByUser_ChatId(Long chatId) {
        modifyDealRepository.deleteByUser_ChatId(chatId);
    }

    @Override
    public void deleteByPidIn(List<Long> pidList) {
        modifyDealRepository.deleteByPidIn(pidList);
    }

}
