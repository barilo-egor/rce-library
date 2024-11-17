package tgb.btc.library.constants.enums.properties;

import tgb.btc.library.util.system.IPropertiesReader;

public enum PropertiesPath implements IPropertiesReader {
    CONFIG_PROPERTIES("config/system/config.properties", ','),
    BOT_PROPERTIES("config/bot/bot.properties", ','),
    MESSAGE_PROPERTIES("config/message.properties", '`'),
    MESSAGE_BUFFER_PROPERTIES("config/message.properties", ','),
    VARIABLE_PROPERTIES("config/variables.properties", ','),
    VARIABLE_BUFFER_PROPERTIES("config/buffer/variables.properties", ','),
    CURRENCIES_TURNING_PROPERTIES("config/currencies_turning.properties", ','),
    BULK_DISCOUNT_PROPERTIES("config/bulk_discount.properties", ','),
    BULK_DISCOUNT_BUFFER_PROPERTIES("config/buffer/bulk_discount.properties", ','),
    ANTI_SPAM_PROPERTIES("config/antispam/antispam.properties", ','),
    MODULES_PROPERTIES("config/bot/modules.properties", ','),
    FUNCTIONS_PROPERTIES("config/bot/functions.properties", ','),
    BUTTONS_DESIGN_PROPERTIES("config/design/buttons.properties", ','),
    CRYPTO_CURRENCIES_DESIGN_PROPERTIES("config/design/crypto_currencies.properties", ','),
    SERVER_PROPERTIES("config/bot/server.properties", ','),
    REVIEW_PRISE_PROPERTIES("config/review_prise.properties", ';'),
    DESIGN_PROPERTIES("config/design/design.properties", ','),
    INFO_MESSAGE_PROPERTIES("config/message/info_message.properties", ','),
    LOGIN_PROPERTIES("config/system/login.properties", ','),
    BACKUP_DRIVE("config/system/backupdrive.properties", ','),
    GAMES_PROPERTIES("config/bot/games/games.properties", ','),
    SLOT_REEL_PROPERTIES("config/bot/games/slotreel/config.properties", ','),
    SLOT_REEL_MESSAGE("config/bot/games/slotreel/message.properties", ','),
    DICE_PROPERTIES("config/bot/games/dice/config.properties", ','),
    DICE_MESSAGE("config/bot/games/dice/message.properties", ','),
    RPS_PROPERTIES("config/bot/games/rps/config.properties", ','),
    RPS_MESSAGE("config/bot/games/rps/message.properties", ',');

    private final String fileName;

    private final char listDelimiter;

    PropertiesPath(String fileName, char listDelimiter) {
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
