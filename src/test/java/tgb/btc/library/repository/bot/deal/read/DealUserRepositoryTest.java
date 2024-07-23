package tgb.btc.library.repository.bot.deal.read;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import tgb.btc.library.bean.bot.Deal;
import tgb.btc.library.bean.bot.User;
import tgb.btc.library.repository.bot.DealRepository;
import tgb.btc.library.repository.bot.UserRepository;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class DealUserRepositoryTest {

    @Autowired
    private DealRepository dealRepository;

    @Autowired
    private UserRepository userRepository;

    private final Long chatId1 = 87654321L;

    private final Long chatId2 = 12345678L;

    private final String username1 = "user1";

    private final String username2 = "user2";

    private Long dealPid1;

    private Long dealPid2;

    private Long emptyDealPid;

    @BeforeEach
    void setUp() {
        User user1 = userRepository.save(User.builder().chatId(chatId1).username(username1).isActive(true).isBanned(false)
                .registrationDate(LocalDateTime.now()).referralBalance(0).build());
        User user2 = userRepository.save(User.builder().chatId(chatId2).username(username2).isActive(true).isBanned(false)
                .registrationDate(LocalDateTime.now()).referralBalance(0).build());

        dealPid1 = dealRepository.save(Deal.builder().user(user1).build()).getPid();
        dealPid2 = dealRepository.save(Deal.builder().user(user2).build()).getPid();
        emptyDealPid = dealRepository.save(Deal.builder().build()).getPid();
    }

    @Test
    void getUserChatIdByDealPid() {
        assertAll(
                () -> assertEquals(chatId1, dealRepository.getUserChatIdByDealPid(dealPid1)),
                () -> assertEquals(chatId2, dealRepository.getUserChatIdByDealPid(dealPid2)),
                () -> assertNull(dealRepository.getUserChatIdByDealPid(emptyDealPid)),
                () -> assertNull(dealRepository.getUserChatIdByDealPid(100L))
        );
    }

    @Test
    void getUserUsernameByDealPid() {
        assertAll(
                () -> assertEquals(username1, dealRepository.getUserUsernameByDealPid(dealPid1)),
                () -> assertEquals(username2, dealRepository.getUserUsernameByDealPid(dealPid2)),
                () -> assertNull(dealRepository.getUserUsernameByDealPid(emptyDealPid)),
                () -> assertNull(dealRepository.getUserUsernameByDealPid(100L))
        );
    }
}