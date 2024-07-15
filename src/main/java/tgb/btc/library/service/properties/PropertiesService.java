package tgb.btc.library.service.properties;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import tgb.btc.library.constants.enums.properties.IPropertiesPath;
import tgb.btc.library.vo.properties.PropertiesValue;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PropertiesService {

    private CacheManager cacheManager;

    @Autowired
    public void setCacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public void updateProperties(IPropertiesPath propertiesPath, List<PropertiesValue> values) {
        values.forEach(value -> {
            log.trace(String.format("Файл %s. Изменение свойсва: %s", propertiesPath.getFileName(), value));
            propertiesPath.setProperty(value.getKey(), value.getValue());
        });
        cacheManager.getCacheNames().forEach(name -> Objects.requireNonNull(cacheManager.getCache(name)).clear());
    }

    public List<PropertiesValue> getPropertiesValues(IPropertiesPath propertiesPath, List<String> keys) {
        return keys.stream().map(key -> {
            log.trace(String.format("Файл %s. Получение свойсва: %s", propertiesPath.getFileName(), key));
            return new PropertiesValue(key, propertiesPath.getString(key));
        }).collect(Collectors.toList());
    }

}
