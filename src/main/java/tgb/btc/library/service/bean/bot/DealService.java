package tgb.btc.library.service.bean.bot;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tgb.btc.api.library.IReviewPriseProcessService;
import tgb.btc.api.web.INotifier;
import tgb.btc.library.bean.bot.Deal;
import tgb.btc.library.bean.bot.PaymentReceipt;
import tgb.btc.library.bean.bot.PaymentType;
import tgb.btc.library.bean.bot.User;
import tgb.btc.library.constants.enums.CreateType;
import tgb.btc.library.constants.enums.ReferralType;
import tgb.btc.library.constants.enums.bot.CryptoCurrency;
import tgb.btc.library.constants.enums.bot.DealStatus;
import tgb.btc.library.constants.enums.bot.DealType;
import tgb.btc.library.constants.enums.bot.FiatCurrency;
import tgb.btc.library.constants.enums.properties.PropertiesPath;
import tgb.btc.library.constants.enums.properties.VariableType;
import tgb.btc.library.exception.BaseException;
import tgb.btc.library.interfaces.service.bean.bot.user.IModifyUserService;
import tgb.btc.library.interfaces.service.bean.bot.user.IReadUserService;
import tgb.btc.library.interfaces.util.IBigDecimalService;
import tgb.btc.library.repository.BaseRepository;
import tgb.btc.library.repository.bot.DealRepository;
import tgb.btc.library.service.bean.BasePersistService;
import tgb.btc.library.service.process.BanningUserService;
import tgb.btc.library.service.process.CalculateService;
import tgb.btc.library.service.properties.VariablePropertiesReader;
import tgb.btc.library.service.schedule.DealDeleteScheduler;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@Transactional
public class DealService extends BasePersistService<Deal> {

    private DealRepository dealRepository;

    private BanningUserService banningUserService;

    private PaymentRequisiteService paymentRequisiteService;

    private IReadUserService readUserService;

    private IModifyUserService modifyUserService;

    private CalculateService calculateService;

    private IReviewPriseProcessService reviewPriseService;

    private INotifier notifier;

    private VariablePropertiesReader variablePropertiesReader;
    
    private IBigDecimalService bigDecimalService;

    @Autowired
    public void setBigDecimalService(IBigDecimalService bigDecimalService) {
        this.bigDecimalService = bigDecimalService;
    }

    @Autowired
    public void setVariablePropertiesReader(VariablePropertiesReader variablePropertiesReader) {
        this.variablePropertiesReader = variablePropertiesReader;
    }

    @Autowired
    public void setModifyUserService(IModifyUserService modifyUserService) {
        this.modifyUserService = modifyUserService;
    }

    @Autowired
    public void setReadUserService(IReadUserService readUserService) {
        this.readUserService = readUserService;
    }

    @Autowired(required = false)
    public void setNotifier(INotifier notifier) {
        this.notifier = notifier;
    }

    @Autowired(required = false)
    public void setReviewPriseService(IReviewPriseProcessService reviewPriseService) {
        this.reviewPriseService = reviewPriseService;
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
    public void setDealRepository(DealRepository dealRepository) {
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

    public Long getCountPassedByUserChatId(Long chatId) {
        return dealRepository.getCountPassedByUserChatId(chatId);
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

    public boolean isFirstDeal(Long chatId) {
        return getDealsCountByUserChatId(chatId) < 1;
    }

    @Transactional
    public void deleteDeal(Long dealPid, Boolean isBanUser) {
        Long userChatId = getUserChatIdByDealPid(dealPid);
        deleteById(dealPid);
        modifyUserService.updateCurrentDealByChatId(null, userChatId);
        if (BooleanUtils.isTrue(isBanUser)) banningUserService.ban(userChatId);
        DealDeleteScheduler.deleteCryptoDeal(dealPid);
    }

    @Transactional
    public void confirm(Long dealPid) {
        Deal deal = getByPid(dealPid);
        User user = deal.getUser();

        if (BooleanUtils.isTrue(deal.getUsedReferralDiscount())) {
            BigDecimal referralBalance = BigDecimal.valueOf(user.getReferralBalance());
            BigDecimal sumWithDiscount;
            if (ReferralType.STANDARD.isCurrent() && FiatCurrency.BYN.equals(deal.getFiatCurrency())
                    && PropertiesPath.VARIABLE_PROPERTIES.isNotBlank("course.rub.byn")) {
                referralBalance = referralBalance.multiply(PropertiesPath.VARIABLE_PROPERTIES.getBigDecimal("course.rub.byn"));
            }
            if (referralBalance.compareTo(deal.getOriginalPrice()) < 1) {
                sumWithDiscount = deal.getOriginalPrice().subtract(referralBalance);
                referralBalance = BigDecimal.ZERO;
            } else {
                sumWithDiscount = BigDecimal.ZERO;
                referralBalance = referralBalance.subtract(deal.getOriginalPrice()).setScale(0, RoundingMode.HALF_UP);
                if (ReferralType.STANDARD.isCurrent() && FiatCurrency.BYN.equals(deal.getFiatCurrency())
                        && PropertiesPath.VARIABLE_PROPERTIES.isNotBlank("course.byn.rub")) {
                    referralBalance = referralBalance.divide(PropertiesPath.VARIABLE_PROPERTIES.getBigDecimal("course.byn.rub"), RoundingMode.HALF_UP);
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
            BigDecimal sumToAdd = bigDecimalService.multiplyHalfUp(deal.getAmount(),
                    calculateService.getPercentsFactor(referralPercent));
            if (ReferralType.STANDARD.isCurrent() && FiatCurrency.BYN.equals(deal.getFiatCurrency())
                    && PropertiesPath.VARIABLE_PROPERTIES.isNotBlank("course.byn.rub")) {
                sumToAdd = sumToAdd.divide(PropertiesPath.VARIABLE_PROPERTIES.getBigDecimal("course.byn.rub"), RoundingMode.HALF_UP);
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


    @Transactional(readOnly = true)
    public List<PaymentReceipt> getPaymentReceipts(Long dealPid) {
        Deal deal = getByPid(dealPid);
        return new ArrayList<>(deal.getPaymentReceipts());
    }

    @Override
    protected BaseRepository<Deal> getBaseRepository() {
        return dealRepository;
    }

}
