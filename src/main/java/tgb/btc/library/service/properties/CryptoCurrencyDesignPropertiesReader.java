package tgb.btc.library.service.properties;

import org.springframework.stereotype.Service;
import tgb.btc.library.constants.enums.properties.PropertiesPath;

@Service
public class CryptoCurrencyDesignPropertiesReader extends PropertiesReader {

    @Override
    protected String getFileName() {
        return PropertiesPath.CRYPTO_CURRENCIES_DESIGN_PROPERTIES.getFileName();
    }

    @Override
    protected char getListDelimiter() {
        return PropertiesPath.CRYPTO_CURRENCIES_DESIGN_PROPERTIES.getListDelimiter();
    }

}
