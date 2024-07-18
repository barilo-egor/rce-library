package tgb.btc.library.service.properties;

import org.springframework.stereotype.Service;
import tgb.btc.library.constants.enums.properties.PropertiesPath;

@Service
public class MessageBufferPropertiesReader extends PropertiesReader {

    @Override
    protected PropertiesPath getPropertiesPath() {
        return PropertiesPath.MESSAGE_BUFFER_PROPERTIES;
    }
}
