package tgb.btc.library.service.properties;

import org.springframework.stereotype.Service;
import tgb.btc.library.constants.enums.properties.PropertiesPath;

@Service
public class ButtonsDesignPropertiesReader extends PropertiesReader {

    @Override
    protected PropertiesPath getPropertiesPath() {
        return PropertiesPath.BUTTONS_DESIGN_PROPERTIES;
    }

}
