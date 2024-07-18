package tgb.btc.library.service.properties;

import org.springframework.stereotype.Service;
import tgb.btc.library.constants.enums.properties.PropertiesPath;

@Service
public class CurrenciesTurningPropertiesReader extends PropertiesReader {

    @Override
    protected PropertiesPath getPropertiesPath() {
        return PropertiesPath.CURRENCIES_TURNING_PROPERTIES;
    }
}
