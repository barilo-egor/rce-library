package tgb.btc.library.repository.bot.user;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import tgb.btc.library.bean.bot.User;
import tgb.btc.library.constants.enums.bot.UserRole;
import tgb.btc.library.repository.bot.UserRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class ModifyUserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager entityManager;

    private Long chatId = 12345678L;

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
        User user = userRepository.getByChatId(chatId);
        assertTrue(user.getActive());
        userRepository.updateIsActiveByChatId(true, chatId);
        entityManager.clear();
        user = userRepository.getByChatId(chatId);
        assertTrue(user.getActive());
    }

    @Test
    void updateIsBannedByChatId() {
        User user = userRepository.getByChatId(chatId);
        assertFalse(user.getBanned());
        userRepository.updateIsBannedByChatId(chatId, true);
        entityManager.clear();
        user = userRepository.getByChatId(chatId);
        assertTrue(user.getBanned());
    }

    @Test
    void updateCurrentDealByChatId() {
        User user = userRepository.getByChatId(chatId);
        assertNull(user.getCurrentDeal());
        userRepository.updateCurrentDealByChatId(1L, chatId);
        entityManager.clear();
        user = userRepository.getByChatId(chatId);
        assertEquals(1L, user.getCurrentDeal());
    }

    @Test
    void updateCommandByChatId() {
        User user = userRepository.getByChatId(chatId);
        assertNull(user.getCommand());
        userRepository.updateCommandByChatId("BUY_BITCOIN", chatId);
        entityManager.clear();
        user = userRepository.getByChatId(chatId);
        assertEquals("BUY_BITCOIN", user.getCommand());
    }

    @Test
    void updateReferralBalanceByChatId() {
        User user = userRepository.getByChatId(chatId);
        assertEquals(0, user.getReferralBalance());
        userRepository.updateCommandByChatId("BUY_BITCOIN", chatId);
        entityManager.clear();
        user = userRepository.getByChatId(chatId);
        assertEquals("BUY_BITCOIN", user.getCommand());
    }

    @Test
    void updateChargesByChatId() {
        User user = userRepository.getByChatId(chatId);
        assertNull(user.getCharges());
        userRepository.updateChargesByChatId(1000, chatId);
        entityManager.clear();
        user = userRepository.getByChatId(chatId);
        assertEquals(1000, user.getCharges());
    }

    @Test
    void updateReferralPercent() {
        User user = userRepository.getByChatId(chatId);
        assertNull(user.getReferralPercent());
        userRepository.updateReferralPercent(new BigDecimal("0.2"), chatId);
        entityManager.clear();
        user = userRepository.getByChatId(chatId);
        assertEquals(0, new BigDecimal("0.2").compareTo(user.getReferralPercent()));
    }

    @Test
    void updateStepAndCommandByChatId() {
        User user = userRepository.getByChatId(chatId);
        assertNull(user.getStep());
        assertNull(user.getCommand());
        userRepository.updateStepAndCommandByChatId(chatId, "command", 6);
        entityManager.clear();
        user = userRepository.getByChatId(chatId);
        assertEquals(6, user.getStep());
        assertEquals("command", user.getCommand());
    }

    @Test
    void updateStepByChatId() {
        User user = userRepository.getByChatId(chatId);
        assertNull(user.getStep());
        userRepository.updateStepByChatId(chatId, 3);
        entityManager.clear();
        user = userRepository.getByChatId(chatId);
        assertEquals(3, user.getStep());
    }

    @Test
    void updateUserRoleByChatId() {
        User user = userRepository.getByChatId(chatId);
        assertEquals(UserRole.USER, user.getUserRole());
        userRepository.updateUserRoleByChatId(UserRole.OPERATOR, chatId);
        entityManager.clear();
        user = userRepository.getByChatId(chatId);
        assertEquals(UserRole.OPERATOR, user.getUserRole());
    }

    @BeforeEach
    void setUp() {
        userRepository.save(User.builder()
                        .chatId(chatId)
                        .isActive(true)
                        .isBanned(false)
                        .registrationDate(LocalDateTime.now())
                        .referralBalance(0)
                        .build());
    }
}