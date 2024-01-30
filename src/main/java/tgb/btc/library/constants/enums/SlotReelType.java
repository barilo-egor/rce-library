package tgb.btc.library.constants.enums;

import lombok.extern.slf4j.Slf4j;
import tgb.btc.library.constants.enums.properties.PropertiesPath;
import tgb.btc.library.interfaces.Module;

@Slf4j
public enum SlotReelType implements Module {

    NONE,
    STANDARD,
    STANDARD_ADMIN;

    private static boolean isPropertiesFilesExist() {
        boolean returnValue = true;
        if (!PropertiesPath.GAMES_PROPERTIES.isExist() || PropertiesPath.GAMES_PROPERTIES.getString("slot.reel") == null) {
            log.trace("Отсутствует файл " + PropertiesPath.GAMES_PROPERTIES.getFileName());
            returnValue = false;
        }
        if (!PropertiesPath.SLOT_REEL_PROPERTIES.isExist()) {
            log.trace("Отсутствует файл " + PropertiesPath.SLOT_REEL_PROPERTIES.getFileName());
            returnValue = false;
        }
        if (!PropertiesPath.SLOT_REEL_MESSAGE.isExist()) {
            log.trace("Отсутствует файл " + PropertiesPath.SLOT_REEL_MESSAGE.getFileName());
            returnValue = false;
        }
        return returnValue;
    }

    @Override
    public boolean isCurrent() {
        return this.equals(isPropertiesFilesExist()
                ? valueOf(PropertiesPath.GAMES_PROPERTIES.getString("slot.reel")) : NONE);
    }
}