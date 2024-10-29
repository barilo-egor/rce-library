package tgb.btc.library.service.module;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tgb.btc.library.constants.enums.DiceType;
import tgb.btc.library.exception.BaseException;
import tgb.btc.library.interfaces.IModule;
import tgb.btc.library.service.properties.GamesPropertiesReader;

import java.util.Objects;

@Service
@Slf4j
public class DiceModule implements IModule<DiceType> {

    private DiceType current;

    private GamesPropertiesReader gamesPropertiesReader;

    @Autowired
    public void setGamesPropertiesReader(GamesPropertiesReader gamesPropertiesReader) {
        this.gamesPropertiesReader = gamesPropertiesReader;
    }

    @Override
    public DiceType getCurrent() {
        if (Objects.nonNull(current))
            return current;
        String type = gamesPropertiesReader.getString("dice", DiceType.NONE.name());
        try {
            DiceType diceType = DiceType.valueOf(type);
            current = diceType;
            return diceType;
        } catch (IllegalArgumentException e) {
            String message = "В проперти dice из games.properties установлено невалидное значение.";
            log.error(message);
            throw new BaseException(message, e);
        }
    }
}
