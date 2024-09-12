package tgb.btc.library.service.process;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tgb.btc.library.bean.bot.Deal;
import tgb.btc.library.constants.enums.ReferralType;
import tgb.btc.library.constants.enums.bot.FiatCurrency;
import tgb.btc.library.interfaces.IModule;
import tgb.btc.library.interfaces.service.process.IReferralService;
import tgb.btc.library.service.properties.VariablePropertiesReader;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.function.Predicate;

@Service
public class ReferralService implements IReferralService {

    private final IModule<ReferralType> referralModule;

    private final VariablePropertiesReader variablePropertiesReader;

    @Autowired
    public ReferralService(IModule<ReferralType> referralModule, VariablePropertiesReader variablePropertiesReader) {
        this.referralModule = referralModule;
        this.variablePropertiesReader = variablePropertiesReader;
    }

    @Override
    public void processReferralDiscount(Deal deal) {
        BigDecimal referralBalance = BigDecimal.valueOf(deal.getUser().getReferralBalance());
        BigDecimal sumWithDiscount;
        Predicate<String> isNotBlankFunction = variablePropertiesReader::isNotBlank;
        boolean isConversionNeeded = referralModule.isCurrent(ReferralType.STANDARD)
                && FiatCurrency.BYN.equals(deal.getFiatCurrency())
                && isNotBlankFunction.test("course.rub.byn")
                && isNotBlankFunction.test("course.byn.rub");
        if (isConversionNeeded) {
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
}
