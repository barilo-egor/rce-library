package tgb.btc.library.interfaces.service.bot;

import tgb.btc.library.bean.bot.BotMessage;
import tgb.btc.library.constants.enums.bot.BotMessageType;

import java.util.Optional;

public interface IBotMessageService {
    Optional<BotMessage> findByType(BotMessageType botMessageType);
}
