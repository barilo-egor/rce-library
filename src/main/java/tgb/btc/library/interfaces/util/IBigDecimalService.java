package tgb.btc.library.interfaces.util;

import java.math.BigDecimal;

public interface IBigDecimalService {
    BigDecimal getHundred();

    BigDecimal divideHalfUp(BigDecimal a, BigDecimal b);

    BigDecimal multiplyHalfUp(BigDecimal a, BigDecimal b);

    BigDecimal addHalfUp(BigDecimal a, BigDecimal b);

    BigDecimal subtractHalfUp(BigDecimal a, BigDecimal b);

    BigDecimal roundNullSafe(BigDecimal num, int scale);

    BigDecimal round(BigDecimal num, int scale);

    String roundToPlainString(BigDecimal num, int scale);

    String roundToPlainString(BigDecimal num);

    String toPlainString(BigDecimal num);

    BigDecimal round(Double num, int scale);

    boolean isZero(BigDecimal number);
}
