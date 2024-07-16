package tgb.btc.library.service.properties;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import tgb.btc.library.constants.enums.properties.PropertiesPath;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
public abstract class PropertiesReader {
    
    private PropertiesConfiguration instance;

    public String getString(String key, String defaultValue) {
        PropertiesConfiguration instance = getInstance();
        return instance.getString(key, defaultValue);
    }

    public String getString(String key) {
        PropertiesConfiguration instance = getInstance();
        return instance.getString(key, null);
    }

    public String[] getStringArray(String key) {
        return StringUtils.split(StringUtils.trimToEmpty(getString(key)), getPropertiesPath().getListDelimiter());
    }

    public List<String> getStringList(String key) {
        PropertiesConfiguration instance = getInstance();
        return instance.getList(key).stream().map(Object::toString).collect(Collectors.toList());
    }

    public boolean isNotBlank(String key) {
        return StringUtils.isNotBlank(getString(key));
    }

    public Integer getInteger(String key, Integer defaultValue) {
        String value = getString(key);
        if (StringUtils.isNotBlank(value) && StringUtils.isNumeric(value)) {
            return Integer.valueOf(value);
        }
        return defaultValue;
    }

    public BigDecimal getBigDecimal(String key) {
        return BigDecimal.valueOf(getDouble(key));
    }

    public Long getLong(String key, Long defaultValue) {
        String value = getString(key);
        if (StringUtils.isNotBlank(value) && StringUtils.isNumeric(value)) {
            return Long.valueOf(value);
        }
        return defaultValue;
    }

    public Double getDouble(String key, Double defaultValue) {
        String value = getString(key);
        if (StringUtils.isNotBlank(value)) {
            return Double.valueOf(value);
        }
        return defaultValue;
    }

    public Double getDouble(String key) {
        return getDouble(key, null);
    }

    public boolean isNotBlankSafely(String key) {
        try {
            return StringUtils.isNotBlank(getString(key));
        } catch (Exception e) {
            log.error("Ошибки при вызове PropertiesEnum.isNotBlankSafely для " + getPropertiesPath().getFileName(), e);
            return false;
        }
    }

    public Boolean getBoolean(String key, boolean defaultValue) {
        Boolean result = BooleanUtils.toBooleanObject(getString(key));
        return result != null ? result : defaultValue;
    }

    public Boolean getBoolean(String key) {
        return BooleanUtils.toBooleanObject(getString(key));
    }

    public List<String> getKeys() {
        List<String> keys = new ArrayList<>();
        getInstance().getKeys().forEachRemaining(keys::add);
        return keys;
    }

    public void setProperty(String key, Object value) {
        value = Objects.isNull(value) ? null : String.valueOf(value);
        getInstance().setProperty(key, value);
    }

    public void clearProperty(String key) {
        getInstance().clearProperty(key);
    }

    public File getFile() {
        return getInstance().getFile();
    }

    public boolean isExist() {
        return new File(getPropertiesPath().getFileName()).exists();
    }

    private PropertiesConfiguration getInstance() {
        if (instance == null) {
            load();
        }
        return instance;
    }

    protected void load() {
        PropertiesPath propertiesPath = getPropertiesPath();
        try {
            instance = new PropertiesConfiguration();
            instance.setFileName(propertiesPath.getFileName());
            instance.setListDelimiter(propertiesPath.getListDelimiter());
            instance.setDelimiterParsingDisabled(true);
            instance.setAutoSave(true);
            instance.load();
            instance.setReloadingStrategy(new FileChangedReloadingStrategy());
        } catch (ConfigurationException e) {
            log.error("Произошла ошибка при чтении параметров из " + propertiesPath.getFileName(), e);
            throw new RuntimeException("Произошла ошибка при чтении параметров из " + propertiesPath.getFileName(), e);
        }
    }

    protected abstract PropertiesPath getPropertiesPath();
}
