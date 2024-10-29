package tgb.btc.library.repository.bot;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import tgb.btc.library.bean.bot.BotMessage;
import tgb.btc.library.constants.enums.bot.BotMessageType;
import tgb.btc.library.constants.enums.bot.MessageType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class BotMessageRepositoryTest {

    @Autowired
    private BotMessageRepository botMessageRepository;

    @Test
    void findByType() {
        Arrays.stream(BotMessageType.values())
                .forEach(botMessageType -> assertFalse(botMessageRepository.findByType(botMessageType).isPresent()));
        Map<BotMessageType, BotMessage> expected = new HashMap<>();
        for (BotMessageType botMessageType : BotMessageType.values()) {
            BotMessage botMessage = BotMessage.builder()
                    .type(botMessageType)
                    .messageType(MessageType.TEXT)
                    .build();
            expected.put(botMessageType, botMessageRepository.save(botMessage));
        }
        for (Map.Entry<BotMessageType, BotMessage> entry : expected.entrySet()) {
            Optional<BotMessage> actual = botMessageRepository.findByType(entry.getKey());
            assertTrue(actual.isPresent());
            assertEquals(entry.getValue(), actual.get());
            botMessageRepository.deleteById(actual.get().getPid());
            assertFalse(botMessageRepository.findByType(entry.getKey()).isPresent());
        }
        Arrays.stream(BotMessageType.values())
                .forEach(botMessageType -> assertFalse(botMessageRepository.findByType(botMessageType).isPresent()));
    }
}