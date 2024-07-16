package tgb.btc.library.service.properties;

import org.springframework.stereotype.Service;
import tgb.btc.library.constants.enums.properties.PropertiesPath;

@Service
public class ModulesPropertiesReader extends PropertiesReader {

    @Override
    protected String getFileName() {
        return PropertiesPath.MODULES_PROPERTIES.getFileName();
    }

    @Override
    protected char getListDelimiter() {
        return PropertiesPath.MODULES_PROPERTIES.getListDelimiter();
    }

}
