package tgb.btc.library.service.process;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tgb.btc.library.constants.enums.CryptoApi;
import tgb.btc.library.constants.enums.bot.CryptoCurrency;
import tgb.btc.library.constants.enums.bot.DealType;
import tgb.btc.library.constants.enums.bot.FiatCurrency;
import tgb.btc.library.constants.enums.properties.PropertiesPath;
import tgb.btc.library.constants.enums.properties.VariableType;
import tgb.btc.library.exception.BaseException;
import tgb.btc.library.interfaces.enums.ICryptoApiService;
import tgb.btc.library.interfaces.scheduler.ICurrencyGetter;
import tgb.btc.library.util.BigDecimalUtil;
import tgb.btc.library.util.BulkDiscountUtil;
import tgb.btc.library.util.properties.VariablePropertiesUtil;
import tgb.btc.library.vo.calculate.CalculateData;
import tgb.btc.library.vo.calculate.CalculateDataForm;
import tgb.btc.library.vo.calculate.DealAmount;

import java.math.BigDecimal;
import java.util.Objects;

@Service
public class CalculateService {

    private PersonalDiscountsCache personalDiscountsCache;

    private ICurrencyGetter currencyGetter;

    private ICryptoApiService cryptoApiService;

    @Autowired
    public void setCryptoApiService(ICryptoApiService cryptoApiService) {
        this.cryptoApiService = cryptoApiService;
    }

    @Autowired
    public void setCurrencyGetter(ICurrencyGetter currencyGetter) {
        this.currencyGetter = currencyGetter;
    }

    @Autowired
    public void setPersonalDiscountsCache(PersonalDiscountsCache personalDiscountsCache) {
        this.personalDiscountsCache = personalDiscountsCache;
    }

    public DealAmount calculate(Long chatId, BigDecimal enteredAmount, CryptoCurrency cryptoCurrency, FiatCurrency fiatCurrency,
            DealType dealType, boolean withCredited) {
        return calculate(chatId, enteredAmount, cryptoCurrency, fiatCurrency, dealType, null, withCredited);
    }

    public DealAmount calculate(Long chatId, BigDecimal enteredAmount, CryptoCurrency cryptoCurrency, FiatCurrency fiatCurrency,
                                DealType dealType, Boolean isEnteredInCrypto, boolean withCredited) {
        CalculateData calculateData =
                new CalculateData(fiatCurrency, dealType, cryptoCurrency, currencyGetter.getCourseCurrency(cryptoCurrency));

        DealAmount dealAmount = new DealAmount();
        dealAmount.setChatId(chatId);
        dealAmount.setDealType(dealType);
        if (Objects.nonNull(isEnteredInCrypto)) dealAmount.setEnteredInCrypto(isEnteredInCrypto);
        else dealAmount.setEnteredInCrypto(isEnteredInCrypto(cryptoCurrency, enteredAmount));
        dealAmount.setCalculateData(calculateData);
        if (dealAmount.isEnteredInCrypto()) {
            dealAmount.setCryptoAmount(enteredAmount);
            if (DealType.isBuy(dealType)) calculateAmount(dealAmount, calculateData, fiatCurrency, cryptoCurrency, withCredited);
            else calculateAmountForSell(dealAmount, calculateData, fiatCurrency, cryptoCurrency);
        } else {
            dealAmount.setAmount(enteredAmount);
            if (DealType.isBuy(dealType)) calculateCryptoAmount(dealAmount, calculateData, fiatCurrency, cryptoCurrency, withCredited);
            else calculateCryptoAmountForSell(dealAmount, calculateData, fiatCurrency, cryptoCurrency);
        }
        return dealAmount;
    }

    public DealAmount calculate(CalculateDataForm calculateDataForm) {
        return calculate(calculateDataForm, false);
    }

    public DealAmount calculate(CalculateDataForm calculateDataForm, boolean withCredited) {
        FiatCurrency fiatCurrency = calculateDataForm.getFiatCurrency();
        CryptoCurrency cryptoCurrency = calculateDataForm.getCryptoCurrency();
        DealType dealType = calculateDataForm.getDealType();
        CalculateData calculateData = new CalculateData(fiatCurrency, dealType, cryptoCurrency,
                calculateDataForm.getCryptoCourse(), calculateDataForm.getUsdCourse(),
                calculateDataForm.getPersonalDiscount(), calculateDataForm.getBulkDiscount());

        DealAmount dealAmount = new DealAmount();
        dealAmount.setDealType(dealType);
        dealAmount.setEnteredInCrypto(Objects.isNull(calculateDataForm.getAmount()));
        dealAmount.setCalculateData(calculateData);
        if (Objects.nonNull(calculateDataForm.getCryptoAmount())) dealAmount.setCryptoAmount(calculateDataForm.getCryptoAmount());
        else dealAmount.setAmount(calculateDataForm.getAmount());

        if (dealAmount.isEnteredInCrypto()) {
            if (DealType.isBuy(dealType)) calculateAmount(dealAmount, calculateData, fiatCurrency, calculateDataForm.getCryptoCurrency(), withCredited);
            else calculateAmountForSell(dealAmount, calculateData, fiatCurrency, calculateDataForm.getCryptoCurrency());
        } else {
            if (DealType.isBuy(dealType)) calculateCryptoAmount(dealAmount, calculateData, fiatCurrency, calculateDataForm.getCryptoCurrency(), withCredited);
            else calculateCryptoAmountForSell(dealAmount, calculateData, fiatCurrency, calculateDataForm.getCryptoCurrency());
        }
        return dealAmount;
    }

    public DealAmount calculate(BigDecimal enteredAmount, CryptoCurrency cryptoCurrency, FiatCurrency fiatCurrency,
            DealType dealType, Boolean isEnteredInCrypto, BigDecimal personalDiscount) {
        CalculateData calculateData =
                new CalculateData(fiatCurrency, dealType, cryptoCurrency, currencyGetter.getCourseCurrency(cryptoCurrency));
        calculateData.setPersonalDiscount(personalDiscount);

        DealAmount dealAmount = new DealAmount();
        dealAmount.setDealType(dealType);
        if (Objects.nonNull(isEnteredInCrypto)) dealAmount.setEnteredInCrypto(isEnteredInCrypto);
        else dealAmount.setEnteredInCrypto(isEnteredInCrypto(cryptoCurrency, enteredAmount));
        dealAmount.setCalculateData(calculateData);
        if (dealAmount.isEnteredInCrypto()) {
            dealAmount.setCryptoAmount(enteredAmount);
            if (DealType.isBuy(dealType)) calculateAmount(dealAmount, calculateData, fiatCurrency, cryptoCurrency, false);
            else calculateAmountForSell(dealAmount, calculateData, fiatCurrency, cryptoCurrency);
        } else {
            dealAmount.setAmount(enteredAmount);
            if (DealType.isBuy(dealType)) calculateCryptoAmount(dealAmount, calculateData, fiatCurrency, cryptoCurrency, false);
            else calculateCryptoAmountForSell(dealAmount, calculateData, fiatCurrency, cryptoCurrency);
        }
        return dealAmount;
    }

    private boolean isEnteredInCrypto(CryptoCurrency cryptoCurrency, BigDecimal enteredAmount) {
        return !CryptoCurrency.BITCOIN.equals(cryptoCurrency)
                || enteredAmount.compareTo(VariablePropertiesUtil.getBigDecimal(VariableType.DEAL_BTC_MAX_ENTERED_SUM.getKey())) < 0;
    }

    private void calculateCryptoAmount(DealAmount dealAmount, CalculateData calculateData, FiatCurrency fiatCurrency, CryptoCurrency cryptoCurrency, boolean withCredited) {
        BigDecimal amount = dealAmount.getAmount();
        BigDecimal commission = amount.compareTo(calculateData.getFix()) < 0
                ? calculateData.getFixCommission()
                : BigDecimalUtil.multiplyHalfUp(amount, getPercentsFactor(calculateData.getCommission()));
        dealAmount.setCommission(commission);
        amount = amount.subtract(commission);
        BigDecimal bulkDiscount = BulkDiscountUtil.getPercentBySum(amount, fiatCurrency, dealAmount.getDealType(), cryptoCurrency);
        if (BigDecimal.ZERO.compareTo(bulkDiscount) != 0) {
            amount = BigDecimalUtil.addHalfUp(amount, BigDecimalUtil.multiplyHalfUp(amount, getPercentsFactor(bulkDiscount)));
        }
        BigDecimal personal = personalDiscountsCache.getDiscount(dealAmount.getChatId(), dealAmount.getDealType());
        if (BigDecimal.ZERO.compareTo(personal) != 0) {
            amount = BigDecimalUtil.addHalfUp(amount, BigDecimalUtil.multiplyHalfUp(amount, getPercentsFactor(personal)));
        }
        BigDecimal usdCourse = calculateData.getUsdCourse();
        BigDecimal transactionCommission = calculateData.getTransactionalCommission();
        if (Objects.nonNull(transactionCommission)) {
            amount = BigDecimalUtil.subtractHalfUp(amount, BigDecimalUtil.multiplyHalfUp(transactionCommission, usdCourse));
        }
        BigDecimal usd = BigDecimalUtil.divideHalfUp(amount, usdCourse);
        BigDecimal cryptoAmount = BigDecimalUtil.round(BigDecimalUtil.divideHalfUp(usd, calculateData.getCryptoCourse()), 5);
        dealAmount.setCryptoAmount(cryptoAmount);
        if (withCredited) dealAmount.setCreditedAmount(convertToFiat(fiatCurrency, cryptoAmount, calculateData.getCryptoCourse()));
    }

    private void calculateAmount(DealAmount dealAmount, CalculateData calculateData, FiatCurrency fiatCurrency, CryptoCurrency cryptoCurrency, boolean withCredited) {
        BigDecimal amount;
        BigDecimal cryptoAmount = dealAmount.getCryptoAmount();
        BigDecimal personal = Objects.nonNull(calculateData.getPersonalDiscount())
                ? calculateData.getPersonalDiscount()
                : personalDiscountsCache.getDiscount(dealAmount.getChatId(), dealAmount.getDealType());
        if (BigDecimal.ZERO.compareTo(personal) != 0) {
            BigDecimal totalPercentsWithPersonal = BigDecimalUtil.addHalfUp(BigDecimalUtil.HUNDRED, personal);
            BigDecimal onePercentPersonal = BigDecimalUtil.divideHalfUp(cryptoAmount, totalPercentsWithPersonal);
            cryptoAmount = BigDecimalUtil.multiplyHalfUp(onePercentPersonal, BigDecimalUtil.HUNDRED);
        }
        BigDecimal bulkDiscount = Objects.nonNull(calculateData.getBulkDiscount())
                ? calculateData.getBulkDiscount()
                : BulkDiscountUtil.getPercentBySum(
                convertToFiat(cryptoAmount, calculateData.getCryptoCourse(), calculateData.getUsdCourse()), fiatCurrency,
                dealAmount.getDealType(), cryptoCurrency);
        if (BigDecimal.ZERO.compareTo(bulkDiscount) != 0) {
            BigDecimal totalPercentsWithBulk = BigDecimalUtil.addHalfUp(BigDecimalUtil.HUNDRED, bulkDiscount);
            BigDecimal onePercentBulk = BigDecimalUtil.divideHalfUp(cryptoAmount, totalPercentsWithBulk);
            cryptoAmount = BigDecimalUtil.multiplyHalfUp(onePercentBulk, BigDecimalUtil.HUNDRED);
        }

        BigDecimal cryptoCourse = calculateData.getCryptoCourse();
        BigDecimal usd = BigDecimalUtil.multiplyHalfUp(cryptoAmount, cryptoCourse);
        BigDecimal course = calculateData.getUsdCourse();
        BigDecimal rub = BigDecimalUtil.multiplyHalfUp(usd, course);
        if (rub.compareTo(calculateData.getFix()) < 0){
            BigDecimal commission = rub.compareTo(calculateData.getFix()) < 0
                    ? calculateData.getFixCommission()
                    : getCommission(cryptoAmount, cryptoCourse, calculateData.getCommission(), course);
            dealAmount.setCommission(commission);
            amount = BigDecimalUtil.addHalfUp(rub, commission);
        } else {
            BigDecimal currentPercents = BigDecimalUtil.subtractHalfUp(BigDecimal.valueOf(100), calculateData.getCommission());
            BigDecimal onePercent = BigDecimalUtil.divideHalfUp(cryptoAmount, currentPercents);
            BigDecimal totalCommission = BigDecimalUtil.multiplyHalfUp(onePercent, calculateData.getCommission());
            BigDecimal usdTotalCommission = BigDecimalUtil.multiplyHalfUp(totalCommission, cryptoCourse);
            BigDecimal rubTotalCommission = BigDecimalUtil.multiplyHalfUp(usdTotalCommission, course);
            dealAmount.setCommission(rubTotalCommission);
            BigDecimal totalCryptoAmount = BigDecimalUtil.addHalfUp(cryptoAmount, totalCommission);
            usd = BigDecimalUtil.multiplyHalfUp(totalCryptoAmount, cryptoCourse);
            amount = BigDecimalUtil.multiplyHalfUp(usd, course);
        }
        BigDecimal transactionCommission = calculateData.getTransactionalCommission();
        if (Objects.nonNull(transactionCommission)) {
            amount = BigDecimalUtil.addHalfUp(amount, BigDecimalUtil.multiplyHalfUp(transactionCommission, course));
        }
        dealAmount.setAmount(amount);
        if (withCredited) dealAmount.setCreditedAmount(convertToFiat(fiatCurrency, cryptoAmount, calculateData.getCryptoCourse()));
    }

    private BigDecimal convertToFiat(BigDecimal cryptoAmount, BigDecimal cryptoCourse, BigDecimal usdCourse) {
        return BigDecimalUtil.multiplyHalfUp(usdCourse,
                BigDecimalUtil.multiplyHalfUp(cryptoAmount, cryptoCourse));
    }

    private void calculateCryptoAmountForSell(DealAmount dealAmount, CalculateData calculateData, FiatCurrency fiatCurrency, CryptoCurrency cryptoCurrency) {
        BigDecimal amount = dealAmount.getAmount();
        BigDecimal commission = amount.compareTo(calculateData.getFix()) < 0
                ? calculateData.getFixCommission()
                : getCommissionForSell(amount, calculateData.getCommission());
        amount = amount.add(commission);
        BigDecimal bulkDiscount = BulkDiscountUtil.getPercentBySum(amount, fiatCurrency, dealAmount.getDealType(), cryptoCurrency);
        if (BigDecimal.ZERO.compareTo(bulkDiscount) != 0) {
            amount = BigDecimalUtil.subtractHalfUp(amount, BigDecimalUtil.multiplyHalfUp(amount, getPercentsFactor(bulkDiscount)));
        }
        BigDecimal personal = personalDiscountsCache.getDiscount(dealAmount.getChatId(), dealAmount.getDealType());
        if (BigDecimal.ZERO.compareTo(personal) != 0) {
            amount = BigDecimalUtil.subtractHalfUp(amount, BigDecimalUtil.multiplyHalfUp(amount, getPercentsFactor(personal)));
        }
        BigDecimal usd = BigDecimalUtil.divideHalfUp(amount, calculateData.getUsdCourse());
        dealAmount.setCommission(commission);
        dealAmount.setCryptoAmount(BigDecimalUtil.divideHalfUp(usd, calculateData.getCryptoCourse()));
    }

    private void calculateAmountForSell(DealAmount dealAmount, CalculateData calculateData, FiatCurrency fiatCurrency, CryptoCurrency cryptoCurrency) {
        BigDecimal cryptoAmount = dealAmount.getCryptoAmount();
        BigDecimal personal = Objects.nonNull(calculateData.getPersonalDiscount())
                ? calculateData.getPersonalDiscount()
                : personalDiscountsCache.getDiscount(dealAmount.getChatId(), dealAmount.getDealType());
        if (BigDecimal.ZERO.compareTo(personal) != 0) {
            BigDecimal totalPercentsWithPersonal = BigDecimalUtil.subtractHalfUp(BigDecimalUtil.HUNDRED, personal);
            BigDecimal onePercentPersonal = BigDecimalUtil.divideHalfUp(cryptoAmount, totalPercentsWithPersonal);
            cryptoAmount = BigDecimalUtil.multiplyHalfUp(onePercentPersonal, BigDecimalUtil.HUNDRED);
        }
        BigDecimal bulkDiscount = Objects.nonNull(calculateData.getBulkDiscount())
                ? calculateData.getBulkDiscount()
                : BulkDiscountUtil.getPercentBySum(
                convertToFiat(cryptoAmount, calculateData.getCryptoCourse(), calculateData.getUsdCourse()), fiatCurrency,
                dealAmount.getDealType(), cryptoCurrency);
        if (BigDecimal.ZERO.compareTo(bulkDiscount) != 0) {
            BigDecimal totalPercentsWithBulk = BigDecimalUtil.subtractHalfUp(BigDecimalUtil.HUNDRED, bulkDiscount);
            BigDecimal onePercentBulk = BigDecimalUtil.divideHalfUp(cryptoAmount, totalPercentsWithBulk);
            cryptoAmount = BigDecimalUtil.multiplyHalfUp(onePercentBulk, BigDecimalUtil.HUNDRED);
        }

        BigDecimal usd = BigDecimalUtil.multiplyHalfUp(cryptoAmount, calculateData.getCryptoCourse());
        BigDecimal rub = BigDecimalUtil.multiplyHalfUp(usd, calculateData.getUsdCourse());
        if (rub.compareTo(calculateData.getFix()) < 0) {
            dealAmount.setCommission(calculateData.getFixCommission());
            dealAmount.setAmount(BigDecimalUtil.subtractHalfUp(rub, calculateData.getFixCommission()));
        } else {
            BigDecimal currentPercents = BigDecimalUtil.addHalfUp(BigDecimal.valueOf(100), calculateData.getCommission());
            BigDecimal onePercent = BigDecimalUtil.divideHalfUp(cryptoAmount, currentPercents);
            BigDecimal totalCommission = BigDecimalUtil.multiplyHalfUp(onePercent, calculateData.getCommission());
            BigDecimal usdTotalCommission = BigDecimalUtil.multiplyHalfUp(totalCommission, calculateData.getCryptoCourse());
            BigDecimal rubTotalCommission = BigDecimalUtil.multiplyHalfUp(usdTotalCommission, calculateData.getUsdCourse());
            dealAmount.setCommission(rubTotalCommission);
            BigDecimal totalCryptoAmount = BigDecimalUtil.subtractHalfUp(cryptoAmount, totalCommission);
            usd = BigDecimalUtil.multiplyHalfUp(totalCryptoAmount, calculateData.getCryptoCourse());
            dealAmount.setAmount(BigDecimalUtil.multiplyHalfUp(usd, calculateData.getUsdCourse()));
        }
    }

    public BigDecimal getCommissionForSell(BigDecimal rub, BigDecimal percentCommission) {
        return BigDecimalUtil.multiplyHalfUp(rub, getPercentsFactor(percentCommission));
    }

    private BigDecimal getCommission(BigDecimal amount, BigDecimal cryptoCurrency, BigDecimal percentCommission,
                                     BigDecimal course) {
        BigDecimal usd = BigDecimalUtil.multiplyHalfUp(amount, cryptoCurrency);
        BigDecimal rub = BigDecimalUtil.multiplyHalfUp(usd, course);
        return BigDecimalUtil.multiplyHalfUp(rub, getPercentsFactor(percentCommission));
    }

    public BigDecimal getPercentsFactor(BigDecimal sum) {
        return BigDecimalUtil.divideHalfUp(sum, BigDecimal.valueOf(100));
    }

    public BigDecimal calculateDiscountInFiat(DealType dealType, BigDecimal amount, BigDecimal discount) {
        return getPercentsFactor(amount).multiply(discount);
    }

    public BigDecimal calculateDiscountInCrypto(CalculateData calculateData, BigDecimal discountInFiat) {
        BigDecimal usd = BigDecimalUtil.divideHalfUp(discountInFiat, calculateData.getUsdCourse());
        return BigDecimalUtil.divideHalfUp(usd, calculateData.getCryptoCourse());
    }

    public BigDecimal convertToFiat(FiatCurrency fiatCurrency, BigDecimal cryptoAmount, BigDecimal cryptoCourse) {
        return BigDecimalUtil.multiplyHalfUp(BigDecimalUtil.multiplyHalfUp(cryptoAmount, cryptoCourse), getUSDToFiat(fiatCurrency));
    }

    public BigDecimal getUSDToFiat(FiatCurrency fiatCurrency) {
        if (!FiatCurrency.RUB.equals(fiatCurrency))
            throw new BaseException("Реализация предусмотрена только для " + FiatCurrency.RUB.name());
        BigDecimal usdRubManualCourse = PropertiesPath.VARIABLE_PROPERTIES.getBigDecimal("usd.rub.course");
        if (Objects.nonNull(usdRubManualCourse)) {
            return usdRubManualCourse;
        }
        return cryptoApiService.getCourse(CryptoApi.USD_RUB_EXCHANGERATE);
    }

}
