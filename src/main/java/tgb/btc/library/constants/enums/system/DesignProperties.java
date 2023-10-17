package tgb.btc.library.constants.enums.system;

import tgb.btc.library.constants.strings.FilePaths;
import tgb.btc.library.util.system.PropertiesReader;

public enum DesignProperties implements PropertiesReader {
    BUTTONS_DESIGN(FilePaths.BUTTONS_DESIGN_PROPERTIES, ','),
    CRYPTO_CURRENCIES_DESIGN(FilePaths.CRYPTO_CURRENCIES_DESIGN_PROPERTIES, ',');

    private final String fileName;
    private final char listDelimiter;

    DesignProperties(String fileName, char listDelimiter) {
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
