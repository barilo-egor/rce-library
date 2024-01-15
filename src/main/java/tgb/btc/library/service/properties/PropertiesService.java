package tgb.btc.library.service.properties;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tgb.btc.library.constants.enums.properties.PropertiesPath;
import tgb.btc.library.vo.properties.PropertiesValue;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PropertiesService {

    public void updateProperties(PropertiesPath propertiesPath, List<PropertiesValue> values) {
        values.forEach(value -> {
            log.debug(String.format("Файл %s. Изменение свойсва: %s", propertiesPath.getFileName(), value));
            propertiesPath.setProperty(value.getKey(), value.getValue());
        });
    }

    public List<PropertiesValue> getPropertiesValues(PropertiesPath propertiesPath, List<String> keys) {
        return keys.stream().map(key -> {
            log.debug(String.format("Файл %s. Получение свойсва: %s", propertiesPath.getFileName(), key));
            return new PropertiesValue(key, propertiesPath.getString(key));
        }).collect(Collectors.toList());
    }

}
