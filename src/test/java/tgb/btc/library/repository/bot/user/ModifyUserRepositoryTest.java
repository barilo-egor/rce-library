package tgb.btc.library.repository.bot.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import tgb.btc.library.bean.bot.User;
import tgb.btc.library.repository.bot.UserRepository;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@DataJpaTest
@ActiveProfiles("test")
class ModifyUserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager entityManager;

    private Long chatId = 12345678L;

    @BeforeEach
    void setUp() {
        userRepository.save(User.builder()
                        .chatId(chatId)
                        .isActive(true)
                        .isBanned(false)
                        .registrationDate(LocalDateTime.now())
                        .referralBalance(0)
                        .build())
                .getPid();
    }

    @Test
    void setDefaultValues() {
        User user = userRepository.getByChatId(chatId);
        assertNull(user.getStep());
        assertNull(user.getCommand());
        userRepository.setDefaultValues(chatId);
        entityManager.clear();
        user = userRepository.getByChatId(chatId);
        assertEquals(User.DEFAULT_STEP, user.getStep());
        assertEquals("START", user.getCommand());
    }

    @Test
    void nextStep() {
        User user = userRepository.getByChatId(chatId);
        assertNull(user.getStep());
        userRepository.setDefaultValues(chatId);
        userRepository.nextStep(chatId);
        entityManager.clear();
        user = userRepository.getByChatId(chatId);
        assertEquals(User.DEFAULT_STEP + 1, user.getStep());
    }

    @Test
    void nextStepWithCommand() {
        User user = userRepository.getByChatId(chatId);
        assertNull(user.getStep());
        userRepository.setDefaultValues(chatId);
        userRepository.nextStep(chatId, "testcommand");
        entityManager.clear();
        user = userRepository.getByChatId(chatId);
        assertEquals(User.DEFAULT_STEP + 1, user.getStep());
        assertEquals("testcommand", user.getCommand());
    }

    @Test
    void previousStep() {
        User user = userRepository.getByChatId(chatId);
        assertNull(user.getStep());
        userRepository.setDefaultValues(chatId);
        userRepository.nextStep(chatId, "testcommand");
        userRepository.nextStep(chatId, "testcommand");
        userRepository.nextStep(chatId, "testcommand");
        userRepository.previousStep(chatId);
        entityManager.clear();
        user = userRepository.getByChatId(chatId);
        assertEquals(User.DEFAULT_STEP + 2, user.getStep());
        assertEquals("testcommand", user.getCommand());
    }

    @Test
    void updateBufferVariable() {
        User user = userRepository.getByChatId(chatId);
        assertNull(user.getBufferVariable());
        userRepository.updateBufferVariable(chatId, "variable");
        entityManager.clear();
        user = userRepository.getByChatId(chatId);
        assertEquals("variable", user.getBufferVariable());
    }

    @Test
    void updateIsActiveByChatId() {
//        User user = userRepository.getByChatId(chatId);
//        assertNull(user.getActive());
//        userRepository.updateIsActiveByChatId(true, chatId);
//        entityManager.clear();
//        user = userRepository.getByChatId(chatId);
//        assertEquals("variable", user.getBufferVariable());
    }

    @Test
    void updateIsBannedByChatId() {
    }

    @Test
    void updateCurrentDealByChatId() {
    }

    @Test
    void updateCommandByChatId() {
    }

    @Test
    void updateReferralBalanceByChatId() {
    }

    @Test
    void updateChargesByChatId() {
    }

    @Test
    void updateReferralPercent() {
    }

    @Test
    void updateStepAndCommandByChatId() {
    }

    @Test
    void updateStepByChatId() {
    }

    @Test
    void updateUserRoleByChatId() {
    }
}