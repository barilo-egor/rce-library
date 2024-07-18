package tgb.btc.library.service.util;

import org.springframework.stereotype.Service;
import tgb.btc.library.constants.enums.bot.CryptoCurrency;
import tgb.btc.library.interfaces.util.IBigDecimalService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;

@Service
public class BigDecimalService implements IBigDecimalService {

    private static final BigDecimal HUNDRED = BigDecimal.valueOf(100);

    private static final int SCALE = Arrays.stream(CryptoCurrency.values())
            .max(Comparator.comparingInt(CryptoCurrency::getScale))
            .map(CryptoCurrency::getScale)
            .orElse(8);

    int getScale() {
        return SCALE;
    }

    @Override
    public BigDecimal getHundred() {
        return HUNDRED;
    }

    @Override
    public BigDecimal divideHalfUp(BigDecimal a, BigDecimal b) {
        if (b.compareTo(BigDecimal.ZERO) == 0) throw new ArithmeticException("Деление на ноль.");
        if (a.compareTo(BigDecimal.ZERO) == 0) return BigDecimal.ZERO;
        return setScaleHalfUp(a.divide(b, RoundingMode.HALF_UP));
    }

    @Override
    public BigDecimal multiplyHalfUp(BigDecimal a, BigDecimal b) {
        if (BigDecimal.ZERO.equals(a) || BigDecimal.ZERO.equals(b)) return BigDecimal.ZERO;
        return setScaleHalfUp(a.multiply(b));
    }

    @Override
    public BigDecimal addHalfUp(BigDecimal a, BigDecimal b) {
        return setScaleHalfUp(a.add(b));
    }

    @Override
    public BigDecimal subtractHalfUp(BigDecimal a, BigDecimal b) {
        return setScaleHalfUp(a.subtract(b));
    }

    private BigDecimal setScaleHalfUp(BigDecimal bigDecimal) {
        return bigDecimal.setScale(getScale(), RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal roundNullSafe(BigDecimal num, int scale) {
        if (Objects.isNull(num)) return BigDecimal.ZERO;
        return round(num, scale);
    }

    @Override
    public BigDecimal round(BigDecimal num, int scale) {
        return num.setScale(scale, RoundingMode.HALF_UP).stripTrailingZeros();
    }

    @Override
    public String roundToPlainString(BigDecimal num) {
        return roundToPlainString(num, 0);
    }

    @Override
    public String roundToPlainString(BigDecimal num, int scale) {
        return toPlainString(round(num, scale));
    }

    @Override
    public String toPlainString(BigDecimal num) {
        return num.stripTrailingZeros().toPlainString();
    }

    @Override
    public BigDecimal round(Double num, int scale) {
        return round(BigDecimal.valueOf(num), scale);
    }

    @Override
    public boolean isZero(BigDecimal number) {
        return BigDecimal.ZERO.compareTo(number) == 0;
    }
}
