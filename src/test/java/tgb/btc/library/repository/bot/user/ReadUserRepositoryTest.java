package tgb.btc.library.repository.bot.user;

import org.apache.commons.lang.math.RandomUtils;
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
import java.util.*;
import java.util.stream.Collectors;

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
        assertEquals(new ArrayList<>(), userRepository.getAdminsChatIds());
        userRepository.save(
                User.builder()
                        .isActive(true)
                        .isBanned(false)
                        .referralBalance(0)
                        .userRole(UserRole.OPERATOR)
                        .registrationDate(LocalDateTime.of(2000, 1, 1, 1, 3))
                        .chatId(0L)
                        .build()
        );
        int testTimesCountUser = testTimesCount * 3;
        for (int i = 1; i <= testTimesCountUser; i++) {
            userRepository.save(
                    User.builder()
                            .isActive(true)
                            .isBanned(false)
                            .referralBalance(0)
                            .userRole(UserRole.USER)
                            .registrationDate(LocalDateTime.of(2000, 1, 1, 1, 3))
                            .chatId((long) i)
                            .build()
            );
        }
        Set<Long> expected = new HashSet<>();
        for (int i = 1; i <= testTimesCount; i++) {
            long chatId = (long) testTimesCountUser + i;
            userRepository.save(
                    User.builder()
                            .isActive(true)
                            .isBanned(false)
                            .referralBalance(0)
                            .userRole(UserRole.ADMIN)
                            .registrationDate(LocalDateTime.of(2000, 1, 1, 1, 3))
                            .chatId(chatId)
                            .build()
            );
            expected.add(chatId);
        }
        assertEquals(expected, new HashSet<>(userRepository.getAdminsChatIds()));
    }

    @Test
    void getChatIdsByRoles() {
        for (UserRole userRole : UserRole.values()) {
            assertEquals(new ArrayList<>(), userRepository.getChatIdsByRoles(Set.of(userRole)));
        }
        assertEquals(new ArrayList<>(), userRepository.getChatIdsByRoles(Set.of(UserRole.values())));
        Map<UserRole, Set<Long>> expected = new HashMap<>();
        long chatIdCounter = 1;
        for (UserRole userRole : UserRole.values()) {
            Set<Long> expectedChatIds = new HashSet<>();
            int count = RandomUtils.nextInt(10);
            for (int i = 0; i < count; i++) {
                userRepository.save(
                        User.builder()
                                .isActive(true)
                                .isBanned(false)
                                .referralBalance(0)
                                .userRole(userRole)
                                .registrationDate(LocalDateTime.of(2000, 1, 1, 1, 3))
                                .chatId(chatIdCounter)
                                .build()
                );
                expectedChatIds.add(chatIdCounter);
                chatIdCounter++;
            }
            expected.put(userRole, expectedChatIds);
        }
        for (UserRole userRole : UserRole.values()) {
            assertEquals(expected.get(userRole), new HashSet<>(userRepository.getChatIdsByRoles(Set.of(userRole))));
            Set<UserRole> rolesWithoutCurrent = Arrays.stream(UserRole.values())
                    .filter(role -> !userRole.equals(role))
                    .collect(Collectors.toSet());
            Set<Long> expectedChatIds = new HashSet<>();
            for (UserRole notCurrentRole: rolesWithoutCurrent) {
                expectedChatIds.addAll(expected.get(notCurrentRole));
            }
            assertEquals(expectedChatIds, new HashSet<>(userRepository.getChatIdsByRoles(rolesWithoutCurrent)));
        }
    }

    @Test
    void getBufferVariable() {
        for (long i = 1; i <= testTimesCount; i++) {
            User user = getDefaultUser(i);
            String expected = "bufferVariable" + i;
            user.setBufferVariable(expected);
            userRepository.save(user);
            String actual = userRepository.getBufferVariable(i);
            assertNotNull(actual);
            assertEquals(expected, actual);
        }
        assertNull(userRepository.getBufferVariable(Long.MAX_VALUE));
    }

    @Test
    void getNullBufferVariable() {
        User user = getDefaultUser(Long.MAX_VALUE);
        userRepository.save(user);
        assertNull(userRepository.getBufferVariable(Long.MAX_VALUE));
    }

    @Test
    void getChatIdsForMailing() {

    }

    @Test
    void getIsBannedByChatId() {
        for (long i = 1; i <= testTimesCount; i++) {
            User user = getDefaultUser(i);
            Boolean expected = RandomUtils.nextBoolean();
            user.setBanned(expected);
            userRepository.save(user);
            Boolean actual = userRepository.getIsBannedByChatId(i);
            assertNotNull(actual);
            assertEquals(expected, actual);
        }
        assertNull(userRepository.getIsBannedByChatId(Long.MAX_VALUE));
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