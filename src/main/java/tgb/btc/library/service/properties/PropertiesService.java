package tgb.btc.library.service.properties;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import tgb.btc.library.constants.enums.properties.PropertiesPath;
import tgb.btc.library.exception.BaseException;
import tgb.btc.library.interfaces.service.properties.IPropertiesService;
import tgb.btc.library.vo.properties.PropertiesValue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PropertiesService implements IPropertiesService {

    private CacheManager cacheManager;

    private final Map<PropertiesPath, PropertiesReader> propertiesReadersMap = new HashMap<>();

    public PropertiesService(List<PropertiesReader> propertiesReaders) {
        for (PropertiesReader propertiesReader: propertiesReaders) {
            propertiesReadersMap.put(propertiesReader.getPropertiesPath(), propertiesReader);
        }
    }

    @Autowired
    public void setCacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Override
    public void updateProperties(PropertiesPath propertiesPath, List<PropertiesValue> values) {
        PropertiesReader reader = propertiesReadersMap.get(propertiesPath);
        if (Objects.isNull(reader)) {
            throw new BaseException("Не найден reader для пропертей " + propertiesPath.name());
        }
        values.forEach(value -> {
            log.trace(String.format("Файл %s. Изменение свойсва: %s", propertiesPath.getFileName(), value));
            reader.setProperty(value.getKey(), value.getValue());
        });
        cacheManager.getCacheNames().forEach(name -> Objects.requireNonNull(cacheManager.getCache(name)).clear());
    }

    @Override
    public List<PropertiesValue> getPropertiesValues(PropertiesPath propertiesPath, List<String> keys) {
        PropertiesReader reader = propertiesReadersMap.get(propertiesPath);
        if (Objects.isNull(reader)) {
            throw new BaseException("Не найден reader для пропертей " + propertiesPath.name());
        }
        return keys.stream().map(key -> {
            log.trace(String.format("Файл %s. Получение свойсва: %s", propertiesPath.getFileName(), key));
            return new PropertiesValue(key, reader.getString(key));
        }).collect(Collectors.toList());
    }

}
