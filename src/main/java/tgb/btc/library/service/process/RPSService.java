package tgb.btc.library.service.process;

import org.apache.commons.lang.BooleanUtils;
import org.springframework.stereotype.Service;
import tgb.btc.library.constants.enums.RPSElement;
import tgb.btc.library.constants.enums.properties.PropertiesPath;

import java.util.Objects;

@Service
public class RPSService {

    private Boolean getResult(String name, RPSElement userElement, RPSElement element) {
        switch (userElement) {
            case ROCK:
                switch (element) {
                    case ROCK:
                        return null;
                    case PAPER:
                        return false;
                    case SCISSORS:
                        return true;
                }
            case PAPER:
                switch (element) {
                    case ROCK:
                        return true;
                    case PAPER:
                        return null;
                    case SCISSORS:
                        return false;
                }
            case SCISSORS:
                switch (element) {
                    case ROCK:
                        return false;
                    case PAPER:
                        return true;
                    case SCISSORS:
                        return null;
                }
        }
        return null;
    }

    public String getResultMessage(String name) {
        RPSElement userElement = RPSElement.valueOf(name);
        RPSElement element = RPSElement.getRandomElement();
        Boolean result = getResult(name, userElement, element);
        StringBuilder sb = new StringBuilder();
        sb.append("Вы выбрали: ").append(userElement.getSymbol()).append(System.lineSeparator());
        sb.append("Против: ").append(element.getSymbol()).append(System.lineSeparator());
        if (Boolean.TRUE.equals(result)) {
            sb.append(PropertiesPath.RPS_MESSAGE.getString("win")).append(System.lineSeparator());
        } else if (Objects.nonNull(result)) {
            sb.append(PropertiesPath.RPS_MESSAGE.getString("lose")).append(System.lineSeparator());
        } else {
            sb.append(PropertiesPath.RPS_MESSAGE.getString("draw"));
        }
        return sb.toString();
    }

}
