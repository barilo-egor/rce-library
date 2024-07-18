package tgb.btc.library.service.bean.bot.deal;

import org.apache.commons.lang.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tgb.btc.api.library.IReviewPriseProcessService;
import tgb.btc.api.web.INotifier;
import tgb.btc.library.bean.bot.Deal;
import tgb.btc.library.bean.bot.PaymentType;
import tgb.btc.library.bean.bot.User;
import tgb.btc.library.constants.enums.CreateType;
import tgb.btc.library.constants.enums.ReferralType;
import tgb.btc.library.constants.enums.bot.*;
import tgb.btc.library.constants.enums.properties.VariableType;
import tgb.btc.library.exception.BaseException;
import tgb.btc.library.interfaces.service.bean.bot.IPaymentRequisiteService;
import tgb.btc.library.interfaces.service.bean.bot.deal.IModifyDealService;
import tgb.btc.library.interfaces.service.bean.bot.deal.IReadDealService;
import tgb.btc.library.interfaces.service.bean.bot.deal.read.IDealUserService;
import tgb.btc.library.interfaces.service.bean.bot.user.IModifyUserService;
import tgb.btc.library.interfaces.service.bean.bot.user.IReadUserService;
import tgb.btc.library.repository.BaseRepository;
import tgb.btc.library.repository.bot.deal.ModifyDealRepository;
import tgb.btc.library.service.bean.BasePersistService;
import tgb.btc.library.service.process.BanningUserService;
import tgb.btc.library.service.process.CalculateService;
import tgb.btc.library.service.properties.VariablePropertiesReader;
import tgb.btc.library.service.schedule.DealDeleteScheduler;
import tgb.btc.library.util.BigDecimalUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;
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

    private IPaymentRequisiteService paymentRequisiteService;

    private CalculateService calculateService;

    private INotifier notifier;

    private IReviewPriseProcessService reviewPriseService;

    private VariablePropertiesReader variablePropertiesReader;

    @Autowired
    public void setVariablePropertiesReader(VariablePropertiesReader variablePropertiesReader) {
        this.variablePropertiesReader = variablePropertiesReader;
    }

    @Autowired(required = false)
    public void setReviewPriseService(IReviewPriseProcessService reviewPriseService) {
        this.reviewPriseService = reviewPriseService;
    }

    @Autowired(required = false)
    public void setNotifier(INotifier notifier) {
        this.notifier = notifier;
    }

    @Autowired
    public void setCalculateService(CalculateService calculateService) {
        this.calculateService = calculateService;
    }

    @Autowired
    public void setPaymentRequisiteService(IPaymentRequisiteService paymentRequisiteService) {
        this.paymentRequisiteService = paymentRequisiteService;
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
        DealDeleteScheduler.deleteCryptoDeal(dealPid);
    }

    @Transactional
    public void confirm(Long dealPid) {
        Deal deal = readDealService.findByPid(dealPid);
        User user = deal.getUser();

        if (BooleanUtils.isTrue(deal.getUsedReferralDiscount())) {
            BigDecimal referralBalance = BigDecimal.valueOf(user.getReferralBalance());
            BigDecimal sumWithDiscount;
            if (ReferralType.STANDARD.isCurrent() && FiatCurrency.BYN.equals(deal.getFiatCurrency())
                    && variablePropertiesReader.isNotBlank("course.rub.byn")) {
                referralBalance = referralBalance.multiply(variablePropertiesReader.getBigDecimal("course.rub.byn"));
            }
            if (referralBalance.compareTo(deal.getOriginalPrice()) < 1) {
                sumWithDiscount = deal.getOriginalPrice().subtract(referralBalance);
                referralBalance = BigDecimal.ZERO;
            } else {
                sumWithDiscount = BigDecimal.ZERO;
                referralBalance = referralBalance.subtract(deal.getOriginalPrice()).setScale(0, RoundingMode.HALF_UP);
                if (ReferralType.STANDARD.isCurrent() && FiatCurrency.BYN.equals(deal.getFiatCurrency())
                        && variablePropertiesReader.isNotBlank("course.byn.rub")) {
                    referralBalance = referralBalance.divide(variablePropertiesReader.getBigDecimal("course.byn.rub"), RoundingMode.HALF_UP);
                }
            }
            user.setReferralBalance(referralBalance.intValue());
            deal.setAmount(sumWithDiscount);
        }
        deal.setDealStatus(DealStatus.CONFIRMED);
        save(deal);
        DealDeleteScheduler.deleteCryptoDeal(deal.getPid());
        paymentRequisiteService.updateOrder(deal.getPaymentType().getPid());
        if (Objects.nonNull(user.getLotteryCount())) user.setLotteryCount(user.getLotteryCount() + 1);
        else user.setLotteryCount(1);
        user.setCurrentDeal(null);
        modifyUserService.save(user);
        if (user.getFromChatId() != null) {
            User refUser = readUserService.findByChatId(user.getFromChatId());
            BigDecimal refUserReferralPercent = readUserService.getReferralPercentByChatId(refUser.getChatId());
            boolean isGeneralReferralPercent = Objects.isNull(refUserReferralPercent) || refUserReferralPercent.compareTo(BigDecimal.ZERO) == 0;
            BigDecimal referralPercent = isGeneralReferralPercent
                    ? BigDecimal.valueOf(variablePropertiesReader.getDouble(VariableType.REFERRAL_PERCENT))
                    : refUserReferralPercent;
            BigDecimal sumToAdd = BigDecimalUtil.multiplyHalfUp(deal.getAmount(),
                    calculateService.getPercentsFactor(referralPercent));
            if (ReferralType.STANDARD.isCurrent() && FiatCurrency.BYN.equals(deal.getFiatCurrency())
                    && variablePropertiesReader.isNotBlank("course.byn.rub")) {
                sumToAdd = sumToAdd.divide(variablePropertiesReader.getBigDecimal("course.byn.rub"), RoundingMode.HALF_UP);
            }
            Integer total = refUser.getReferralBalance() + sumToAdd.intValue();
            modifyUserService.updateReferralBalanceByChatId(total, refUser.getChatId());
            if (BigDecimal.ZERO.compareTo(sumToAdd) != 0 && Objects.nonNull(notifier))
                notifier.sendNotify(refUser.getChatId(), "На реферальный баланс было добавлено " + sumToAdd.intValue() + "₽ по сделке партнера.");
            modifyUserService.updateChargesByChatId(refUser.getCharges() + sumToAdd.intValue(), refUser.getChatId());
        }
        String message;
        if (!DealType.isBuy(deal.getDealType())) {
            message = "Заявка обработана, деньги отправлены.";
        } else {
            switch (deal.getCryptoCurrency()) {
                case BITCOIN:
                    message = "Биткоин отправлен ✅\nhttps://blockchair.com/bitcoin/address/" + deal.getWallet();
                    break;
                case LITECOIN:
                    message = "Валюта отправлена.\nhttps://blockchair.com/ru/litecoin/address/" + deal.getWallet();
                    break;
                case USDT:
                    message = "Валюта отправлена.https://tronscan.io/#/address/" + deal.getWallet();
                    break;
                case MONERO:
                    message = "Валюта отправлена."; // TODO добавить url
                    break;
                default:
                    throw new BaseException("Не найдена криптовалюта у сделки. dealPid=" + deal.getPid());
            }
        }
        if (Objects.nonNull(notifier)) notifier.sendNotify(deal.getUser().getChatId(), message);
        if (Objects.nonNull(reviewPriseService)) reviewPriseService.processReviewPrise(deal.getPid());
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
