package tgb.btc.library.service.module;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tgb.btc.library.constants.enums.SlotReelType;
import tgb.btc.library.exception.BaseException;
import tgb.btc.library.service.properties.GamesPropertiesReader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SlotReelModuleTest {

    @Mock
    private GamesPropertiesReader gamesPropertiesReader;

    @InjectMocks
    private SlotReelModule slotReelTypeModule;

    @Test
    @DisplayName("Должен закешировать инстанс.")
    void getCurrentCache() {
        doReturn("NONE").when(gamesPropertiesReader).getString("slot.reel");
        slotReelTypeModule.getCurrent();
        slotReelTypeModule.getCurrent();
        verify(gamesPropertiesReader, times(1)).getString("slot.reel");
        verify(gamesPropertiesReader, times(1)).getString(anyString());
    }

    @Test
    @DisplayName("Получение NONE с инстансом и без.")
    void getCurrentNone() {
        doReturn("NONE").when(gamesPropertiesReader).getString("slot.reel");
        SlotReelType expected = SlotReelType.NONE;
        assertEquals(expected, slotReelTypeModule.getCurrent());
        assertEquals(expected, slotReelTypeModule.getCurrent());
        verify(gamesPropertiesReader, times(1)).getString("slot.reel");
        verify(gamesPropertiesReader, times(1)).getString(anyString());
    }

    @Test
    @DisplayName("Получение STANDARD.")
    void getCurrentStandard() {
        doReturn("STANDARD").when(gamesPropertiesReader).getString("slot.reel");
        SlotReelType expected = SlotReelType.STANDARD;
        assertEquals(expected, slotReelTypeModule.getCurrent());
    }

    @Test
    @DisplayName("Получение STANDARD_ADMIN.")
    void getCurrentStandardAdmin() {
        doReturn("STANDARD_ADMIN").when(gamesPropertiesReader).getString("slot.reel");
        SlotReelType expected = SlotReelType.STANDARD_ADMIN;
        assertEquals(expected, slotReelTypeModule.getCurrent());
    }

    @Test
    @DisplayName("Должен бросить исключение при невалидном значении.")
    void getCurrentNotValid() {
        doReturn("qwe").when(gamesPropertiesReader).getString("slot.reel");
        BaseException baseException = assertThrows(BaseException.class, () -> slotReelTypeModule.getCurrent());
        assertEquals(baseException.getCause().getClass(), IllegalArgumentException.class);
    }

    @ParameterizedTest
    @DisplayName("Должен бросить исключение при не найденном значении.")
    @NullSource
    @EmptySource
    void getCurrentNoValue(String value) {
        doReturn(value).when(gamesPropertiesReader).getString("slot.reel");
        assertThrows(BaseException.class, () -> slotReelTypeModule.getCurrent());
    }
}