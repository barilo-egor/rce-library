package tgb.btc.library.repository.bot.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import tgb.btc.library.bean.bot.ReferralUser;
import tgb.btc.library.bean.bot.User;
import tgb.btc.library.constants.enums.bot.UserRole;
import tgb.btc.library.exception.BaseException;
import tgb.btc.library.repository.bot.ReferralUserRepository;
import tgb.btc.library.repository.bot.UserRepository;

import java.math.BigDecimal;
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
            assertEquals(user, actual);
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
            long newChatId = (long) testTimesCountUser + i;
            userRepository.save(
                    User.builder()
                            .isActive(true)
                            .isBanned(false)
                            .referralBalance(0)
                            .userRole(UserRole.ADMIN)
                            .registrationDate(LocalDateTime.of(2000, 1, 1, 1, 3))
                            .chatId(newChatId)
                            .build()
            );
            expected.add(newChatId);
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
            for (int i = 0; i < 10; i++) {
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
            for (UserRole notCurrentRole : rolesWithoutCurrent) {
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
        assertEquals(new ArrayList<>(), userRepository.getChatIdsForMailing(List.of(UserRole.values())));
        Map<UserRole, Set<Long>> expected = new HashMap<>();
        long chatIdCounter = 1;
        for (UserRole userRole : UserRole.values()) {
            Set<Long> expectedChatIds = new HashSet<>();
            for (long i = 0; i < 15; i++) {
                User user = getDefaultUser(chatIdCounter);
                boolean isActive;
                boolean isBanned;
                if (i < 5) {
                    isActive = true;
                    isBanned = false;
                    expectedChatIds.add(chatIdCounter);
                } else if (i < 10) {
                    isActive = false;
                    isBanned = false;
                } else {
                    isActive = true;
                    isBanned = true;
                }
                user.setActive(isActive);
                user.setBanned(isBanned);
                user.setUserRole(userRole);
                userRepository.save(user);
                chatIdCounter++;
            }
            expected.put(userRole, expectedChatIds);
        }
        for (UserRole userRole : UserRole.values()) {
            assertEquals(expected.get(userRole), new HashSet<>(userRepository.getChatIdsForMailing(List.of(userRole))));
            List<UserRole> rolesWithoutCurrent = Arrays.stream(UserRole.values())
                    .filter(role -> !userRole.equals(role))
                    .collect(Collectors.toList());
            Set<Long> expectedChatIds = new HashSet<>();
            for (UserRole notCurrentRole : rolesWithoutCurrent) {
                expectedChatIds.addAll(expected.get(notCurrentRole));
            }
            assertEquals(expectedChatIds, new HashSet<>(userRepository.getChatIdsForMailing(rolesWithoutCurrent)));
        }
    }

    @Test
    void getIsBannedByChatId() {
        for (long i = 1; i <= testTimesCount; i++) {
            User user = getDefaultUser(i);
            Boolean expected = i % 2 == 0;
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
        assertEquals(new ArrayList<>(), userRepository.getChatIdsByIsBanned(true));
        assertEquals(new ArrayList<>(), userRepository.getChatIdsByIsBanned(false));
        assertEquals(new ArrayList<>(), userRepository.getChatIdsByIsBanned(null));
        Set<Long> bannedExpected = new HashSet<>();
        Set<Long> notBannedExpected = new HashSet<>();
        for (long i = 1; i <= 10; i++) {
            boolean isBanned = i % 2 == 0;
            User user = getDefaultUser(i);
            user.setBanned(isBanned);
            userRepository.save(user);
            if (isBanned) {
                bannedExpected.add(i);
            } else {
                notBannedExpected.add(i);
            }
        }
        assertAll(
                () -> assertEquals(bannedExpected, new HashSet<>(userRepository.getChatIdsByIsBanned(true))),
                () -> assertEquals(notBannedExpected, new HashSet<>(userRepository.getChatIdsByIsBanned(false))),
                () -> assertEquals(new ArrayList<>(), userRepository.getChatIdsByIsBanned(null))
        );
    }

    @Test
    void getCurrentDealByChatId() {
        for (long i = 1; i <= testTimesCount; i++) {
            User user = getDefaultUser(i);
            Long expected = i * 3;
            user.setCurrentDeal(expected);
            userRepository.save(user);
            Long actual = userRepository.getCurrentDealByChatId(i);
            assertNotNull(actual);
            assertEquals(expected, actual);
        }
        assertNull(userRepository.getCurrentDealByChatId(Long.MAX_VALUE));
    }

    @Test
    void getUsernameByChatId() {
        for (long i = 1; i <= testTimesCount; i++) {
            User user = getDefaultUser(i);
            String expected = "username" + i;
            user.setUsername(expected);
            userRepository.save(user);
            String actual = userRepository.getUsernameByChatId(i);
            assertNotNull(actual);
            assertEquals(expected, actual);
        }
        assertNull(userRepository.getUsernameByChatId(Long.MAX_VALUE));
    }

    @Test
    void getChargesByChatId() {
        for (int i = 1; i <= testTimesCount; i++) {
            User user = getDefaultUser((long) i);
            Integer expected = i * 2000;
            user.setCharges(expected);
            userRepository.save(user);
            Integer actual = userRepository.getChargesByChatId((long) i);
            assertNotNull(actual);
            assertEquals(expected, actual);
        }
        assertNull(userRepository.getChargesByChatId(Long.MAX_VALUE));
    }

    @Test
    void getReferralPercentByChatId() {
        for (int i = 1; i <= testTimesCount; i++) {
            User user = getDefaultUser((long) i);
            BigDecimal expected = new BigDecimal(2).multiply(new BigDecimal(2));
            user.setReferralPercent(expected);
            userRepository.save(user);
            BigDecimal actual = userRepository.getReferralPercentByChatId((long) i);
            assertNotNull(actual);
            assertEquals(0, actual.compareTo(expected));
        }
        assertNull(userRepository.getReferralPercentByChatId(Long.MAX_VALUE));
    }

    @Test
    void getPids() {
        assertEquals(new ArrayList<>(), userRepository.getPids());
        Set<Long> expected = new HashSet<>();
        for (long i = 1; i <= 15; i++) {
            expected.add(userRepository.save(getDefaultUser(i)).getPid());
        }
        assertEquals(expected, new HashSet<>(userRepository.getPids()));
        expected.removeIf(pid -> {
            if (pid % 3 == 0) {
                userRepository.deleteById(pid);
                return true;
            }
            return false;
        });
        assertEquals(expected, new HashSet<>(userRepository.getPids()));
    }

    @Test
    void countByRegistrationDate() {
        assertEquals(0, userRepository.countByRegistrationDate(
                LocalDateTime.now().minusYears(100),
                LocalDateTime.now().plusYears(100)),
                "Ожидается пустой список, если пользователей нет."
        );
        int testUsersCount = 10;
        LocalDateTime from = LocalDateTime.of(2000, 1, 1, 1, 1);
        LocalDateTime to = from.plusDays(testUsersCount - 1);
        for (long i = 1; i <= testUsersCount; i++) {
            User user = getDefaultUser(i);
            LocalDateTime registrationDate = from.plusDays(i - 1);
            user.setRegistrationDate(registrationDate);
            userRepository.save(user);
        }
        assertAll(
                () -> assertEquals(testUsersCount, userRepository.countByRegistrationDate(from, to),
                        "Ожидается, что все 10 пользователей попадают в диапазон дат от 'from' до 'to'."),
                () -> assertEquals(testUsersCount, userRepository.countByRegistrationDate(from.minusYears(100), to.plusYears(100)),
                        "Ожидается, что все 10 пользователей попадают в широкий диапазон дат, охватывающий крайние границы."),
                () -> assertEquals(testUsersCount - 1, userRepository.countByRegistrationDate(from, to.minusHours(1)),
                        "Ожидается, что 1 пользователь не попадет в диапазон, если конечная дата уменьшена на 1 час."),
                () -> assertEquals(testUsersCount - 2, userRepository.countByRegistrationDate(from.plusHours(1), to.minusHours(1)),
                        "Ожидается, что 2 пользователя не попадут в диапазон, если начальная дата увеличена на 1 час и конечная дата уменьшена на 1 час."),
                () -> assertEquals(testUsersCount - 1, userRepository.countByRegistrationDate(from.plusHours(1), to),
                        "Ожидается, что 1 пользователь не попадет в диапазон, если начальная дата увеличена на 1 час."),
                () -> assertEquals(0, userRepository.countByRegistrationDate(from.minusYears(200), to.minusYears(100)),
                        "Ожидается, что ни один пользователь не попадет в дальний диапазон, который не пересекается с регистрациями."),
                () -> assertEquals(1, userRepository.countByRegistrationDate(from.minusSeconds(1), from.plusSeconds(1)),
                        "Ожидается, что один пользователь попадет в диапазон, охватывающий одну секунду до и после первой даты."),
                () -> assertEquals(1, userRepository.countByRegistrationDate(to, to),
                        "Ожидается, что один пользователь попадет в диапазон, охватывающий точное значение 'to'.")
        );
    }

    @Test
    void getChatIdsByRegistrationDateAndFromChatIdNotNull() {
        assertEquals(new ArrayList<>(), userRepository.getChatIdsByRegistrationDateAndFromChatIdNotNull(
                LocalDateTime.now().minusYears(100),
                LocalDateTime.now().plusYears(100))
        );
        int testUsersCount = 10;
        LocalDateTime from = LocalDateTime.of(2000, 1, 1, 1, 1);
        LocalDateTime to = from.plusDays(testUsersCount - 1);
        Set<User> expectedUsers = new HashSet<>();
        for (long i = 1; i <= testUsersCount; i++) {
            User user = getDefaultUser(i);
            LocalDateTime registrationDate = from.plusDays(i - 1);
            user.setRegistrationDate(registrationDate);
            userRepository.save(user);
            if (i % 2 == 0) {
                long fromChatId = testUsersCount + i;
                user.setFromChatId(fromChatId);
                expectedUsers.add(user);
            }
        }
        Set<Long> allExpected = expectedUsers.stream().map(User::getChatId).collect(Collectors.toSet());
        Set<Long> withoutFromExpected = expectedUsers.stream()
                .filter(user -> !user.getRegistrationDate().isEqual(from))
                .map(User::getChatId).collect(Collectors.toSet());
        Set<Long> withoutToExpected = expectedUsers.stream()
                .filter(user -> !user.getRegistrationDate().isEqual(to))
                .map(User::getChatId).collect(Collectors.toSet());
        User userExpected = expectedUsers.stream()
                .findFirst()
                .orElseThrow(() -> new BaseException("Ожидался юзер с датой from."));
        assertAll(
                () -> assertEquals(allExpected,
                        new HashSet<>(userRepository.getChatIdsByRegistrationDateAndFromChatIdNotNull(from, to)),
                        "Ожидается, что все пользователи с fromChatId не равным null в диапазоне от 'from' до 'to' будут возвращены."
                ),
                () -> assertEquals(allExpected,
                        new HashSet<>(userRepository.getChatIdsByRegistrationDateAndFromChatIdNotNull(from.minusYears(100), to.plusYears(100))),
                        "Ожидается, что все пользователи с fromChatId не равным null в широком диапазоне будут возвращены."
                ),
                () -> assertEquals(new HashSet<>(),
                        new HashSet<>(userRepository.getChatIdsByRegistrationDateAndFromChatIdNotNull(from.minusYears(200), to.minusYears(100))),
                        "Ожидается, что ни один пользователь не будет возвращен для дальнего диапазона дат."
                ),
                () -> assertEquals(withoutFromExpected,
                        new HashSet<>(userRepository.getChatIdsByRegistrationDateAndFromChatIdNotNull(from.plusDays(1), to)),
                        "Ожидается, что пользователи, зарегистрированные на 'from', не будут включены в результаты при сдвиге начальной даты на один день."
                ),
                () -> assertEquals(withoutToExpected,
                        new HashSet<>(userRepository.getChatIdsByRegistrationDateAndFromChatIdNotNull(from, to.minusDays(1))),
                        "Ожидается, что пользователи, зарегистрированные на 'to', не будут включены в результаты при сдвиге конечной даты на один день."
                ),
                () -> assertEquals(withoutToExpected,
                        new HashSet<>(userRepository.getChatIdsByRegistrationDateAndFromChatIdNotNull(from, to.minusDays(1))),
                        "Ожидается, что пользователи, зарегистрированные на 'to', не будут включены в результаты при сдвиге конечной даты на один день (дубликат)."
                ),
                () -> assertEquals(Set.of(userExpected.getChatId()),
                        new HashSet<>(userRepository.getChatIdsByRegistrationDateAndFromChatIdNotNull(userExpected.getRegistrationDate(), userExpected.getRegistrationDate())),
                        "Ожидается, что пользователь, зарегистрированный на конкретную дату, будет возвращен."
                )
        );
    }

    @Test
    void getChatIdByPid() {
        for (int i = 1; i <= testTimesCount; i++) {
            User user = getDefaultUser((long) i);
            userRepository.save(user);
            Long actual = userRepository.getChatIdByPid(user.getPid());
            assertNotNull(actual);
            assertEquals(i, actual);
        }
        assertNull(userRepository.getChargesByChatId(Long.MAX_VALUE));
    }

    @Test
    void findAllForUsersReport() {
        assertEquals(new ArrayList<>(), userRepository.findAllForUsersReport());
        List<User> expectedUsers = new ArrayList<>();
        for (int i = 1; i < 6; i++) {
            boolean isBanned = i % 2 == 0;
            User user = getDefaultUser((long) i);
            user.setBanned(isBanned);
            if (i % 3 == 0) {
                user.setUsername("username" + i);
            }
            userRepository.save(user);
            if (!isBanned) {
                expectedUsers.add(user);
            }
        }

        List<Object[]> actual = userRepository.findAllForUsersReport();
        int i = 0;
        for (Object[] row : actual) {
            assertEquals(expectedUsers.get(i).getPid(), row[0]);
            assertEquals(expectedUsers.get(i).getChatId(), row[1]);
            assertEquals(expectedUsers.get(i).getUsername(), row[2]);
            i++;
        }
    }
}