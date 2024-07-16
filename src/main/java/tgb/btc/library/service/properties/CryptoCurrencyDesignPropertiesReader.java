package tgb.btc.library.service.properties;

import org.springframework.stereotype.Service;
import tgb.btc.library.constants.enums.properties.PropertiesPath;

@Service
public class CryptoCurrencyDesignPropertiesReader extends PropertiesReader {

    @Override
    protected PropertiesPath getPropertiesPath() {
        return PropertiesPath.CRYPTO_CURRENCIES_DESIGN_PROPERTIES;
    }

}
