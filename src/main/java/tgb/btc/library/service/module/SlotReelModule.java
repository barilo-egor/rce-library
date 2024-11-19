package tgb.btc.library.service.module;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tgb.btc.library.constants.enums.SlotReelType;
import tgb.btc.library.exception.BaseException;
import tgb.btc.library.interfaces.IModule;
import tgb.btc.library.service.properties.GamesPropertiesReader;

import java.util.Objects;

@Service
@Slf4j
public class SlotReelModule implements IModule<SlotReelType> {

    private SlotReelType current;

    private GamesPropertiesReader gamesPropertiesReader;

    @Autowired
    public void setGamesPropertiesReader(GamesPropertiesReader gamesPropertiesReader) {
        this.gamesPropertiesReader = gamesPropertiesReader;
    }

    @Override
    public SlotReelType getCurrent() {
        if (Objects.nonNull(current))
            return current;
        String type = gamesPropertiesReader.getString("slot.reel", SlotReelType.NONE.name());
        try {
            SlotReelType slotReelType = SlotReelType.valueOf(type);
            current = slotReelType;
            return slotReelType;
        } catch (IllegalArgumentException e) {
            String message = "В проперти slot.reel из games.properties установлено невалидное значение.";
            log.error(message);
            throw new BaseException(message, e);
        }
    }

    @Override
    public void set(SlotReelType slotReelType) {
        gamesPropertiesReader.setProperty("slot.reel", slotReelType.name());
    }
}
