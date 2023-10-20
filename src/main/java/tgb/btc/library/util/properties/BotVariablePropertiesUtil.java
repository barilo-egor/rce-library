package tgb.btc.library.util.properties;

import lombok.extern.slf4j.Slf4j;
import tgb.btc.library.constants.enums.bot.CryptoCurrency;
import tgb.btc.library.constants.enums.bot.DealType;
import tgb.btc.library.constants.enums.bot.FiatCurrency;
import tgb.btc.library.constants.enums.properties.CommonProperties;
import tgb.btc.library.constants.enums.properties.VariableType;
import tgb.btc.library.exception.BaseException;

import java.math.BigDecimal;
import java.util.Objects;

@Slf4j
public class BotVariablePropertiesUtil {

    private final static String wrongFormat = "Неверный формат переменной по ключу %s";

    public static String getVariable(VariableType variableType) {
        String text;
        try {
            text = CommonProperties.VARIABLE.getString(variableType.getKey());
        } catch (Exception e) {
            throw new BaseException("Переменная по ключу " + variableType.getKey() + " не найдена.");
        }
        if (Objects.isNull(text))
            throw new BaseException("Переменная по ключу " + variableType.getKey() + " не найдена.");
        return text;
    }

    public static String getVariable(VariableType variableType, FiatCurrency fiatCurrency,
                                     DealType dealType, CryptoCurrency cryptoCurrency) {
        String text;
        String key = variableType.getKey() + "."
                + fiatCurrency.getCode() + "."
                + dealType.getKey() + "."
                + cryptoCurrency.getShortName();
        try {
            text = CommonProperties.VARIABLE.getString(variableType.getKey() + "."
                    + fiatCurrency.getCode() + "."
                    + dealType.getKey() + "."
                    + cryptoCurrency.getShortName());
        } catch (Exception e) {
            throw new BaseException("Переменная по ключу " + key + " не найдена.");
        }
        if (Objects.isNull(text))
            throw new BaseException("Переменная по ключу " + variableType.getKey() + " не найдена.");
        return text;
    }

    public static String getVariable(VariableType variableType, DealType dealType, CryptoCurrency cryptoCurrency) {
        String text;
        String key = variableType.getKey() + "."
                + dealType.getKey() + "."
                + cryptoCurrency.getShortName();
        try {
            text = CommonProperties.VARIABLE.getString(key);
        } catch (Exception e) {
            throw new BaseException("Переменная по ключу " + key + " не найдена.");
        }
        if (Objects.isNull(text))
            throw new BaseException("Переменная по ключу " + key + " не найдена.");
        return text;
    }

    public static BigDecimal getBigDecimal(VariableType variableType, FiatCurrency fiatCurrency, DealType dealType,
                                           CryptoCurrency cryptoCurrency) {
        return BigDecimal.valueOf(Double.parseDouble(getVariable(variableType, fiatCurrency, dealType, cryptoCurrency)));
    }

    public static BigDecimal getBigDecimal(VariableType variableType, DealType dealType, CryptoCurrency cryptoCurrency) {
        return BigDecimal.valueOf(getDouble(variableType, dealType, cryptoCurrency));
    }

    public static Double getDouble(VariableType variableType, DealType dealType, CryptoCurrency cryptoCurrency) {
        return Double.parseDouble(getVariable(variableType, dealType, cryptoCurrency));
    }


    public static Float getFloat(VariableType variableType) {
        try {
            return Float.parseFloat(getVariable(variableType));
        } catch (NumberFormatException e) {
            throw new BaseException(String.format(wrongFormat, variableType.getKey()));
        }
    }

    public static Double getDouble(VariableType variableType) {
        try {
            return Double.parseDouble(getVariable(variableType));
        } catch (NumberFormatException e) {
            throw new BaseException(String.format(wrongFormat, variableType.getKey()));
        }
    }

    public static Boolean getBoolean(VariableType variableType) {
        return Boolean.parseBoolean(getVariable(variableType));
    }

    public static Integer getInt(VariableType variableType) {
        try {
            return Integer.parseInt(getVariable(variableType));
        } catch (NumberFormatException e) {
            throw new BaseException(String.format(wrongFormat, variableType.getKey()));
        }
    }

    public static BigDecimal getTransactionCommission(CryptoCurrency cryptoCurrency) {
        return getBigDecimal(VariableType.TRANSACTION_COMMISSION.getKey(cryptoCurrency));
    }

    public static BigDecimal getBigDecimal(String key) {
        return CommonProperties.VARIABLE.getBigDecimal(key);
    }

    public static Double getDouble(String key) {
        return CommonProperties.VARIABLE.getDouble(key);
    }

    public static String getWallet(CryptoCurrency cryptoCurrency) {
        return CommonProperties.VARIABLE.getString(VariableType.WALLET.getKey(cryptoCurrency));
    }
}
