package tgb.btc.library.service.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BigDecimalServiceTest {

    @Spy
    private BigDecimalService bigDecimalService;

    @Test
    void hundredTest() {
        assertEquals(0, new BigDecimal(100).compareTo(bigDecimalService.getHundred()));
    }

    static Stream<Arguments> getBigDecimalsForAddHalfUp() {
        return Stream.of(
                Arguments.of(0, BigDecimal.ZERO, new BigDecimal(2), new BigDecimal(2)),
                Arguments.of(0, new BigDecimal("2.6"), new BigDecimal(2), new BigDecimal(5)),
                Arguments.of(0, new BigDecimal(5), new BigDecimal("2.7"), new BigDecimal(8)),
                Arguments.of(0, new BigDecimal("2.6"), new BigDecimal("6.2"), new BigDecimal(9)),
                Arguments.of(0, new BigDecimal(-100), new BigDecimal(59), new BigDecimal(-41)),
                Arguments.of(0, new BigDecimal(-100), new BigDecimal(-59), new BigDecimal(-159)),
                Arguments.of(3, new BigDecimal("10.2222"), new BigDecimal("2.4444"), new BigDecimal("12.667")),
                Arguments.of(0, new BigDecimal(-200), new BigDecimal("2.6"), new BigDecimal("-197")),
                Arguments.of(0, new BigDecimal(-200), new BigDecimal("2.4"), new BigDecimal("-198"))
        );
    }

    @ParameterizedTest
    @MethodSource("getBigDecimalsForAddHalfUp")
    void addHalfUp(int scale, BigDecimal a, BigDecimal b, BigDecimal expected) {
        when(bigDecimalService.getScale()).thenReturn(scale);
        assertEquals(expected, bigDecimalService.addHalfUp(a, b));
    }

    @ParameterizedTest
    @MethodSource("getBigDecimalsForDivideHalfUp")
    void divideHalfUp(int scale, BigDecimal a, BigDecimal b, BigDecimal expected) {
        when(bigDecimalService.getScale()).thenReturn(scale);
        assertEquals(expected, bigDecimalService.divideHalfUp(a, b));
    }

    static Stream<Arguments> getBigDecimalsForDivideHalfUp() {
        return Stream.of(
                Arguments.of(0, new BigDecimal(5), new BigDecimal(2), new BigDecimal("3")),
                Arguments.of(1, new BigDecimal("5.5"), new BigDecimal(2), new BigDecimal("2.8")),
                Arguments.of(0, new BigDecimal("6.8"), new BigDecimal(2), new BigDecimal("3")),
                Arguments.of(2, new BigDecimal("6.468"), new BigDecimal(2), new BigDecimal("3.23"))
        );
    }

    @Test
    void testZeroADivideHalfUp() {
        assertEquals(BigDecimal.ZERO, bigDecimalService.divideHalfUp(BigDecimal.ZERO, new BigDecimal(10)));
    }

    @Test
    void divideHalfUpByZero() {
        assertAll(
                () -> assertThrows(ArithmeticException.class, this::divideByZero)
        );
    }

    private void divideByZero() {
        bigDecimalService.divideHalfUp(new BigDecimal(1), BigDecimal.ZERO);
    }

    static Stream<Arguments> getBigDecimalsWithOneZero() {
        return Stream.of(
                Arguments.of(BigDecimal.ZERO, new BigDecimal(5)),
                Arguments.of(new BigDecimal("0.85"), BigDecimal.ZERO),
                Arguments.of(BigDecimal.ZERO, BigDecimal.ZERO)
        );
    }

    @ParameterizedTest
    @MethodSource("getBigDecimalsWithOneZero")
    void multiplyHalfUpWithZero(BigDecimal a, BigDecimal b) {
        assertEquals(0, BigDecimal.ZERO.compareTo(bigDecimalService.multiplyHalfUp(a, b)));
    }

    static Stream<Arguments> getBigDecimalsForMultiplyHalfUp() {
        return Stream.of(
                Arguments.of(0, new BigDecimal(14), new BigDecimal(2), new BigDecimal(28)),
                Arguments.of(0, new BigDecimal(20), new BigDecimal(-50), new BigDecimal(-1000))
        );
    }

    @ParameterizedTest
    @MethodSource("getBigDecimalsForMultiplyHalfUp")
    void multiplyHalfUp(int scale, BigDecimal a, BigDecimal b, BigDecimal expected) {
        when(bigDecimalService.getScale()).thenReturn(scale);
        assertEquals(expected, bigDecimalService.multiplyHalfUp(a, b));
    }

    static Stream<Arguments> getBigDecimalsForSubtractHalfUp() {
        return Stream.of(
                Arguments.of(0, new BigDecimal(14), new BigDecimal(2), new BigDecimal(12)),
                Arguments.of(0, new BigDecimal(20), new BigDecimal(-50), new BigDecimal(70))
        );
    }

    @ParameterizedTest
    @MethodSource("getBigDecimalsForSubtractHalfUp")
    void subtractHalfUp(int scale, BigDecimal a, BigDecimal b, BigDecimal expected) {
        when(bigDecimalService.getScale()).thenReturn(scale);
        assertEquals(expected, bigDecimalService.subtractHalfUp(a, b));
    }

    @ParameterizedTest
    @NullSource
    void roundNullSafeNullValue(BigDecimal val) {
        assertEquals(0, BigDecimal.ZERO.compareTo(bigDecimalService.roundNullSafe(val, 0)));
    }

    static Stream<Arguments> getBigDecimalsForRound() {
        return Stream.of(
                Arguments.of(new BigDecimal("0.123456"), 5, new BigDecimal("0.12346")),
                Arguments.of(new BigDecimal("0.123456"), 0, new BigDecimal(0)),
                Arguments.of(new BigDecimal("0.523456"), 0, new BigDecimal(1)),
                Arguments.of(new BigDecimal("65.3545354354"), 7, new BigDecimal("65.3545354")),
                Arguments.of(new BigDecimal("0"), 5, BigDecimal.ZERO)
        );
    }

    @ParameterizedTest
    @MethodSource("getBigDecimalsForRound")
    void round(BigDecimal num, int scale, BigDecimal expected) {
        assertEquals(expected, bigDecimalService.round(num, scale));
    }

    static Stream<Arguments> getBigDecimalsForRoundToPlainString() {
        return Stream.of(
                Arguments.of(new BigDecimal("0.123456000"), 5, "0.12346"),
                Arguments.of(new BigDecimal("0.1234560000000"), 0, "0"),
                Arguments.of(new BigDecimal("0.523456"), 0, "1"),
                Arguments.of(new BigDecimal("65.1234567800"), 7, "65.1234568"),
                Arguments.of(new BigDecimal("-65.12345674"), 7, "-65.1234567"),
                Arguments.of(new BigDecimal("0"), 5, "0")
        );
    }

    @ParameterizedTest
    @MethodSource("getBigDecimalsForRoundToPlainString")
    void roundToPlainString(BigDecimal num, int scale, String expected) {
        assertEquals(expected, bigDecimalService.roundToPlainString(num, scale));
    }

    static Stream<Arguments> getBigDecimalsForRoundToPlainStringOnlyNum() {
        return Stream.of(
                Arguments.of(new BigDecimal("0.123456"), "0"),
                Arguments.of(new BigDecimal("0.523456"), "1"),
                Arguments.of(new BigDecimal("65.12345678"), "65"),
                Arguments.of(new BigDecimal("-65.8"), "-66"),
                Arguments.of(new BigDecimal("0"), "0")
        );
    }

    @ParameterizedTest
    @MethodSource("getBigDecimalsForRoundToPlainStringOnlyNum")
    void roundToPlainStringOnlyNum(BigDecimal num, String expected) {
        assertEquals(expected, bigDecimalService.roundToPlainString(num));
    }

    static Stream<Arguments> getDoublesForRound() {
        return Stream.of(
                Arguments.of(0.49, 0,  BigDecimal.ZERO),
                Arguments.of(0.5, 0,  BigDecimal.ONE),
                Arguments.of(2.554, 2,  new BigDecimal("2.55")),
                Arguments.of(-10.5555, 3,  new BigDecimal("-10.556"))
        );
    }

    @ParameterizedTest
    @MethodSource("getDoublesForRound")
    void roundDouble(Double num, int scale, BigDecimal expected) {
        assertEquals(expected, bigDecimalService.round(num, scale));
    }

    static Stream<Arguments> getArgumentsForIsZero() {
        return Stream.of(
                Arguments.of(BigDecimal.ZERO, true),
                Arguments.of(new BigDecimal(0), true),
                Arguments.of(new BigDecimal("0"), true),
                Arguments.of(new BigDecimal("0.00000000"), true),
                Arguments.of(BigDecimal.ONE, false),
                Arguments.of(new BigDecimal(15), false),
                Arguments.of(new BigDecimal(-255), false)
        );
    }

    @ParameterizedTest
    @MethodSource("getArgumentsForIsZero")
    void isZero(BigDecimal num, boolean expected) {
        assertEquals(expected, bigDecimalService.isZero(num));
    }
}