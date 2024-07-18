package tgb.btc.library.service.util;

import org.springframework.stereotype.Service;
import tgb.btc.library.interfaces.util.IBigDecimalService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

@Service
public class BigDecimalService implements IBigDecimalService {

    private static final BigDecimal HUNDRED = BigDecimal.valueOf(100);

    private static final int SCALE = 20;

    private int getScale() {
        return SCALE;
    }

    @Override
    public BigDecimal getHundred() {
        return HUNDRED;
    }

    @Override
    public BigDecimal divideHalfUp(BigDecimal a, BigDecimal b) {
        return a.setScale(getScale(), RoundingMode.HALF_UP).divide(b, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal multiplyHalfUp(BigDecimal a, BigDecimal b) {
        return a.setScale(getScale(), RoundingMode.HALF_UP).multiply(b);
    }

    @Override
    public BigDecimal addHalfUp(BigDecimal a, BigDecimal b) {
        return a.setScale(getScale(), RoundingMode.HALF_UP).add(b);
    }

    @Override
    public BigDecimal subtractHalfUp(BigDecimal a, BigDecimal b) {
        return a.setScale(getScale(), RoundingMode.HALF_UP).subtract(b);
    }

    @Override
    public BigDecimal roundNullSafe(BigDecimal num, int scale) {
        if (Objects.isNull(num)) return BigDecimal.valueOf(0);
        return num.setScale(scale, RoundingMode.HALF_UP).stripTrailingZeros();
    }

    @Override
    public BigDecimal round(BigDecimal num, int scale) {
        return num.setScale(scale, RoundingMode.HALF_UP).stripTrailingZeros();
    }

    @Override
    public String roundToPlainString(BigDecimal num, int scale) {
        return toPlainString(round(num, scale));
    }

    @Override
    public String roundToPlainString(BigDecimal num) {
        return roundToPlainString(num, 0);
    }

    @Override
    public String toPlainString(BigDecimal num) {
        return num.stripTrailingZeros().toPlainString();
    }

    @Override
    public BigDecimal round(Double num, int scale) {
        return BigDecimal.valueOf(num).setScale(scale, RoundingMode.HALF_UP).stripTrailingZeros();
    }

    @Override
    public boolean isZero(BigDecimal number) {
        return BigDecimal.ZERO.compareTo(number) == 0;
    }
}
