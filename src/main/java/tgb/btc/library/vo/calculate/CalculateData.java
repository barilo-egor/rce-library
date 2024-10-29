package tgb.btc.library.vo.calculate;

import lombok.Data;
import tgb.btc.library.constants.enums.bot.CryptoCurrency;
import tgb.btc.library.constants.enums.bot.DealType;
import tgb.btc.library.constants.enums.bot.FiatCurrency;
import tgb.btc.library.constants.enums.properties.VariableType;
import tgb.btc.library.service.properties.VariablePropertiesReader;

import java.math.BigDecimal;

@Data
public class CalculateData {
    private final BigDecimal fix;

    private final BigDecimal usdCourse;

    private final BigDecimal commission;

    private final BigDecimal fixCommission;

    private final BigDecimal transactionalCommission;

    private final BigDecimal cryptoCourse;

    private BigDecimal personalDiscount;

    private BigDecimal bulkDiscount;

    private boolean isApplyBulk = true;

    public CalculateData(FiatCurrency fiatCurrency, DealType dealType, CryptoCurrency cryptoCurrency, BigDecimal cryptoCourse,
                         VariablePropertiesReader variablePropertiesReader) {
        this.fix = variablePropertiesReader.getBigDecimal(VariableType.FIX, fiatCurrency, dealType, cryptoCurrency);
        this.usdCourse = variablePropertiesReader.getBigDecimal(VariableType.USD_COURSE, fiatCurrency, dealType, cryptoCurrency);
        this.commission = variablePropertiesReader.getBigDecimal(VariableType.COMMISSION, fiatCurrency, dealType, cryptoCurrency);
        this.fixCommission = variablePropertiesReader.getBigDecimal(VariableType.FIX_COMMISSION, fiatCurrency, dealType, cryptoCurrency);
        this.transactionalCommission = variablePropertiesReader.getTransactionCommission(cryptoCurrency);
        this.cryptoCourse = cryptoCourse;
    }

    public CalculateData(FiatCurrency fiatCurrency, DealType dealType, CryptoCurrency cryptoCurrency, BigDecimal cryptoCourse,
                         BigDecimal usdCourse, BigDecimal personalDiscount, BigDecimal bulkDiscount, VariablePropertiesReader variablePropertiesReader) {
        this.fix = variablePropertiesReader.getBigDecimal(VariableType.FIX, fiatCurrency, dealType, cryptoCurrency);
        this.usdCourse = usdCourse;
        this.commission = variablePropertiesReader.getBigDecimal(VariableType.COMMISSION, fiatCurrency, dealType, cryptoCurrency);
        this.fixCommission = variablePropertiesReader.getBigDecimal(VariableType.FIX_COMMISSION, fiatCurrency, dealType, cryptoCurrency);
        this.transactionalCommission = variablePropertiesReader.getTransactionCommission(cryptoCurrency);
        this.cryptoCourse = cryptoCourse;
        this.personalDiscount = personalDiscount;
        this.bulkDiscount = bulkDiscount;
    }

    public CalculateData(FiatCurrency fiatCurrency, DealType dealType, CryptoCurrency cryptoCurrency, BigDecimal cryptoCourse,
                         BigDecimal usdCourse, BigDecimal personalDiscount, BigDecimal bulkDiscount, VariablePropertiesReader variablePropertiesReader,
                         boolean isApplyBulk) {
        this.fix = variablePropertiesReader.getBigDecimal(VariableType.FIX, fiatCurrency, dealType, cryptoCurrency);
        this.usdCourse = usdCourse;
        this.commission = variablePropertiesReader.getBigDecimal(VariableType.COMMISSION, fiatCurrency, dealType, cryptoCurrency);
        this.fixCommission = variablePropertiesReader.getBigDecimal(VariableType.FIX_COMMISSION, fiatCurrency, dealType, cryptoCurrency);
        this.transactionalCommission = variablePropertiesReader.getTransactionCommission(cryptoCurrency);
        this.cryptoCourse = cryptoCourse;
        this.personalDiscount = personalDiscount;
        this.bulkDiscount = bulkDiscount;
        this.isApplyBulk = isApplyBulk;
    }

    public BigDecimal getFix() {
        return fix;
    }

    public BigDecimal getUsdCourse() {
        return usdCourse;
    }

    public BigDecimal getCommission() {
        return commission;
    }

    public BigDecimal getFixCommission() {
        return fixCommission;
    }

    public BigDecimal getTransactionalCommission() {
        return transactionalCommission;
    }

    public BigDecimal getCryptoCourse() {
        return cryptoCourse;
    }

    public BigDecimal getPersonalDiscount() {
        return personalDiscount;
    }

    public BigDecimal getBulkDiscount() {
        return bulkDiscount;
    }

    public boolean isApplyBulk() {
        return isApplyBulk;
    }
}
