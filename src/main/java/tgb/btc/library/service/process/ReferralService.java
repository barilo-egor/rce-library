package tgb.btc.library.service.process;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tgb.btc.api.web.INotifier;
import tgb.btc.library.bean.bot.Deal;
import tgb.btc.library.bean.bot.User;
import tgb.btc.library.constants.enums.ReferralType;
import tgb.btc.library.constants.enums.bot.FiatCurrency;
import tgb.btc.library.constants.enums.properties.VariableType;
import tgb.btc.library.constants.enums.strings.BotMessageConst;
import tgb.btc.library.exception.BaseException;
import tgb.btc.library.interfaces.IModule;
import tgb.btc.library.interfaces.service.bean.bot.user.IModifyUserService;
import tgb.btc.library.interfaces.service.bean.bot.user.IReadUserService;
import tgb.btc.library.interfaces.service.process.IReferralService;
import tgb.btc.library.service.properties.VariablePropertiesReader;
import tgb.btc.library.service.util.BigDecimalService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.function.Predicate;

@Service
public class ReferralService implements IReferralService {

    private final IModule<ReferralType> referralModule;

    private final VariablePropertiesReader variablePropertiesReader;

    private final IReadUserService readUserService;

    private final BigDecimalService bigDecimalService;

    private final INotifier notifier;

    private final CalculateService calculateService;

    private final IModifyUserService modifyUserService;

    @Autowired
    public ReferralService(IModule<ReferralType> referralModule, VariablePropertiesReader variablePropertiesReader,
                           IReadUserService readUserService, BigDecimalService bigDecimalService,
                           @Autowired(required = false) INotifier notifier,
                           CalculateService calculateService, IModifyUserService modifyUserService) {
        this.referralModule = referralModule;
        this.variablePropertiesReader = variablePropertiesReader;
        this.readUserService = readUserService;
        this.bigDecimalService = bigDecimalService;
        this.notifier = notifier;
        this.calculateService = calculateService;
        this.modifyUserService = modifyUserService;
    }

    @Override
    public void processReferralDiscount(Deal deal) {
        BigDecimal referralBalance = BigDecimal.valueOf(deal.getUser().getReferralBalance());
        BigDecimal sumWithDiscount;
        boolean isConversionNeeded = referralModule.isCurrent(ReferralType.STANDARD)
                && FiatCurrency.BYN.equals(deal.getFiatCurrency());
        if (isConversionNeeded) {
            checkCoursesNotBlank();
            referralBalance = referralBalance.multiply(variablePropertiesReader.getBigDecimal("course.rub.byn"));
        }
        if (referralBalance.compareTo(deal.getOriginalPrice()) < 1) {
            sumWithDiscount = deal.getOriginalPrice().subtract(referralBalance);
            referralBalance = BigDecimal.ZERO;
        } else {
            sumWithDiscount = BigDecimal.ZERO;
            referralBalance = referralBalance.subtract(deal.getOriginalPrice()).setScale(0, RoundingMode.HALF_UP);
            if (isConversionNeeded) {
                referralBalance = referralBalance.divide(variablePropertiesReader.getBigDecimal("course.byn.rub"), RoundingMode.HALF_UP);
            }
        }
        deal.getUser().setReferralBalance(referralBalance.intValue());
        deal.setAmount(sumWithDiscount);
    }

    @Override
    public void processReferralBonus(Deal deal) {
        User refUser = readUserService.findByChatId(deal.getUser().getFromChatId());
        BigDecimal sumToAdd = bigDecimalService.multiplyHalfUp(
                deal.getAmount(),
                calculateService.getPercentsFactor(getReferralPercent(refUser))
        );
        if (referralModule.isCurrent(ReferralType.STANDARD) && FiatCurrency.BYN.equals(deal.getFiatCurrency())) {
            checkCoursesNotBlank();
            sumToAdd = sumToAdd.divide(variablePropertiesReader.getBigDecimal("course.byn.rub"), RoundingMode.HALF_UP);
        }
        Integer total = refUser.getReferralBalance() + sumToAdd.intValue();
        modifyUserService.updateReferralBalanceByChatId(total, refUser.getChatId());
        if (BigDecimal.ZERO.compareTo(sumToAdd) != 0) {
            notifier.sendNotify(refUser.getChatId(), String.format(BotMessageConst.FROM_REFERRAL_BALANCE_PURCHASE.getMessage(),
                    sumToAdd.intValue()));
        }
        modifyUserService.updateChargesByChatId(refUser.getCharges() + sumToAdd.intValue(), refUser.getChatId());
    }

    private void checkCoursesNotBlank() {
        Predicate<String> isNotBlankFunction = variablePropertiesReader::isNotBlank;
        if (!isNotBlankFunction.test("course.rub.byn") || !isNotBlankFunction.test("course.byn.rub")) {
            throw new BaseException("Отсутствуют курсы для конвертации rub-byn.");
        }
    }

    private BigDecimal getReferralPercent(User referralUser) {
        BigDecimal refUserReferralPercent = readUserService.getReferralPercentByChatId(referralUser.getChatId());
        if (bigDecimalService.isZeroOrNull(refUserReferralPercent)) {
            return variablePropertiesReader.getBigDecimal(VariableType.REFERRAL_PERCENT.getKey());
        } else {
            return refUserReferralPercent;
        }
    }
}
