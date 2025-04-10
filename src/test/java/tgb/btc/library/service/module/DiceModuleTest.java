package tgb.btc.library.service.module;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tgb.btc.library.constants.enums.DiceType;
import tgb.btc.library.exception.BaseException;
import tgb.btc.library.service.properties.GamesPropertiesReader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DiceModuleTest {

    @Mock
    private GamesPropertiesReader gamesPropertiesReader;

    @InjectMocks
    private DiceModule diceModule;

    @Test
    @DisplayName("Должен закешировать инстанс.")
    void getCurrentCache() {
        doReturn("NONE").when(gamesPropertiesReader).getString("dice", DiceType.NONE.name());
        diceModule.getCurrent();
        diceModule.getCurrent();
        verify(gamesPropertiesReader, times(1)).getString("dice", DiceType.NONE.name());
    }

    @Test
    @DisplayName("Получение NONE с инстансом и без.")
    void getCurrentNone() {
        doReturn("NONE").when(gamesPropertiesReader).getString("dice", DiceType.NONE.name());
        DiceType expected = DiceType.NONE;
        assertEquals(expected, diceModule.getCurrent());
        assertEquals(expected, diceModule.getCurrent());
        verify(gamesPropertiesReader, times(1)).getString("dice", DiceType.NONE.name());
    }

    @Test
    @DisplayName("Получение STANDARD.")
    void getCurrentStandard() {
        doReturn("STANDARD").when(gamesPropertiesReader).getString("dice", DiceType.NONE.name());
        DiceType expected = DiceType.STANDARD;
        assertEquals(expected, diceModule.getCurrent());
    }

    @Test
    @DisplayName("Получение STANDARD_ADMIN.")
    void getCurrentStandardAdmin() {
        doReturn("STANDARD_ADMIN").when(gamesPropertiesReader).getString("dice", DiceType.NONE.name());
        DiceType expected = DiceType.STANDARD_ADMIN;
        assertEquals(expected, diceModule.getCurrent());
    }

    @Test
    @DisplayName("Должен бросить исключение при невалидном значении.")
    void getCurrentNotValid() {
        doReturn("qwe").when(gamesPropertiesReader).getString("dice", DiceType.NONE.name());
        BaseException baseException = assertThrows(BaseException.class, () -> diceModule.getCurrent());
        assertEquals(baseException.getCause().getClass(), IllegalArgumentException.class);
    }
}