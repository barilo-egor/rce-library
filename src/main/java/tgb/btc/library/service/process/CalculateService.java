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
import tgb.btc.library.interfaces.util.IBigDecimalService;
import tgb.btc.library.interfaces.util.IBulkDiscountService;
import tgb.btc.library.service.properties.VariablePropertiesReader;
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

    private VariablePropertiesReader variablePropertiesReader;

    private IBulkDiscountService bulkDiscountService;
    
    private IBigDecimalService bigDecimalService;

    @Autowired
    public void setBigDecimalService(IBigDecimalService bigDecimalService) {
        this.bigDecimalService = bigDecimalService;
    }

    @Autowired
    public void setBulkDiscountService(IBulkDiscountService bulkDiscountService) {
        this.bulkDiscountService = bulkDiscountService;
    }

    @Autowired
    public void setVariablePropertiesReader(VariablePropertiesReader variablePropertiesReader) {
        this.variablePropertiesReader = variablePropertiesReader;
    }

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
                new CalculateData(fiatCurrency, dealType, cryptoCurrency, currencyGetter.getCourseCurrency(cryptoCurrency), variablePropertiesReader);

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
                calculateDataForm.getPersonalDiscount(), calculateDataForm.getBulkDiscount(), variablePropertiesReader);

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
                new CalculateData(fiatCurrency, dealType, cryptoCurrency, currencyGetter.getCourseCurrency(cryptoCurrency), variablePropertiesReader);
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
                || enteredAmount.compareTo(variablePropertiesReader.getBigDecimal(VariableType.DEAL_BTC_MAX_ENTERED_SUM.getKey())) < 0;
    }

    private void calculateCryptoAmount(DealAmount dealAmount, CalculateData calculateData, FiatCurrency fiatCurrency, CryptoCurrency cryptoCurrency, boolean withCredited) {
        BigDecimal amount = dealAmount.getAmount();
        BigDecimal commission = amount.compareTo(calculateData.getFix()) < 0
                ? calculateData.getFixCommission()
                : bigDecimalService.multiplyHalfUp(amount, getPercentsFactor(calculateData.getCommission()));
        dealAmount.setCommission(commission);
        amount = amount.subtract(commission);
        BigDecimal bulkDiscount = bulkDiscountService.getPercentBySum(amount, fiatCurrency, dealAmount.getDealType(), cryptoCurrency);
        if (BigDecimal.ZERO.compareTo(bulkDiscount) != 0) {
            amount = bigDecimalService.addHalfUp(amount, bigDecimalService.multiplyHalfUp(amount, getPercentsFactor(bulkDiscount)));
        }
        BigDecimal personal = personalDiscountsCache.getDiscount(dealAmount.getChatId(), dealAmount.getDealType());
        if (BigDecimal.ZERO.compareTo(personal) != 0) {
            amount = bigDecimalService.addHalfUp(amount, bigDecimalService.multiplyHalfUp(amount, getPercentsFactor(personal)));
        }
        BigDecimal usdCourse = calculateData.getUsdCourse();
        BigDecimal transactionCommission = calculateData.getTransactionalCommission();
        if (Objects.nonNull(transactionCommission)) {
            amount = bigDecimalService.subtractHalfUp(amount, bigDecimalService.multiplyHalfUp(transactionCommission, usdCourse));
        }
        BigDecimal usd = bigDecimalService.divideHalfUp(amount, usdCourse);
        BigDecimal cryptoAmount = bigDecimalService.round(bigDecimalService.divideHalfUp(usd, calculateData.getCryptoCourse()), 5);
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
            BigDecimal totalPercentsWithPersonal = bigDecimalService.addHalfUp(bigDecimalService.getHundred(), personal);
            BigDecimal onePercentPersonal = bigDecimalService.divideHalfUp(cryptoAmount, totalPercentsWithPersonal);
            cryptoAmount = bigDecimalService.multiplyHalfUp(onePercentPersonal, bigDecimalService.getHundred());
        }
        BigDecimal bulkDiscount = Objects.nonNull(calculateData.getBulkDiscount())
                ? calculateData.getBulkDiscount()
                : bulkDiscountService.getPercentBySum(
                convertToFiat(cryptoAmount, calculateData.getCryptoCourse(), calculateData.getUsdCourse()), fiatCurrency,
                dealAmount.getDealType(), cryptoCurrency);
        if (BigDecimal.ZERO.compareTo(bulkDiscount) != 0) {
            BigDecimal totalPercentsWithBulk = bigDecimalService.addHalfUp(bigDecimalService.getHundred(), bulkDiscount);
            BigDecimal onePercentBulk = bigDecimalService.divideHalfUp(cryptoAmount, totalPercentsWithBulk);
            cryptoAmount = bigDecimalService.multiplyHalfUp(onePercentBulk, bigDecimalService.getHundred());
        }

        BigDecimal cryptoCourse = calculateData.getCryptoCourse();
        BigDecimal usd = bigDecimalService.multiplyHalfUp(cryptoAmount, cryptoCourse);
        BigDecimal course = calculateData.getUsdCourse();
        BigDecimal rub = bigDecimalService.multiplyHalfUp(usd, course);
        if (rub.compareTo(calculateData.getFix()) < 0){
            BigDecimal commission = rub.compareTo(calculateData.getFix()) < 0
                    ? calculateData.getFixCommission()
                    : getCommission(cryptoAmount, cryptoCourse, calculateData.getCommission(), course);
            dealAmount.setCommission(commission);
            amount = bigDecimalService.addHalfUp(rub, commission);
        } else {
            BigDecimal currentPercents = bigDecimalService.subtractHalfUp(BigDecimal.valueOf(100), calculateData.getCommission());
            BigDecimal onePercent = bigDecimalService.divideHalfUp(cryptoAmount, currentPercents);
            BigDecimal totalCommission = bigDecimalService.multiplyHalfUp(onePercent, calculateData.getCommission());
            BigDecimal usdTotalCommission = bigDecimalService.multiplyHalfUp(totalCommission, cryptoCourse);
            BigDecimal rubTotalCommission = bigDecimalService.multiplyHalfUp(usdTotalCommission, course);
            dealAmount.setCommission(rubTotalCommission);
            BigDecimal totalCryptoAmount = bigDecimalService.addHalfUp(cryptoAmount, totalCommission);
            usd = bigDecimalService.multiplyHalfUp(totalCryptoAmount, cryptoCourse);
            amount = bigDecimalService.multiplyHalfUp(usd, course);
        }
        BigDecimal transactionCommission = calculateData.getTransactionalCommission();
        if (Objects.nonNull(transactionCommission)) {
            amount = bigDecimalService.addHalfUp(amount, bigDecimalService.multiplyHalfUp(transactionCommission, course));
        }
        dealAmount.setAmount(amount);
        if (withCredited) dealAmount.setCreditedAmount(convertToFiat(fiatCurrency, cryptoAmount, calculateData.getCryptoCourse()));
    }

    private BigDecimal convertToFiat(BigDecimal cryptoAmount, BigDecimal cryptoCourse, BigDecimal usdCourse) {
        return bigDecimalService.multiplyHalfUp(usdCourse,
                bigDecimalService.multiplyHalfUp(cryptoAmount, cryptoCourse));
    }

    private void calculateCryptoAmountForSell(DealAmount dealAmount, CalculateData calculateData, FiatCurrency fiatCurrency, CryptoCurrency cryptoCurrency) {
        BigDecimal amount = dealAmount.getAmount();
        BigDecimal commission = amount.compareTo(calculateData.getFix()) < 0
                ? calculateData.getFixCommission()
                : getCommissionForSell(amount, calculateData.getCommission());
        amount = amount.add(commission);
        BigDecimal bulkDiscount = bulkDiscountService.getPercentBySum(amount, fiatCurrency, dealAmount.getDealType(), cryptoCurrency);
        if (BigDecimal.ZERO.compareTo(bulkDiscount) != 0) {
            amount = bigDecimalService.subtractHalfUp(amount, bigDecimalService.multiplyHalfUp(amount, getPercentsFactor(bulkDiscount)));
        }
        BigDecimal personal = personalDiscountsCache.getDiscount(dealAmount.getChatId(), dealAmount.getDealType());
        if (BigDecimal.ZERO.compareTo(personal) != 0) {
            amount = bigDecimalService.subtractHalfUp(amount, bigDecimalService.multiplyHalfUp(amount, getPercentsFactor(personal)));
        }
        BigDecimal usd = bigDecimalService.divideHalfUp(amount, calculateData.getUsdCourse());
        dealAmount.setCommission(commission);
        dealAmount.setCryptoAmount(bigDecimalService.divideHalfUp(usd, calculateData.getCryptoCourse()));
    }

    private void calculateAmountForSell(DealAmount dealAmount, CalculateData calculateData, FiatCurrency fiatCurrency, CryptoCurrency cryptoCurrency) {
        BigDecimal cryptoAmount = dealAmount.getCryptoAmount();
        BigDecimal personal = Objects.nonNull(calculateData.getPersonalDiscount())
                ? calculateData.getPersonalDiscount()
                : personalDiscountsCache.getDiscount(dealAmount.getChatId(), dealAmount.getDealType());
        if (BigDecimal.ZERO.compareTo(personal) != 0) {
            BigDecimal totalPercentsWithPersonal = bigDecimalService.subtractHalfUp(bigDecimalService.getHundred(), personal);
            BigDecimal onePercentPersonal = bigDecimalService.divideHalfUp(cryptoAmount, totalPercentsWithPersonal);
            cryptoAmount = bigDecimalService.multiplyHalfUp(onePercentPersonal, bigDecimalService.getHundred());
        }
        BigDecimal bulkDiscount = Objects.nonNull(calculateData.getBulkDiscount())
                ? calculateData.getBulkDiscount()
                : bulkDiscountService.getPercentBySum(
                convertToFiat(cryptoAmount, calculateData.getCryptoCourse(), calculateData.getUsdCourse()), fiatCurrency,
                dealAmount.getDealType(), cryptoCurrency);
        if (BigDecimal.ZERO.compareTo(bulkDiscount) != 0) {
            BigDecimal totalPercentsWithBulk = bigDecimalService.subtractHalfUp(bigDecimalService.getHundred(), bulkDiscount);
            BigDecimal onePercentBulk = bigDecimalService.divideHalfUp(cryptoAmount, totalPercentsWithBulk);
            cryptoAmount = bigDecimalService.multiplyHalfUp(onePercentBulk, bigDecimalService.getHundred());
        }

        BigDecimal usd = bigDecimalService.multiplyHalfUp(cryptoAmount, calculateData.getCryptoCourse());
        BigDecimal rub = bigDecimalService.multiplyHalfUp(usd, calculateData.getUsdCourse());
        if (rub.compareTo(calculateData.getFix()) < 0) {
            dealAmount.setCommission(calculateData.getFixCommission());
            dealAmount.setAmount(bigDecimalService.subtractHalfUp(rub, calculateData.getFixCommission()));
        } else {
            BigDecimal currentPercents = bigDecimalService.addHalfUp(BigDecimal.valueOf(100), calculateData.getCommission());
            BigDecimal onePercent = bigDecimalService.divideHalfUp(cryptoAmount, currentPercents);
            BigDecimal totalCommission = bigDecimalService.multiplyHalfUp(onePercent, calculateData.getCommission());
            BigDecimal usdTotalCommission = bigDecimalService.multiplyHalfUp(totalCommission, calculateData.getCryptoCourse());
            BigDecimal rubTotalCommission = bigDecimalService.multiplyHalfUp(usdTotalCommission, calculateData.getUsdCourse());
            dealAmount.setCommission(rubTotalCommission);
            BigDecimal totalCryptoAmount = bigDecimalService.subtractHalfUp(cryptoAmount, totalCommission);
            usd = bigDecimalService.multiplyHalfUp(totalCryptoAmount, calculateData.getCryptoCourse());
            dealAmount.setAmount(bigDecimalService.multiplyHalfUp(usd, calculateData.getUsdCourse()));
        }
    }

    public BigDecimal getCommissionForSell(BigDecimal rub, BigDecimal percentCommission) {
        return bigDecimalService.multiplyHalfUp(rub, getPercentsFactor(percentCommission));
    }

    private BigDecimal getCommission(BigDecimal amount, BigDecimal cryptoCurrency, BigDecimal percentCommission,
                                     BigDecimal course) {
        BigDecimal usd = bigDecimalService.multiplyHalfUp(amount, cryptoCurrency);
        BigDecimal rub = bigDecimalService.multiplyHalfUp(usd, course);
        return bigDecimalService.multiplyHalfUp(rub, getPercentsFactor(percentCommission));
    }

    public BigDecimal getPercentsFactor(BigDecimal sum) {
        return bigDecimalService.divideHalfUp(sum, BigDecimal.valueOf(100));
    }

    public BigDecimal calculateDiscountInFiat(DealType dealType, BigDecimal amount, BigDecimal discount) {
        return getPercentsFactor(amount).multiply(discount);
    }

    public BigDecimal calculateDiscountInCrypto(CalculateData calculateData, BigDecimal discountInFiat) {
        BigDecimal usd = bigDecimalService.divideHalfUp(discountInFiat, calculateData.getUsdCourse());
        return bigDecimalService.divideHalfUp(usd, calculateData.getCryptoCourse());
    }

    public BigDecimal convertToFiat(FiatCurrency fiatCurrency, BigDecimal cryptoAmount, BigDecimal cryptoCourse) {
        return bigDecimalService.multiplyHalfUp(bigDecimalService.multiplyHalfUp(cryptoAmount, cryptoCourse), getUSDToFiat(fiatCurrency));
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
