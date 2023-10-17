package tgb.btc.library.repository.bot;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import tgb.btc.library.bean.bot.BotMessage;
import tgb.btc.library.constants.enums.bot.BotMessageType;
import tgb.btc.library.repository.BaseRepository;

import java.util.Optional;

@Repository
@Transactional
public interface BotMessageRepository extends BaseRepository<BotMessage> {
    Optional<BotMessage> findByType(BotMessageType botMessageType);
}
