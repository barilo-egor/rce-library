package tgb.btc.library.interfaces.service.properties;

import tgb.btc.library.constants.enums.properties.PropertiesPath;
import tgb.btc.library.vo.properties.PropertiesValue;

import java.util.List;

public interface IPropertiesService {

    void updateProperties(PropertiesPath propertiesPath, List<PropertiesValue> values);

    List<PropertiesValue> getPropertiesValues(PropertiesPath propertiesPath, List<String> keys);

}
