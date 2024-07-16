package tgb.btc.library.constants.enums;

import tgb.btc.library.constants.enums.properties.PropertiesPath;
import tgb.btc.library.interfaces.Module;

public enum RPSType implements Module {

    NONE,
    STANDARD,
    STANDARD_ADMIN;

    @Override
    public boolean isCurrent() {
        return this.equals(valueOf(PropertiesPath.GAMES_PROPERTIES.getString("rock.paper.scissors", "NONE")));
    }

}
