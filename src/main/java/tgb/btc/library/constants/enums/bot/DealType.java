package tgb.btc.library.constants.enums.bot;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import tgb.btc.library.exception.EnumTypeNotFoundException;
import tgb.btc.library.interfaces.ObjectNodeConvertable;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Function;

public enum DealType implements ObjectNodeConvertable<DealType> {
    BUY("покупка", "покупку", "покупки", "buy"),
    SELL("продажа", "продажу", "продажи", "sell");

    /**
     * Именительный
     */
    final String nominative;
    /**
     * Родительный
     */
    final String genitive;
    /**
     * Винительный
     */
    final String accusative;


    final String key;

    DealType(String nominative, String genitive, String accusative, String key) {
        this.nominative = nominative;
        this.genitive = genitive;
        this.accusative = accusative;
        this.key = key;
    }

    public String getNominative() {
        return nominative;
    }

    public String getGenitive() {
        return genitive;
    }

    public String getNominativeFirstLetterToUpper() {
        String firstLetter = nominative.substring(0, 1).toUpperCase();
        return firstLetter + nominative.substring(1);
    }

    public String getAccusative() {
        return accusative;
    }

    public String getKey() {
        return key;
    }

    public static boolean isBuy(DealType dealType) {
        return DealType.BUY.equals(dealType);
    }

    public static DealType findByKey(String key) {
        return Arrays.stream(DealType.values())
                .filter(dealType -> dealType.getKey().equals(key))
                .findFirst()
                .orElseThrow(EnumTypeNotFoundException::new);
    }

    @Override
    public Function<DealType, ObjectNode> mapFunction() {
        return dealType -> new ObjectMapper().createObjectNode()
                .put("name", dealType.name())
                .put("nominative", dealType.getNominative())
                .put("nominativeFirstUpper", dealType.getNominativeFirstLetterToUpper());
    }

    public static DealType valueOfNullable(String name) {
        if (Objects.isNull(name)) return null;
        DealType dealType;
        try {
            dealType = DealType.valueOf(name);
        } catch (IllegalArgumentException e) {
            dealType = null;
        }
        return dealType;
    }
}
