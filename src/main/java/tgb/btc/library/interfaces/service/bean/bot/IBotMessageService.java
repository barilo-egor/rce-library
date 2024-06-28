package tgb.btc.library.interfaces.service.bean.bot;

import tgb.btc.library.bean.bot.BotMessage;
import tgb.btc.library.constants.enums.bot.BotMessageType;
import tgb.btc.library.interfaces.service.IBasePersistService;

import java.util.Optional;

public interface IBotMessageService extends IBasePersistService<BotMessage> {
    Optional<BotMessage> findByType(BotMessageType botMessageType);
}
