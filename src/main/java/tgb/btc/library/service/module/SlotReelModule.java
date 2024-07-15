package tgb.btc.library.service.module;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tgb.btc.library.constants.enums.SlotReelType;
import tgb.btc.library.constants.enums.properties.PropertiesPath;
import tgb.btc.library.exception.BaseException;
import tgb.btc.library.interfaces.IModule;

import java.util.Objects;

@Service
@Slf4j
public class SlotReelModule implements IModule<SlotReelType> {

    private SlotReelType current;

    @Override
    public SlotReelType getCurrent() {
        if (Objects.nonNull(current))
            return current;
        String type = PropertiesPath.GAMES_PROPERTIES.getString("slot.reel");
        try {
            SlotReelType slotReelType = SlotReelType.valueOf(type);
            current = slotReelType;
            return slotReelType;
        } catch (IllegalArgumentException e) {
            String message = "В проперти slot.reel из games.properties установлено невалидное значение.";
            log.error(message);
            throw new BaseException(message);
        }
    }
}
