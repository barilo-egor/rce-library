package tgb.btc.library.service.process;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tgb.btc.library.constants.enums.RPSElement;
import tgb.btc.library.constants.enums.properties.IPropertiesPath;
import tgb.btc.library.interfaces.service.bean.bot.user.IModifyUserService;
import tgb.btc.library.interfaces.service.bean.bot.user.IReadUserService;

import java.util.Objects;

@Service
public class RPSService {

    private IModifyUserService modifyUserService;

    private IReadUserService readUserService;

    @Autowired
    public void setReadUserService(IReadUserService readUserService) {
        this.readUserService = readUserService;
    }

    @Autowired
    public void setModifyUserService(IModifyUserService modifyUserService) {
        this.modifyUserService = modifyUserService;
    }

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

    public String getResultMessageText(String name, String sum, Long chatId) {
        RPSElement userElement = RPSElement.valueOf(name);
        RPSElement element = RPSElement.getRandomElement();
        Boolean result = getResult(name, userElement, element);
        StringBuilder sb = new StringBuilder();
        sb.append("Вы выбрали: ").append(userElement.getSymbol()).append(System.lineSeparator());
        sb.append("Против: ").append(element.getSymbol()).append(System.lineSeparator());
        if (Boolean.TRUE.equals(result)) {
            modifyUserService.updateReferralBalanceByChatId(readUserService.getReferralBalanceByChatId(chatId) + Integer.parseInt(sum), chatId);
            sb.append(IPropertiesPath.RPS_MESSAGE.getString("win")).append(System.lineSeparator())
                    .append(IPropertiesPath.RPS_MESSAGE.getString("win.sum")).append(" ").append(sum).append("₽");
        } else if (Objects.nonNull(result)) {
            modifyUserService.updateReferralBalanceByChatId(readUserService.getReferralBalanceByChatId(chatId) - Integer.parseInt(sum), chatId);
            sb.append(IPropertiesPath.RPS_MESSAGE.getString("lose")).append(System.lineSeparator());
        } else {
            sb.append(IPropertiesPath.RPS_MESSAGE.getString("draw"));
        }
        return sb.toString();
    }

}
