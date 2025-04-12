package tgb.btc.library.constants.enums.properties;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PropertiesPath {
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
    LOGIN_PROPERTIES("config/system/login.properties", ','),
    GAMES_PROPERTIES("config/bot/games/games.properties", ','),
    SLOT_REEL_PROPERTIES("config/bot/games/slotreel/config.properties", ','),
    SLOT_REEL_MESSAGE("config/bot/games/slotreel/message.properties", ','),
    DICE_PROPERTIES("config/bot/games/dice/config.properties", ','),
    DICE_MESSAGE("config/bot/games/dice/message.properties", ','),
    RPS_PROPERTIES("config/bot/games/rps/config.properties", ','),
    RPS_MESSAGE("config/bot/games/rps/message.properties", ',');

    private final String fileName;

    private final char listDelimiter;
}
