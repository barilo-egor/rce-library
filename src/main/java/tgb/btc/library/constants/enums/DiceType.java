package tgb.btc.library.constants.enums;

import lombok.extern.slf4j.Slf4j;
import tgb.btc.library.constants.enums.properties.IPropertiesPath;
import tgb.btc.library.interfaces.Module;

@Slf4j
public enum DiceType implements Module {

    NONE,
    STANDARD,
    STANDARD_ADMIN;

    private static boolean isPropertiesFilesExist() {
        boolean returnValue = true;
        if (!IPropertiesPath.GAMES_PROPERTIES.isExist() || IPropertiesPath.GAMES_PROPERTIES.getString("dice") == null) {
            log.trace("Отсутствует файл " + IPropertiesPath.GAMES_PROPERTIES.getFileName());
            returnValue = false;
        }
        if (!IPropertiesPath.DICE_PROPERTIES.isExist()) {
            log.trace("Отсутствует файл " + IPropertiesPath.DICE_PROPERTIES.getFileName());
            returnValue = false;
        }
        if (!IPropertiesPath.DICE_MESSAGE.isExist()) {
            log.trace("Отсутствует файл " + IPropertiesPath.DICE_MESSAGE.getFileName());
            returnValue = false;
        }
        return returnValue;
    }

    @Override
    public boolean isCurrent() {
        return this.equals(isPropertiesFilesExist()
                ? valueOf(IPropertiesPath.GAMES_PROPERTIES.getString("dice")) : NONE);
    }
}