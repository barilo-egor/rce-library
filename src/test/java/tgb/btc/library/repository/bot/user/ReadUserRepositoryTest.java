package tgb.btc.library.repository.bot.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import tgb.btc.library.bean.bot.ReferralUser;
import tgb.btc.library.bean.bot.User;
import tgb.btc.library.constants.enums.bot.UserRole;
import tgb.btc.library.repository.bot.ReferralUserRepository;
import tgb.btc.library.repository.bot.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class ReadUserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReferralUserRepository referralUserRepository;

    private final int testTimesCount = 3;

    private final long chatId = 12345678L;

    private User getDefaultUser(Long chatId) {
        return User.builder()
                .isActive(true)
                .isBanned(false)
                .referralBalance(0)
                .registrationDate(LocalDateTime.now())
                .chatId(chatId)
                .build();
    }

    @Test
    void findByChatId() {
        for (long i = 1; i <= testTimesCount; i++) {
            User user = getDefaultUser(i);
            userRepository.save(user);
            User actual = userRepository.findByChatId(i);
            assertNotNull(actual);
            assertEquals(user.getPid(), actual.getPid());
        }
        assertNull(userRepository.findByChatId(Long.MAX_VALUE));
    }

    @Test
    void getPidByChatId() {
        for (long i = 1; i <= testTimesCount; i++) {
            User user = getDefaultUser(i);
            userRepository.save(user);
            User actual = userRepository.findByChatId(i);
            assertNotNull(actual);
            assertEquals(i, actual.getChatId());
        }
        assertNull(userRepository.findByChatId(Long.MAX_VALUE));
    }

    @Test
    void getStepByChatId() {
        for (int i = 1; i <= testTimesCount; i++) {
            User user = getDefaultUser((long) i);
            user.setStep(i);
            userRepository.save(user);
            Integer actual = userRepository.getStepByChatId((long) i);
            assertNotNull(actual);
            assertEquals(i, actual);
        }
        assertNull(userRepository.getStepByChatId(Long.MAX_VALUE));
    }

    @Test
    void getNullStepByChatId() {
        User user = getDefaultUser(chatId);
        userRepository.save(user);
        assertNull(userRepository.getStepByChatId(chatId));
    }

    @Test
    void getCommandByChatId() {
        for (int i = 1; i <= testTimesCount; i++) {
            User user = getDefaultUser((long) i);
            String expected = "command" + i;
            user.setCommand(() -> expected);
            userRepository.save(user);
            String actual = userRepository.getCommandByChatId((long) i);
            assertNotNull(actual);
            assertEquals(expected, actual);
        }
        assertNull(userRepository.getStepByChatId(Long.MAX_VALUE));
    }

    @Test
    void getNullCommandByChatId() {
        User user = getDefaultUser(chatId);
        userRepository.save(user);
        assertNull(userRepository.getCommandByChatId(chatId));
    }

    @Test
    void existsByChatId() {
        for (long i = 1; i <= testTimesCount; i++) {
            User user = getDefaultUser(i);
            userRepository.save(user);
            assertTrue(userRepository.existsByChatId(i));
            userRepository.delete(user);
            assertFalse(userRepository.existsByChatId(i));
        }
        assertFalse(userRepository.existsByChatId(Long.MAX_VALUE));
    }

    @Test
    void getUserRoleByChatId() {
        for (int i = 1; i <= UserRole.values().length; i++) {
            User user = getDefaultUser((long) i);
            UserRole expected = UserRole.values()[i - 1];
            user.setUserRole(expected);
            userRepository.save(user);
            UserRole actual = userRepository.getUserRoleByChatId((long) i);
            assertNotNull(actual);
            assertEquals(expected, actual);
        }
        assertNull(userRepository.getStepByChatId(Long.MAX_VALUE));
    }

    @Test
    void getDefaultUserRoleByChatId() {
        User user = getDefaultUser(chatId);
        userRepository.save(user);
        assertEquals(UserRole.USER, userRepository.getUserRoleByChatId(chatId));
    }

    @Test
    void getReferralBalanceByChatId() {
        for (int i = 1; i <= testTimesCount; i++) {
            User user = getDefaultUser((long) i);
            Integer expected = i * 1000;
            user.setReferralBalance(expected);
            userRepository.save(user);
            Integer actual = userRepository.getReferralBalanceByChatId((long) i);
            assertNotNull(actual);
            assertEquals(expected, actual);
        }
        assertNull(userRepository.getStepByChatId(Long.MAX_VALUE));
    }

    @Test
    void getDefaultReferralBalanceByChatId() {
        User user = getDefaultUser(chatId);
        userRepository.save(user);
        assertEquals(0, userRepository.getReferralBalanceByChatId(chatId));
    }

    @Test
    void getUserReferralsByChatId() {
        for (int i = 1; i <= testTimesCount; i++) {
            User user = getDefaultUser((long) i);
            List<ReferralUser> expected = new ArrayList<>();
            for (int j = 0; j < i; j++) {
                ReferralUser referralUser = ReferralUser.builder().build();
                expected.add(referralUserRepository.save(referralUser));
            }
            user.setReferralUsers(expected);
            userRepository.save(user);
            List<ReferralUser> actual = userRepository.getUserReferralsByChatId((long) i);
            assertEquals(expected.size(), actual.size());
            assertTrue(expected.containsAll(actual));
        }
    }

    @Test
    void getDefaultUserReferralsByChatId() {
        User user = getDefaultUser(chatId);
        userRepository.save(user);
        assertEquals(new ArrayList<>(), userRepository.getUserReferralsByChatId(chatId));
    }

    @Test
    void getByChatId() {
        for (long i = 1; i <= testTimesCount; i++) {
            User user = getDefaultUser(i);
            userRepository.save(user);
            User actual = userRepository.findByChatId(i);
            assertNotNull(actual);
            assertEquals(user, actual);
        }
        assertNull(userRepository.findByChatId(Long.MAX_VALUE));
    }

    @Test
    void getAdminsChatIds() {

    }

    @Test
    void getChatIdsByRoles() {

    }

    @Test
    void getBufferVariable() {

    }

    @Test
    void getChatIdsForMailing() {

    }

    @Test
    void getIsBannedByChatId() {

    }

    @Test
    void getChatIdsByIsBanned() {

    }

    @Test
    void getCurrentDealByChatId() {

    }

    @Test
    void getUsernameByChatId() {

    }

    @Test
    void getChargesByChatId() {

    }

    @Test
    void getReferralPercentByChatId() {

    }

    @Test
    void getPids() {

    }

    @Test
    void countByRegistrationDate() {

    }

    @Test
    void getChatIdsByRegistrationDateAndFromChatIdNotNull() {

    }

    @Test
    void getChatIdByPid() {

    }

    @Test
    void findAllForUsersReport() {

    }
}