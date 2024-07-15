package tgb.btc.library.util;

import tgb.btc.library.constants.enums.properties.IPropertiesPath;

import java.util.Objects;

public final class SystemUtil {

    private SystemUtil() {
    }
    private static Boolean IS_DEV = null;

    public static boolean isDev() {
        if (Objects.isNull(IS_DEV)) {
            String instance = IPropertiesPath.CONFIG_PROPERTIES.getString("instance");
            IS_DEV = "dev".equals(instance);
        }
        return IS_DEV;
    }
}
