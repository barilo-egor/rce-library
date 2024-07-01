package tgb.btc.library.service.bean.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tgb.btc.library.bean.bot.BotMessage;
import tgb.btc.library.constants.enums.bot.BotMessageType;
import tgb.btc.library.constants.enums.bot.MessageType;
import tgb.btc.library.constants.strings.ErrorMessage;
import tgb.btc.library.exception.BaseException;
import tgb.btc.library.interfaces.service.bean.bot.IBotMessageService;
import tgb.btc.library.repository.BaseRepository;
import tgb.btc.library.repository.bot.BotMessageRepository;
import tgb.btc.library.service.bean.BasePersistService;

import java.util.Optional;

@Service
public class BotMessageService extends BasePersistService<BotMessage> implements IBotMessageService {
    private BotMessageRepository botMessageRepository;

    @Autowired
    public void setBotMessageRepository(BotMessageRepository botMessageRepository) {
        this.botMessageRepository = botMessageRepository;
    }

    @Override
    protected BaseRepository<BotMessage> getBaseRepository() {
        return botMessageRepository;
    }

    public BotMessage findByTypeNullSafe(BotMessageType botMessageType) {
        return findByType(botMessageType)
                .orElse(BotMessage.builder().messageType(MessageType.TEXT)
                        .text(String.format(ErrorMessage.BOT_MESSAGE_NOT_SET, botMessageType.getDisplayName())).build());
    }

    public BotMessage findByTypeThrows(BotMessageType botMessageType) {
        return botMessageRepository.findByType(botMessageType)
                .orElseThrow(() -> new BaseException(String.format(ErrorMessage.BOT_MESSAGE_NOT_SET,
                        botMessageType.getDisplayName())));
    }

    @Override
    public Optional<BotMessage> findByType(BotMessageType botMessageType) {
        return botMessageRepository.findByType(botMessageType);
    }

}

