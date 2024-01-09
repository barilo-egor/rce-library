package tgb.btc.library.constants.enums;

import tgb.btc.library.constants.strings.FilePaths;
import tgb.btc.library.util.system.PropertiesReader;

public enum CommonDesignProperties implements PropertiesReader {
    DESIGN(FilePaths.DESIGN_PROPERTIES, ',');

    private final String fileName;
    private final char listDelimiter;

    CommonDesignProperties(String fileName, char listDelimiter) {
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
