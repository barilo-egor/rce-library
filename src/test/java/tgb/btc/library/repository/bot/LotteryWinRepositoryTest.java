package tgb.btc.library.repository.bot;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import tgb.btc.library.bean.bot.LotteryWin;
import tgb.btc.library.bean.bot.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ActiveProfiles("test")
class LotteryWinRepositoryTest {

    @Autowired
    private LotteryWinRepository lotteryWinRepository;

    @Autowired
    private UserRepository userRepository;

    private final long chatId = 1234568L;

    private final int wonCount = 10;

    @BeforeEach
    void setUp() {
        User user = userRepository.save(User.builder().chatId(chatId).isActive(true).isBanned(false)
                .registrationDate(LocalDateTime.now()).referralBalance(0).build());
        for (int i = 0; i < wonCount; i++) {
            lotteryWinRepository.save(LotteryWin.builder().user(user).wonDateTime(LocalDateTime.now()).build());
        }
    }

    @Test
    void getLotteryWinCount() {
        assertAll(
                () -> assertEquals(0, lotteryWinRepository.getLotteryWinCount(Long.MAX_VALUE)),
                () -> assertEquals(wonCount, lotteryWinRepository.getLotteryWinCount(chatId))
        );
    }

    @Test
    void deleteByUser_ChatId() {
        assertEquals(wonCount, lotteryWinRepository.getLotteryWinCount(chatId));
        lotteryWinRepository.deleteByUser_ChatId(chatId);
        assertEquals(0, lotteryWinRepository.getLotteryWinCount(chatId));
    }
}