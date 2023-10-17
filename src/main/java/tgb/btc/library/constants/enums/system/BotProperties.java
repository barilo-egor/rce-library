package tgb.btc.library.constants.enums.system;

import tgb.btc.library.constants.strings.FilePaths;
import tgb.btc.library.util.system.PropertiesReader;

import java.util.Objects;

public enum BotProperties implements PropertiesReader {

    ANTI_SPAM(FilePaths.ANTI_SPAM_PROPERTIES, ','),
    BOT_CONFIG(FilePaths.BOT_PROPERTIES, ','),
    BOT_VARIABLE(FilePaths.BOT_VARIABLE_PROPERTIES, ','),
    BOT_VARIABLE_BUFFER(FilePaths.BOT_VARIABLE_BUFFER_PROPERTIES, ','),
    BULK_DISCOUNT(FilePaths.BULK_DISCOUNT_PROPERTIES, ','),
    BULK_DISCOUNT_BUFFER(FilePaths.BULK_DISCOUNT_BUFFER_PROPERTIES, ','),
    MESSAGE(FilePaths.MESSAGE_PROPERTIES, ','),
    MESSAGE_BUFFER(FilePaths.MESSAGE_BUFFER_PROPERTIES, ','),
    TURNING_CURRENCIES(FilePaths.CURRENCIES_TURNING_PROPERTIES, ','),
    MODULES(FilePaths.MODULES_PROPERTIES, ','),

    FUNCTIONS(FilePaths.FUNCTIONS_PROPERTIES, ','),
    BUTTONS_DESIGN(FilePaths.BUTTONS_DESIGN_PROPERTIES, ','),
    CRYPTO_CURRENCIES_DESIGN(FilePaths.CRYPTO_CURRENCIES_DESIGN_PROPERTIES, ','),
    SERVER(FilePaths.SERVER_PROPERTIES, ','),
    REVIEW_PRISE(FilePaths.REVIEW_PRISE_PROPERTIES, ';'),
    INFO_MESSAGE(FilePaths.INFO_MESSAGE_PROPERTIES, ','),

    LOGIN(FilePaths.LOGIN_PROPERTIES, ',')
    ;

    private final String fileName;
    private final char listDelimiter;

    BotProperties(String fileName, char listDelimiter) {
        this.fileName = fileName;
        this.listDelimiter = listDelimiter;
    }

    @Override
    public String getFileName() {
        return fileName;
    }

    @Override
    public char getListDelimiter() {
        return listDelimiter;
    }

}
