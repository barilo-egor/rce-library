package tgb.btc.library.service.module;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tgb.btc.library.constants.enums.RPSType;
import tgb.btc.library.exception.BaseException;
import tgb.btc.library.interfaces.IModule;
import tgb.btc.library.service.properties.GamesPropertiesReader;

import java.util.Objects;

@Service
@Slf4j
public class RPSModule implements IModule<RPSType> {

    private RPSType current;

    private GamesPropertiesReader gamesPropertiesReader;

    @Autowired
    public void setGamesPropertiesReader(GamesPropertiesReader gamesPropertiesReader) {
        this.gamesPropertiesReader = gamesPropertiesReader;
    }

    @Override
    public RPSType getCurrent() {
        if (Objects.nonNull(current))
            return current;
        String type = gamesPropertiesReader.getString("rock.paper.scissors", RPSType.NONE.name());
        if (StringUtils.isEmpty(type))
            throw new BaseException("Проперти rock.paper.scissors из games.properties не найдено.");
        try {
            RPSType rpsType = RPSType.valueOf(type);
            current = rpsType;
            return rpsType;
        } catch (IllegalArgumentException e) {
            String message = "В проперти rock.paper.scissors из games.properties установлено невалидное значение.";
            log.error(message);
            throw new BaseException(message, e);
        }
    }

    @Override
    public void set(RPSType rpsType) {
        gamesPropertiesReader.setProperty("rock.paper.scissors", rpsType.name());
    }
}
