package tgb.btc.library.service.bean.bot.deal;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tgb.btc.api.library.IReviewPriseProcessService;
import tgb.btc.api.web.INotifier;
import tgb.btc.library.bean.bot.Deal;
import tgb.btc.library.bean.bot.PaymentType;
import tgb.btc.library.bean.bot.User;
import tgb.btc.library.constants.enums.CreateType;
import tgb.btc.library.constants.enums.bot.*;
import tgb.btc.library.constants.enums.strings.BotMessageConst;
import tgb.btc.library.exception.BaseException;
import tgb.btc.library.interfaces.enums.MessageImage;
import tgb.btc.library.interfaces.service.bean.bot.deal.IModifyDealService;
import tgb.btc.library.interfaces.service.bean.bot.deal.IReadDealService;
import tgb.btc.library.interfaces.service.bean.bot.deal.read.IDealUserService;
import tgb.btc.library.interfaces.service.bean.bot.user.IModifyUserService;
import tgb.btc.library.interfaces.service.bean.bot.user.IReadUserService;
import tgb.btc.library.interfaces.service.design.IMessageImageService;
import tgb.btc.library.interfaces.service.process.ILotteryService;
import tgb.btc.library.interfaces.service.process.IReferralService;
import tgb.btc.library.repository.BaseRepository;
import tgb.btc.library.repository.bot.deal.ModifyDealRepository;
import tgb.btc.library.service.bean.BasePersistService;
import tgb.btc.library.service.process.BanningUserService;
import tgb.btc.library.service.schedule.DealDeleteScheduler;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class ModifyDealService extends BasePersistService<Deal> implements IModifyDealService {

    private ModifyDealRepository modifyDealRepository;

    private IModifyUserService modifyUserService;

    private IReadUserService readUserService;

    private BanningUserService banningUserService;

    private IDealUserService dealUserService;

    private IReadDealService readDealService;

    private INotifier notifier;

    private IReviewPriseProcessService reviewPriseProcessService;

    private DealDeleteScheduler dealDeleteScheduler;

    private IReferralService referralService;

    private ILotteryService lotteryService;

    private IMessageImageService messageImageService;

    @Autowired
    public void setMessageImageService(IMessageImageService messageImageService) {
        this.messageImageService = messageImageService;
    }

    @Autowired
    public void setLotteryService(ILotteryService lotteryService) {
        this.lotteryService = lotteryService;
    }

    @Autowired
    public void setReferralService(IReferralService referralService) {
        this.referralService = referralService;
    }

    @Autowired
    public void setDealDeleteScheduler(DealDeleteScheduler dealDeleteScheduler) {
        this.dealDeleteScheduler = dealDeleteScheduler;
    }

    @Autowired(required = false)
    public void setReviewPriseService(IReviewPriseProcessService reviewPriseService) {
        this.reviewPriseProcessService = reviewPriseService;
    }

    @Autowired(required = false)
    public void setNotifier(INotifier notifier) {
        this.notifier = notifier;
    }

    @Autowired
    public void setReadDealService(IReadDealService readDealService) {
        this.readDealService = readDealService;
    }

    @Autowired
    public void setDealUserService(IDealUserService dealUserService) {
        this.dealUserService = dealUserService;
    }

    @Autowired
    public void setBanningUserService(BanningUserService banningUserService) {
        this.banningUserService = banningUserService;
    }

    @Autowired
    public void setModifyUserService(IModifyUserService modifyUserService) {
        this.modifyUserService = modifyUserService;
    }

    @Autowired
    public void setReadUserService(IReadUserService readUserService) {
        this.readUserService = readUserService;
    }

    @Autowired
    public void setModifyDealRepository(ModifyDealRepository modifyDealRepository) {
        this.modifyDealRepository = modifyDealRepository;
    }

    @Override
    public Deal createNewDeal(DealType dealType, Long chatId) {
        Deal deal = new Deal();
        deal.setDealStatus(DealStatus.NEW);
        deal.setCreateType(CreateType.BOT);
        deal.setDateTime(LocalDateTime.now());
        deal.setDealType(dealType);
        deal.setUser(readUserService.findByChatId(chatId));
        Deal savedDeal = save(deal);
        modifyUserService.updateCurrentDealByChatId(savedDeal.getPid(), chatId);
        return savedDeal;
    }

    @Transactional
    public void deleteDeal(Long dealPid, Boolean isBanUser) {
        Long userChatId = dealUserService.getUserChatIdByDealPid(dealPid);
        deleteById(dealPid);
        modifyUserService.updateCurrentDealByChatId(null, userChatId);
        if (BooleanUtils.isTrue(isBanUser)) banningUserService.ban(userChatId);
        dealDeleteScheduler.deleteDeal(dealPid);
    }

    @Override
    public void deleteById(Long dealPid) {
        Deal deal = findById(dealPid);
        delete(deal);
    }

    @Override
    public void confirm(Long dealPid) {
        confirm(dealPid, null);
    }

    @Override
    @Transactional
    public void confirm(Long dealPid, String hash) {
        Deal deal = readDealService.findByPid(dealPid);
        User user = deal.getUser();
        if (BooleanUtils.isTrue(deal.getUsedReferralDiscount())) {
            referralService.processReferralDiscount(deal);
        }
        deal.setDealStatus(DealStatus.CONFIRMED);
        deal.setHash(hash);
        save(deal);
        dealDeleteScheduler.deleteDeal(deal.getPid());
        lotteryService.addLottery(user);
        user.setCurrentDeal(null);
        modifyUserService.save(user);
        if (user.getFromChatId() != null) {
            referralService.processReferralBonus(deal);
        }
        sendNotify(deal);

        reviewPriseProcessService.processReviewPrise(deal.getPid());
    }

    private void sendNotify(Deal deal) {
        String message;
        CryptoCurrency cryptoCurrency = deal.getCryptoCurrency();
        if (!DealType.isBuy(deal.getDealType())) {
            message = BotMessageConst.DEAL_CONFIRMED.getMessage();
        } else {
            String url;
            if (Objects.nonNull(deal.getHash())) {
                url = String.format(cryptoCurrency.getHashUrl(), deal.getHash());
            } else {
                url = String.format(cryptoCurrency.getAddressUrl(), deal.getWallet());
            }
            switch (cryptoCurrency) {
                case BITCOIN ->
                        message = String.format(messageImageService.getMessage(MessageImage.DEAL_CONFIRMED_BITCOIN), url);
                case LITECOIN ->
                        message = String.format(messageImageService.getMessage(MessageImage.DEAL_CONFIRMED_LITECOIN), url);
                case USDT ->
                        message = String.format(messageImageService.getMessage(MessageImage.DEAL_CONFIRMED_USDT), url);
                case MONERO ->
                        message = String.format(messageImageService.getMessage(MessageImage.DEAL_CONFIRMED_MONERO), url);
                default -> throw new BaseException();
            }
        }
        notifier.sendNotify(deal.getUser().getChatId(), message);
    }

    /**
     * UPDATE
     */

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

    /**
     * DELETE
     */

    @Override
    public void deleteByUser_ChatId(Long chatId) {
        modifyDealRepository.deleteByUser_ChatId(chatId);
    }

    @Override
    public void deleteByPidIn(List<Long> pidList) {
        modifyDealRepository.deleteByPidIn(pidList);
    }

    @Override
    protected BaseRepository<Deal> getBaseRepository() {
        return modifyDealRepository;
    }

}
