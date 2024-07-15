package tgb.btc.library.service.properties;

import org.springframework.stereotype.Service;
import tgb.btc.library.constants.enums.properties.PropertiesPath;

@Service
public class GamesPropertiesReader extends PropertiesReader {
    @Override
    protected String getFileName() {
        return PropertiesPath.GAMES_PROPERTIES.getFileName();
    }

    @Override
    protected char getListDelimiter() {
        return PropertiesPath.GAMES_PROPERTIES.getListDelimiter();
    }
}
