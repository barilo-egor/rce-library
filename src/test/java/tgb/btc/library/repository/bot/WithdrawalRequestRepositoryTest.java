package tgb.btc.library.repository.bot;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import tgb.btc.library.bean.bot.User;
import tgb.btc.library.bean.bot.WithdrawalRequest;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class WithdrawalRequestRepositoryTest {

    @Autowired
    private WithdrawalRequestRepository withdrawalRequestRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager entityManager;

    private User user;

    private final Long chatId = 12345678L;

    @BeforeEach
    void setUp() {
        user = userRepository.save(User.builder().chatId(chatId).isActive(true).isBanned(false)
                .registrationDate(LocalDateTime.now()).referralBalance(0).build());
    }

    @Test
    void updateIsActiveByPid() {
        WithdrawalRequest withdrawalRequest = withdrawalRequestRepository.save(WithdrawalRequest.builder().user(user).build());
        assertNull(withdrawalRequest.getActive());
        withdrawalRequestRepository.updateIsActiveByPid(true, withdrawalRequest.getPid());
        entityManager.clear();
        WithdrawalRequest updated = withdrawalRequestRepository.getById(withdrawalRequest.getPid());
        assertTrue(updated.getActive());
    }

    @Test
    void getActiveByUserChatId() {
        User notQueryUser = userRepository.save(User.builder().chatId(chatId + 1).isActive(true).isBanned(false)
                .registrationDate(LocalDateTime.now()).referralBalance(0).build());
        int requestsCount = 10;
        int expected = 0;
        for (int i = 0; i < requestsCount; i++) {
            WithdrawalRequest withdrawalRequest = new WithdrawalRequest();
            if (i % 2 == 0) {
                withdrawalRequest.setUser(user);
            } else {
                withdrawalRequest.setUser(notQueryUser);
            }
            if (i % 3 == 0) {
                withdrawalRequest.setActive(true);
            } else if (i % 4 == 0) {
                withdrawalRequest.setActive(false);
            }
            if ((i % 3 != 0 && i % 4 == 0)) {
                expected++;
            }
            withdrawalRequestRepository.save(withdrawalRequest);
        }
        assertEquals(expected, withdrawalRequestRepository.getActiveByUserChatId(chatId));
        assertEquals(0, withdrawalRequestRepository.getActiveByUserChatId(Long.MAX_VALUE));
    }

    @Test
    void getAllActive() {
        assertEquals(new ArrayList<>(), withdrawalRequestRepository.getAllActive());
        Set<WithdrawalRequest> expected = new HashSet<>();
        for (int i = 0; i < 10; i++) {
            WithdrawalRequest withdrawalRequest = new WithdrawalRequest();
            if (i % 3 == 0) {
                withdrawalRequest.setActive(true);
            } else {
                if (i % 2 == 0) {
                    withdrawalRequest.setActive(false);
                }
            }
            withdrawalRequestRepository.save(withdrawalRequest);
            if (i % 3 == 0) {
                expected.add(withdrawalRequest);
            }
        }
        assertEquals(expected, new HashSet<>(withdrawalRequestRepository.getAllActive()));
    }

    @Test
    void getPidByUserChatId() {
        assertNull(withdrawalRequestRepository.getPidByUserChatId(Long.MAX_VALUE));
        WithdrawalRequest withdrawalRequest = withdrawalRequestRepository.save(WithdrawalRequest.builder().user(user).build());
        assertEquals(withdrawalRequest.getPid(), withdrawalRequestRepository.getPidByUserChatId(chatId));
    }

    @Test
    void deleteByUser_ChatId() {
        User notQueryUser = userRepository.save(User.builder().chatId(chatId + 1).isActive(true).isBanned(false)
                .registrationDate(LocalDateTime.now()).referralBalance(0).build());
        Set<WithdrawalRequest> expected = new HashSet<>();
        for (int i = 0; i < 10; i++) {
            WithdrawalRequest withdrawalRequest = new WithdrawalRequest();
            if (i % 2 == 0) {
                withdrawalRequest.setUser(user);
            } else {
                withdrawalRequest.setUser(notQueryUser);
            }
            withdrawalRequestRepository.save(withdrawalRequest);
            if (i % 2 != 0) {
                expected.add(withdrawalRequest);
            }
        }
        withdrawalRequestRepository.deleteByUser_ChatId(user.getChatId());
        assertEquals(expected, new HashSet<>(withdrawalRequestRepository.findAll()));
    }
}