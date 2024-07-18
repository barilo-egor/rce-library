package tgb.btc.library.service.module;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tgb.btc.library.constants.enums.RPSType;
import tgb.btc.library.exception.BaseException;
import tgb.btc.library.service.properties.GamesPropertiesReader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RPSModuleTest {

    @Mock
    private GamesPropertiesReader gamesPropertiesReader;

    @InjectMocks
    private RPSModule rpsModule;

    @Test
    @DisplayName("Должен закешировать инстанс.")
    void getCurrentCache() {
        doReturn("NONE").when(gamesPropertiesReader).getString("rock.paper.scissors", RPSType.NONE.name());
        rpsModule.getCurrent();
        rpsModule.getCurrent();
        verify(gamesPropertiesReader, times(1)).getString("rock.paper.scissors", RPSType.NONE.name());
    }

    @Test
    @DisplayName("Получение NONE с инстансом и без.")
    void getCurrentNone() {
        doReturn("NONE").when(gamesPropertiesReader).getString("rock.paper.scissors", RPSType.NONE.name());
        RPSType expected = RPSType.NONE;
        assertEquals(expected, rpsModule.getCurrent());
        assertEquals(expected, rpsModule.getCurrent());
        verify(gamesPropertiesReader, times(1)).getString("rock.paper.scissors", RPSType.NONE.name());
    }

    @Test
    @DisplayName("Получение STANDARD.")
    void getCurrentStandard() {
        doReturn("STANDARD").when(gamesPropertiesReader).getString("rock.paper.scissors", RPSType.NONE.name());
        RPSType expected = RPSType.STANDARD;
        assertEquals(expected, rpsModule.getCurrent());
    }

    @Test
    @DisplayName("Получение STANDARD_ADMIN.")
    void getCurrentStandardAdmin() {
        doReturn("STANDARD_ADMIN").when(gamesPropertiesReader).getString("rock.paper.scissors", RPSType.NONE.name());
        RPSType expected = RPSType.STANDARD_ADMIN;
        assertEquals(expected, rpsModule.getCurrent());
    }

    @Test
    @DisplayName("Должен бросить исключение при невалидном значении.")
    void getCurrentNotValid() {
        doReturn("qwe").when(gamesPropertiesReader).getString("rock.paper.scissors", RPSType.NONE.name());
        BaseException baseException = assertThrows(BaseException.class, () -> rpsModule.getCurrent());
        assertEquals(baseException.getCause().getClass(), IllegalArgumentException.class);
    }
}