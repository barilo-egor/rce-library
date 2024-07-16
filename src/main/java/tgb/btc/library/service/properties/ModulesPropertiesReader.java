package tgb.btc.library.service.properties;

import org.springframework.stereotype.Service;
import tgb.btc.library.constants.enums.properties.PropertiesPath;

@Service
public class ModulesPropertiesReader extends PropertiesReader {

    @Override
    protected PropertiesPath getPropertiesPath() {
        return PropertiesPath.MODULES_PROPERTIES;
    }

}
