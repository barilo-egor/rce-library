package tgb.btc.library.service.properties;

import org.springframework.stereotype.Service;
import tgb.btc.library.constants.enums.bot.CryptoCurrency;
import tgb.btc.library.constants.enums.bot.DealType;
import tgb.btc.library.constants.enums.bot.FiatCurrency;
import tgb.btc.library.constants.enums.properties.PropertiesPath;
import tgb.btc.library.constants.enums.properties.VariableType;
import tgb.btc.library.exception.BaseException;

import java.math.BigDecimal;
import java.util.Objects;

@Service
public class VariablePropertiesReader extends PropertiesReader {

    private static final String WRONG_FORMAT = "Неверный формат переменной по ключу %s";

    private static final String VARIABLE_NOT_FOUND = "Переменная по ключу %s не найдена.";

    @Override
    protected PropertiesPath getPropertiesPath() {
        return PropertiesPath.VARIABLE_PROPERTIES;
    }

    public String getVariable(VariableType variableType) {
        String text;
        try {
            text = getString(variableType.getKey());
        } catch (Exception e) {
            throw new BaseException(String.format(VARIABLE_NOT_FOUND, variableType.getKey()));
        }
        if (Objects.isNull(text))
            throw new BaseException(String.format(VARIABLE_NOT_FOUND, variableType.getKey()));
        return text;
    }

    public String getVariable(VariableType variableType, FiatCurrency fiatCurrency,
                                     DealType dealType, CryptoCurrency cryptoCurrency) {
        String text;
        String key = variableType.getKey() + "."
                + fiatCurrency.getCode() + "."
                + dealType.getKey() + "."
                + cryptoCurrency.getShortName();
        try {
            text = getString(variableType.getKey() + "."
                    + fiatCurrency.getCode() + "."
                    + dealType.getKey() + "."
                    + cryptoCurrency.getShortName());
        } catch (Exception e) {
            throw new BaseException(String.format(VARIABLE_NOT_FOUND, key));
        }
        if (Objects.isNull(text))
            throw new BaseException(String.format(VARIABLE_NOT_FOUND, variableType.getKey()));
        return text;
    }

    public Integer getInt(VariableType variableType, FiatCurrency fiatCurrency, Integer defaultValue) {
        try {
            return Integer.parseInt(getVariable(variableType, fiatCurrency, String.valueOf(defaultValue)));
        } catch (NumberFormatException e) {
            throw new BaseException("Ошибка парсинга " + variableType.getKey() + "."
                    + fiatCurrency.getCode() + " к целому числу.");
        }
    }

    public String getVariable(VariableType variableType, FiatCurrency fiatCurrency, String defaultValue) {
        String text;
        String key = variableType.getKey() + "." + fiatCurrency.getCode();
        try {
            text = getString(key);
        } catch (Exception e) {
            throw new BaseException("Ошибки при чтении параметра " + key, e);
        }
        if (Objects.isNull(text) || text.isBlank())
            return defaultValue;
        return text;
    }

    public String getVariable(VariableType variableType, DealType dealType, CryptoCurrency cryptoCurrency) {
        String text;
        String key = variableType.getKey() + "."
                + dealType.getKey() + "."
                + cryptoCurrency.getShortName();
        try {
            text = getString(key);
        } catch (Exception e) {
            throw new BaseException(String.format(VARIABLE_NOT_FOUND, key));
        }
        if (Objects.isNull(text))
            throw new BaseException(String.format(VARIABLE_NOT_FOUND, key));
        return text;
    }

    public BigDecimal getBigDecimal(VariableType variableType, FiatCurrency fiatCurrency, DealType dealType,
                                           CryptoCurrency cryptoCurrency) {
        return BigDecimal.valueOf(Double.parseDouble(getVariable(variableType, fiatCurrency, dealType, cryptoCurrency)));
    }

    public BigDecimal getBigDecimal(VariableType variableType, DealType dealType, CryptoCurrency cryptoCurrency) {
        return BigDecimal.valueOf(getDouble(variableType, dealType, cryptoCurrency));
    }

    public Double getDouble(VariableType variableType, DealType dealType, CryptoCurrency cryptoCurrency) {
        return Double.parseDouble(getVariable(variableType, dealType, cryptoCurrency));
    }


    public Float getFloat(VariableType variableType) {
        try {
            return Float.parseFloat(getVariable(variableType));
        } catch (NumberFormatException e) {
            throw new BaseException(String.format(WRONG_FORMAT, variableType.getKey()));
        }
    }

    public Double getDouble(VariableType variableType) {
        try {
            return Double.parseDouble(getVariable(variableType));
        } catch (NumberFormatException e) {
            throw new BaseException(String.format(WRONG_FORMAT, variableType.getKey()));
        }
    }

    public Boolean getBoolean(VariableType variableType) {
        return Boolean.parseBoolean(getVariable(variableType));
    }

    public Integer getInt(VariableType variableType, FiatCurrency fiatCurrency, DealType dealType,
                                 CryptoCurrency cryptoCurrency) {
        return Integer.parseInt(getVariable(variableType, fiatCurrency, dealType, cryptoCurrency));
    }

    public Integer getInt(VariableType variableType) {
        try {
            return Integer.parseInt(getVariable(variableType));
        } catch (NumberFormatException e) {
            throw new BaseException(String.format(WRONG_FORMAT, variableType.getKey()));
        }
    }

    public BigDecimal getTransactionCommission(CryptoCurrency cryptoCurrency) {
        return getBigDecimal(VariableType.TRANSACTION_COMMISSION.getKey(cryptoCurrency));
    }

    public String getWallet(CryptoCurrency cryptoCurrency) {
        return getString(VariableType.WALLET.getKey(cryptoCurrency));
    }

    public void reload() {
        super.load();
    }
}
