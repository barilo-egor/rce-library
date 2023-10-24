package tgb.btc.library.service.bean.bot;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tgb.btc.api.library.IReviewPriseService;
import tgb.btc.api.web.INotifier;
import tgb.btc.library.bean.bot.Deal;
import tgb.btc.library.bean.bot.PaymentReceipt;
import tgb.btc.library.bean.bot.PaymentType;
import tgb.btc.library.bean.bot.User;
import tgb.btc.library.constants.enums.ReferralType;
import tgb.btc.library.constants.enums.bot.CryptoCurrency;
import tgb.btc.library.constants.enums.bot.DealStatus;
import tgb.btc.library.constants.enums.bot.DealType;
import tgb.btc.library.constants.enums.bot.FiatCurrency;
import tgb.btc.library.constants.enums.properties.CommonProperties;
import tgb.btc.library.constants.enums.properties.VariableType;
import tgb.btc.library.exception.BaseException;
import tgb.btc.library.repository.BaseRepository;
import tgb.btc.library.repository.bot.DealRepository;
import tgb.btc.library.repository.bot.UserRepository;
import tgb.btc.library.service.bean.BasePersistService;
import tgb.btc.library.service.process.BanningUserService;
import tgb.btc.library.service.process.CalculateService;
import tgb.btc.library.service.schedule.DealDeleteScheduler;
import tgb.btc.library.util.BigDecimalUtil;
import tgb.btc.library.util.properties.VariablePropertiesUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class DealService extends BasePersistService<Deal> {

    private final DealRepository dealRepository;

    private UserRepository userRepository;

    private BanningUserService banningUserService;

    private PaymentRequisiteService paymentRequisiteService;

    private UserService userService;

    private CalculateService calculateService;

    private IReviewPriseService reviewPriseService;

    private INotifier notifier;

    @Autowired
    public void setNotifier(INotifier notifier) {
        this.notifier = notifier;
    }

    @Autowired(required = false)
    public void setReviewPriseService(IReviewPriseService reviewPriseService) {
        this.reviewPriseService = reviewPriseService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setCalculateService(CalculateService calculateService) {
        this.calculateService = calculateService;
    }

    @Autowired
    public void setPaymentRequisiteService(PaymentRequisiteService paymentRequisiteService) {
        this.paymentRequisiteService = paymentRequisiteService;
    }

    @Autowired
    public void setBanningUserService(BanningUserService banningUserService) {
        this.banningUserService = banningUserService;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public DealService(BaseRepository<Deal> baseRepository, DealRepository dealRepository) {
        super(baseRepository);
        this.dealRepository = dealRepository;
    }

    public CryptoCurrency getCryptoCurrencyByPid(@Param("pid") Long pid) {
        return dealRepository.getCryptoCurrencyByPid(pid);
    }

    public boolean existByPid(Long pid) {
        return dealRepository.existsById(pid);
    }

    public Long getDealsCountByUserChatId(Long chatId) {
        return dealRepository.getPassedDealsCountByUserChatId(chatId);
    }

    public Deal getByPid(Long pid) {
        return dealRepository.findByPid(pid);
    }

    public void updatePaymentTypeByPid(PaymentType paymentType, Long pid) {
        dealRepository.updatePaymentTypeByPid(paymentType, pid);
    }

    public Long getPidActiveDealByChatId(Long chatId) {
        return dealRepository.getPidActiveDealByChatId(chatId);
    }

    public Long getCountPassedByUserChatId(Long chatId) {
        return dealRepository.getCountPassedByUserChatId(chatId);
    }

    public List<Long> getActiveDealPids() {
        return dealRepository.getActiveDealPids();
    }

    public Long getUserChatIdByDealPid(Long pid) {
        return dealRepository.getUserChatIdByDealPid(pid);
    }

    public List<Deal> getByDateBetween(LocalDate startDate, LocalDate endDate) {
        return dealRepository.getByDateBetween(startDate, endDate);
    }

    public List<Deal> getByDate(LocalDate dateTime) {
        return dealRepository.getPassedByDate(dateTime);
    }

    public DealType getDealTypeByPid(Long pid) {
        return dealRepository.getDealTypeByPid(pid);
    }

    @Transactional(readOnly = true)
    public List<PaymentReceipt> getPaymentReceipts(Long dealPid) {
        Deal deal = getByPid(dealPid);
        return new ArrayList<>(deal.getPaymentReceipts());
    }

    public Deal createNewDeal(DealType dealType, Long chatId) {
        Deal deal = new Deal();
        deal.setDealStatus(DealStatus.NEW);
        deal.setActive(false);
        deal.setPassed(false);
        deal.setDateTime(LocalDateTime.now());
        deal.setDate(LocalDate.now());
        deal.setDealType(dealType);
        deal.setUser(userRepository.findByChatId(chatId));
        Deal savedDeal = save(deal);
        userRepository.updateCurrentDealByChatId(savedDeal.getPid(), chatId);
        return savedDeal;
    }

    public boolean isFirstDeal(Long chatId) {
        return getDealsCountByUserChatId(chatId) < 1;
    }

    @Transactional
    public void deleteDeal(Long dealPid, Boolean isBanUser) {
        Long userChatId = getUserChatIdByDealPid(dealPid);
        deleteById(dealPid);
        userRepository.updateCurrentDealByChatId(null, userChatId);
        if (BooleanUtils.isTrue(isBanUser)) banningUserService.ban(userChatId);
        DealDeleteScheduler.deleteCryptoDeal(dealPid);
    }

    @Transactional
    public void confirm(Long dealPid) {
        Deal deal = getByPid(dealPid);
        User user = deal.getUser();

        deal.setActive(false);
        deal.setPassed(true);

        if (BooleanUtils.isTrue(deal.getUsedReferralDiscount())) {
            BigDecimal referralBalance = BigDecimal.valueOf(user.getReferralBalance());
            BigDecimal sumWithDiscount;
            if (ReferralType.STANDARD.isCurrent() && FiatCurrency.BYN.equals(deal.getFiatCurrency())
                    && CommonProperties.VARIABLE.isNotBlank("course.rub.byn")) {
                referralBalance = referralBalance.multiply(CommonProperties.VARIABLE.getBigDecimal("course.rub.byn"));
            }
            if (referralBalance.compareTo(deal.getOriginalPrice()) < 1) {
                sumWithDiscount = deal.getOriginalPrice().subtract(referralBalance);
                referralBalance = BigDecimal.ZERO;
            } else {
                sumWithDiscount = BigDecimal.ZERO;
                referralBalance = referralBalance.subtract(deal.getOriginalPrice()).setScale(0, RoundingMode.HALF_UP);
                if (ReferralType.STANDARD.isCurrent() && FiatCurrency.BYN.equals(deal.getFiatCurrency())
                        && CommonProperties.VARIABLE.isNotBlank("course.byn.rub")) {
                    referralBalance = referralBalance.divide(CommonProperties.VARIABLE.getBigDecimal("course.byn.rub"), RoundingMode.HALF_UP);
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
        userService.save(user);
        if (user.getFromChatId() != null) {
            User refUser = userService.findByChatId(user.getFromChatId());
            BigDecimal refUserReferralPercent = userRepository.getReferralPercentByChatId(refUser.getChatId());
            boolean isGeneralReferralPercent = Objects.isNull(refUserReferralPercent) || refUserReferralPercent.compareTo(BigDecimal.ZERO) == 0;
            BigDecimal referralPercent = isGeneralReferralPercent
                    ? BigDecimal.valueOf(VariablePropertiesUtil.getDouble(VariableType.REFERRAL_PERCENT))
                    : refUserReferralPercent;
            BigDecimal sumToAdd = BigDecimalUtil.multiplyHalfUp(deal.getAmount(),
                    calculateService.getPercentsFactor(referralPercent));
            if (ReferralType.STANDARD.isCurrent() && FiatCurrency.BYN.equals(deal.getFiatCurrency())
                    && CommonProperties.VARIABLE.isNotBlank("course.byn.rub")) {
                sumToAdd = sumToAdd.divide(CommonProperties.VARIABLE.getBigDecimal("course.byn.rub"), RoundingMode.HALF_UP);
            }
            Integer total = refUser.getReferralBalance() + sumToAdd.intValue();
            userService.updateReferralBalanceByChatId(total, refUser.getChatId());
            if (BigDecimal.ZERO.compareTo(sumToAdd) != 0)
                notifier.sendNotify(refUser.getChatId(), "На реферальный баланс было добавлено " + sumToAdd.intValue() + "₽ по сделке партнера.");
            userService.updateChargesByChatId(refUser.getCharges() + sumToAdd.intValue(), refUser.getChatId());
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
        notifier.sendNotify(deal.getUser().getChatId(), message);

        if (Objects.nonNull(reviewPriseService)) reviewPriseService.processReviewPrise(deal.getPid());
    }
}
