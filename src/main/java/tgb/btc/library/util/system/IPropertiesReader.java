package tgb.btc.library.util.system;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public interface IPropertiesReader {

    Logger logger = LoggerFactory.getLogger(IPropertiesReader.class);

    String getFileName();

    char getListDelimiter();

    default String getString(String key, String defaultValue) {
        PropertiesConfiguration instance = ReaderSupport.getInstance(this);
        return instance.getString(key, defaultValue);
    }

    default String getString(String key) {
        PropertiesConfiguration instance = ReaderSupport.getInstance(this);
        return instance.getString(key, null);
    }

    default String[] getStringArray(String key) {
        PropertiesConfiguration instance = ReaderSupport.getInstance(this);
        return StringUtils.split(StringUtils.trimToEmpty(getString(key)), this.getListDelimiter());
    }

    default List<String> getStringList(String key) {
        PropertiesConfiguration instance = ReaderSupport.getInstance(this);
        return instance.getList(key).stream().map(Object::toString).collect(Collectors.toList());
    }

    default boolean isNotBlank(String key) {
        return StringUtils.isNotBlank(getString(key));
    }

    default Integer getInteger(String key, Integer defaultValue) {
        String value = getString(key);
        if (StringUtils.isNotBlank(value) && StringUtils.isNumeric(value)) {
            return Integer.valueOf(value);
        }
        return defaultValue;
    }

    default BigDecimal getBigDecimal(String key) {
        return BigDecimal.valueOf(getDouble(key));
    }

    default Long getLong(String key, Long defaultValue) {
        String value = getString(key);
        if (StringUtils.isNotBlank(value) && StringUtils.isNumeric(value)) {
            return Long.valueOf(value);
        }
        return defaultValue;
    }

    default Double getDouble(String key, Double defaultValue) {
        String value = getString(key);
        if (StringUtils.isNotBlank(value)) {
            return Double.valueOf(value);
        }
        return defaultValue;
    }

    default Double getDouble(String key) {
        return getDouble(key, null);
    }

    default boolean isNotBlankSafely(String key) {
        try {
            return StringUtils.isNotBlank(getString(key));
        } catch (Exception e) {
            logger.error("Ошибки при вызове PropertiesEnum.isNotBlankSafely для " + getFileName(), e);
            return false;
        }
    }

    default Boolean getBoolean(String key, boolean defaultValue) {
        Boolean result = BooleanUtils.toBooleanObject(getString(key));
        return result != null ? result : defaultValue;
    }

    default Boolean getBoolean(String key) {
        return BooleanUtils.toBooleanObject(getString(key));
    }

    default List<String> getKeys() {
        List<String> keys = new ArrayList<>();
        ReaderSupport.getInstance(this).getKeys().forEachRemaining(keys::add);
        return keys;
    }

    default void setProperty(String key, Object value) {
        value = Objects.isNull(value) ? null : String.valueOf(value);
        ReaderSupport.getInstance(this).setProperty(key, value);
    }

    default void clearProperty(String key) {
        ReaderSupport.getInstance(this).clearProperty(key);
    }

    default File getFile() {
        return ReaderSupport.getInstance(this).getFile();
    }

    default void reload() {
        ReaderSupport.properties.remove(this);
        ReaderSupport.getInstance(this);
    }

    default boolean isExist() {
        return new File(this.getFileName()).exists();
    }

    class ReaderSupport {
        private static final Map<IPropertiesReader, PropertiesConfiguration> properties = new HashMap<>();

        private static PropertiesConfiguration getInstance(IPropertiesReader IPropertiesReader) {
            PropertiesConfiguration instance = properties.get(IPropertiesReader);
            if (instance == null) {
                try {
                    instance = new PropertiesConfiguration();
                    instance.setFileName(IPropertiesReader.getFileName());
                    instance.setListDelimiter(IPropertiesReader.getListDelimiter());
                    instance.setDelimiterParsingDisabled(true);
                    instance.setAutoSave(true);
                    instance.load();
                    instance.setReloadingStrategy(new FileChangedReloadingStrategy());
                    properties.put(IPropertiesReader, instance);
                } catch (ConfigurationException e) {
                    logger.error("Произошла ошибка при чтении параметров из " + IPropertiesReader.getFileName(), e);
                    throw new RuntimeException("Произошла ошибка при чтении параметров из " + IPropertiesReader.getFileName(), e);
                }
            }
            return instance;
        }
    }
}
