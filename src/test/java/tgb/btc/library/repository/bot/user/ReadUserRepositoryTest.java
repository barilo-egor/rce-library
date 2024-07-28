package tgb.btc.library.repository.bot.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import tgb.btc.library.bean.bot.ReferralUser;
import tgb.btc.library.bean.bot.User;
import tgb.btc.library.constants.enums.bot.UserRole;
import tgb.btc.library.repository.bot.ReferralUserRepository;
import tgb.btc.library.repository.bot.UserRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class ReadUserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReferralUserRepository referralUserRepository;

    private final Long chatId1 = 87654321L;

    private final Long chatId2 = 12345678L;

    private final Long chatId3 = 23456789L;

    private User user1;

    private User user2;

    private final List<ReferralUser> referralUsers1 = new ArrayList<>();

    private final List<ReferralUser> referralUsers2 = new ArrayList<>();

    private User emptyUser;

    @BeforeEach
    void setUp(TestInfo testInfo) {
        referralUsers1.add(referralUserRepository.save(ReferralUser.builder().chatId(120L).build()));
        referralUsers1.add(referralUserRepository.save(ReferralUser.builder().chatId(121L).build()));
        referralUsers1.add(referralUserRepository.save(ReferralUser.builder().chatId(122L).build()));
        referralUsers2.add(referralUserRepository.save(ReferralUser.builder().chatId(123L).build()));
        user1 = userRepository.save(User.builder()
                .isActive(true)
                .isBanned(false)
                .registrationDate(LocalDateTime.of(2000, 1, 1, 1, 1))
                .chatId(chatId1)
                .step(0)
                .command("command1")
                .userRole(UserRole.OPERATOR)
                .referralUsers(referralUsers1)
                .referralBalance(1000)
                .bufferVariable("bufferVariable1")
                .username("username1")
                .currentDeal(1L)
                .charges(10)
                .referralPercent(new BigDecimal(1))
                .fromChatId(300L)
                .build());
        user2 = userRepository.save(User.builder()
                .isActive(false)
                .isBanned(true)
                .registrationDate(LocalDateTime.of(2000, 1, 1, 1, 2))
                .chatId(chatId2)
                .step(1)
                .command("command2")
                .userRole(UserRole.ADMIN)
                .referralUsers(referralUsers2)
                .referralBalance(2000)
                .bufferVariable("bufferVariable2")
                .username("username2")
                .currentDeal(2L)
                .charges(20)
                .fromChatId(301L)
                .referralPercent(new BigDecimal(2))
                .build());
        emptyUser = userRepository.save(User.builder()
                .isActive(true)
                .isBanned(false)
                .referralBalance(0)
                .registrationDate(LocalDateTime.of(2000, 1, 1, 1, 3))
                .chatId(chatId3).build());
    }

    @Test
    void findByChatId() {
        assertAll(
                () -> assertEquals(user1.getPid(), userRepository.findByChatId(chatId1).getPid()),
                () -> assertEquals(user2.getPid(), userRepository.findByChatId(chatId2).getPid()),
                () -> assertNull(userRepository.findByChatId(Long.MAX_VALUE))
        );
    }

    @Test
    void getPidByChatId() {
        assertAll(
                () -> assertEquals(user1.getPid(), userRepository.getPidByChatId(chatId1)),
                () -> assertEquals(user2.getPid(), userRepository.getPidByChatId(chatId2)),
                () -> assertNull(userRepository.getPidByChatId(Long.MAX_VALUE))
        );
    }

    @Test
    void getStepByChatId() {
        assertAll(
                () -> assertEquals(0, userRepository.getStepByChatId(chatId1)),
                () -> assertEquals(1, userRepository.getStepByChatId(chatId2)),
                () -> assertNull(userRepository.getStepByChatId(chatId3)),
                () -> assertNull(userRepository.getStepByChatId(Long.MAX_VALUE))
        );
    }

    @Test
    void getCommandByChatId() {
        assertAll(
                () -> assertEquals("command1", userRepository.getCommandByChatId(chatId1)),
                () -> assertEquals("command2", userRepository.getCommandByChatId(chatId2)),
                () -> assertNull(userRepository.getCommandByChatId(chatId3)),
                () -> assertNull(userRepository.getCommandByChatId(Long.MAX_VALUE))
        );
    }

    @Test
    void existsByChatId() {
        assertAll(
                () -> assertTrue(userRepository.existsByChatId(chatId1)),
                () -> assertTrue(userRepository.existsByChatId(chatId2)),
                () -> assertFalse(userRepository.existsByChatId(Long.MAX_VALUE))
        );
    }

    @Test
    void getUserRoleByChatId() {
        assertAll(
                () -> assertEquals(UserRole.OPERATOR, userRepository.getUserRoleByChatId(chatId1)),
                () -> assertEquals(UserRole.ADMIN, userRepository.getUserRoleByChatId(chatId2)),
                () -> assertEquals(UserRole.USER, userRepository.getUserRoleByChatId(chatId3)),
                () -> assertNull(userRepository.getUserRoleByChatId(Long.MAX_VALUE))
        );
    }

    @Test
    void getReferralBalanceByChatId() {
        assertAll(
                () -> assertEquals(1000, userRepository.getReferralBalanceByChatId(chatId1)),
                () -> assertEquals(2000, userRepository.getReferralBalanceByChatId(chatId2)),
                () -> assertEquals(0, userRepository.getReferralBalanceByChatId(chatId3)),
                () -> assertNull(userRepository.getReferralBalanceByChatId(Long.MAX_VALUE))
        );
    }

    @Test
    void getUserReferralsByChatId() {
        assertAll(
                () -> assertEquals(referralUsers1, userRepository.getUserReferralsByChatId(chatId1)),
                () -> assertEquals(referralUsers2, userRepository.getUserReferralsByChatId(chatId2)),
                () -> assertEquals(new ArrayList<>(), userRepository.getUserReferralsByChatId(chatId3)),
                () -> assertEquals(new ArrayList<>(), userRepository.getUserReferralsByChatId(Long.MAX_VALUE))
        );
    }

    @Test
    void getByChatId() {
        assertAll(
                () -> assertEquals(user1.getPid(), userRepository.getByChatId(chatId1).getPid()),
                () -> assertEquals(user2.getPid(), userRepository.getByChatId(chatId2).getPid()),
                () -> assertEquals(emptyUser.getPid(), userRepository.getByChatId(chatId3).getPid()),
                () -> assertNull(userRepository.getByChatId(Long.MAX_VALUE))
        );
    }

    @Test
    void getAdminsChatIds() {
        Set<Long> expected = new HashSet<>();
        expected.add(user2.getChatId());
        expected.add(userRepository.save(User.builder().isActive(true).isBanned(false).referralBalance(0)
                .registrationDate(LocalDateTime.now()).userRole(UserRole.ADMIN).chatId(1L).build()).getChatId());
        expected.add(userRepository.save(User.builder().isActive(true).isBanned(false).referralBalance(0)
                .registrationDate(LocalDateTime.now()).userRole(UserRole.ADMIN).chatId(2L).build()).getChatId());
        List<Long> actual = userRepository.getAdminsChatIds();
        assertAll(
                () -> assertEquals(expected.size(), actual.size()),
                () -> assertEquals(expected, new HashSet<>(actual))
        );
    }

    @Test
    void getChatIdsByRoles() {
        Set<Long> expectedUsers = new HashSet<>();
        expectedUsers.add(chatId3);
        expectedUsers.add(userRepository.save(User.builder()
                .isActive(true)
                .isBanned(false)
                .referralBalance(0)
                .registrationDate(LocalDateTime.now())
                .chatId(1L)
                .build()).getChatId());
        expectedUsers.add(userRepository.save(User.builder()
                .isActive(true)
                .isBanned(false)
                .referralBalance(0)
                .registrationDate(LocalDateTime.now())
                .chatId(2L)
                .build()).getChatId());
        Set<Long> expectedAdmins = new HashSet<>();
        expectedAdmins.add(chatId2);
        userRepository.deleteById(user1.getPid());
        Set<Long> expectedAdminsAndUsers = new HashSet<>();
        expectedAdminsAndUsers.addAll(expectedUsers);
        expectedAdminsAndUsers.addAll(expectedAdmins);
        assertAll(
                () -> assertEquals(expectedUsers, new HashSet<>(userRepository.getChatIdsByRoles(Set.of(UserRole.USER)))),
                () -> assertEquals(expectedAdmins, new HashSet<>(userRepository.getChatIdsByRoles(Set.of(UserRole.ADMIN)))),
                () -> assertEquals(0, userRepository.getChatIdsByRoles(Set.of(UserRole.OPERATOR)).size()),
                () -> assertEquals(expectedAdminsAndUsers, new HashSet<>(userRepository.getChatIdsByRoles(Set.of(UserRole.ADMIN, UserRole.USER))))
        );
        userRepository.deleteAll();
        assertEquals(new ArrayList<>(), userRepository.getChatIdsByRoles(Set.of(UserRole.values())));
    }

    @Test
    void getBufferVariable() {
        assertAll(
                () -> assertEquals("bufferVariable1", userRepository.getBufferVariable(chatId1)),
                () -> assertEquals("bufferVariable2", userRepository.getBufferVariable(chatId2)),
                () -> assertNull(userRepository.getBufferVariable(chatId3)),
                () -> assertNull(userRepository.getBufferVariable(Long.MAX_VALUE))
        );
    }

    @Test
    void getChatIdsForMailing() {
        Set<Long> expected = new HashSet<>();
        expected.add(chatId3);
        expected.add(userRepository.save(User.builder()
                .chatId(1L)
                .isActive(true)
                .isBanned(false)
                .referralBalance(0)
                .userRole(UserRole.USER)
                .registrationDate(LocalDateTime.now())
                .build()).getChatId());
        userRepository.save(User.builder()
                .chatId(2L)
                .isActive(false)
                .isBanned(false)
                .referralBalance(0)
                .registrationDate(LocalDateTime.now())
                .build());
        userRepository.save(User.builder()
                .chatId(3L)
                .isActive(true)
                .isBanned(true)
                .referralBalance(0)
                .registrationDate(LocalDateTime.now())
                .build());
        assertAll(
                () -> assertEquals(expected, new HashSet<>(userRepository.getChatIdsForMailing(List.of(UserRole.USER))))
        );
    }

    @Test
    void getIsBannedByChatId() {
        assertAll(
                () -> assertFalse(userRepository.getIsBannedByChatId(chatId1)),
                () -> assertTrue(userRepository.getIsBannedByChatId(chatId2)),
                () -> assertNull(userRepository.getBufferVariable(Long.MAX_VALUE))
        );
    }

    @Test
    void getChatIdsByIsBanned() {
        assertAll(
                () -> assertEquals(Set.of(chatId2), new HashSet<>(userRepository.getChatIdsByIsBanned(true))),
                () -> assertEquals(Set.of(chatId1, chatId3), new HashSet<>(userRepository.getChatIdsByIsBanned(false))),
                () -> assertEquals(new ArrayList<>(), userRepository.getChatIdsByIsBanned(null))
        );
        userRepository.deleteAll();
        assertAll(
                () -> assertEquals(new ArrayList<>(), userRepository.getChatIdsByIsBanned(true)),
                () -> assertEquals(new ArrayList<>(), userRepository.getChatIdsByIsBanned(false)),
                () -> assertEquals(new ArrayList<>(), userRepository.getChatIdsByIsBanned(null))
        );
    }

    @Test
    void getCurrentDealByChatId() {
        assertAll(
                () -> assertEquals(1L, userRepository.getCurrentDealByChatId(chatId1)),
                () -> assertEquals(2L, userRepository.getCurrentDealByChatId(chatId2)),
                () -> assertNull(userRepository.getCurrentDealByChatId(chatId3)),
                () -> assertNull(userRepository.getCurrentDealByChatId(Long.MAX_VALUE))
        );
    }

    @Test
    void getUsernameByChatId() {
        assertAll(
                () -> assertEquals("username1", userRepository.getUsernameByChatId(chatId1)),
                () -> assertEquals("username2", userRepository.getUsernameByChatId(chatId2)),
                () -> assertNull(userRepository.getUsernameByChatId(chatId3)),
                () -> assertNull(userRepository.getUsernameByChatId(Long.MAX_VALUE))
        );
    }

    @Test
    void getChargesByChatId() {
        assertAll(
                () -> assertEquals(10, userRepository.getChargesByChatId(chatId1)),
                () -> assertEquals(20, userRepository.getChargesByChatId(chatId2)),
                () -> assertNull(userRepository.getChargesByChatId(chatId3)),
                () -> assertNull(userRepository.getChargesByChatId(Long.MAX_VALUE))
        );
    }

    @Test
    void getReferralPercentByChatId() {
        assertAll(
                () -> assertEquals(0, new BigDecimal(1).compareTo(userRepository.getReferralPercentByChatId(chatId1))),
                () -> assertEquals(0, new BigDecimal(2).compareTo(userRepository.getReferralPercentByChatId(chatId2))),
                () -> assertNull(userRepository.getReferralPercentByChatId(chatId3)),
                () -> assertNull(userRepository.getReferralPercentByChatId(Long.MAX_VALUE))
        );
    }

    @Test
    void getPids() {
        Set<Long> expectedPids = new HashSet<>();
        expectedPids.add(user1.getPid());
        expectedPids.add(user2.getPid());
        expectedPids.add(emptyUser.getPid());
        assertEquals(expectedPids, new HashSet<>(userRepository.getPids()));
        userRepository.deleteAll();
        assertEquals(new ArrayList<>(), userRepository.getPids());
    }

    @Test
    void countByRegistrationDate() {
        assertAll(
                () -> assertEquals(3, userRepository.countByRegistrationDate(
                        LocalDateTime.of(1990, 1, 1, 1, 1),
                        LocalDateTime.of(2000, 1, 1, 1, 3))),
                () -> assertEquals(1, userRepository.countByRegistrationDate(
                        LocalDateTime.of(2000, 1, 1, 1, 3),
                        LocalDateTime.of(2010, 1, 1, 1, 2))),
                () -> assertEquals(1, userRepository.countByRegistrationDate(
                        LocalDateTime.of(2000, 1, 1, 1, 2),
                        LocalDateTime.of(2000, 1, 1, 1, 2))),
                () -> assertEquals(0, userRepository.countByRegistrationDate(
                        LocalDateTime.of(2010, 1, 1, 1, 1),
                        LocalDateTime.of(2011, 1, 1, 1, 2)))
        );
        userRepository.deleteAll();
        assertEquals(0, userRepository.countByRegistrationDate(
                LocalDateTime.of(1990, 1, 1, 1, 1),
                LocalDateTime.of(2011, 1, 1, 1, 2)
        ));
    }

    @Test
    void getChatIdsByRegistrationDateAndFromChatIdNotNull() {
        assertAll(
                () -> assertEquals(Set.of(chatId1, chatId2), new HashSet<>(userRepository.getChatIdsByRegistrationDateAndFromChatIdNotNull(
                        LocalDateTime.of(2000, 1, 1, 1, 1),
                        LocalDateTime.of(2010, 1, 1, 1, 3)))),
                () -> assertEquals(Set.of(chatId1), new HashSet<>(userRepository.getChatIdsByRegistrationDateAndFromChatIdNotNull(
                        LocalDateTime.of(1990, 1, 1, 1, 1),
                        LocalDateTime.of(2000, 1, 1, 1, 1)))),
                () -> assertEquals(Set.of(chatId2), new HashSet<>(userRepository.getChatIdsByRegistrationDateAndFromChatIdNotNull(
                        LocalDateTime.of(2000, 1, 1, 1, 2),
                        LocalDateTime.of(2000, 1, 1, 1, 2)))),
                () -> assertEquals(new ArrayList<>(), userRepository.getChatIdsByRegistrationDateAndFromChatIdNotNull(
                        LocalDateTime.of(2000, 1, 1, 1, 3),
                        LocalDateTime.of(2010, 1, 1, 1, 1)))
        );
        userRepository.deleteAll();
        assertEquals(new ArrayList<>(), userRepository.getChatIdsByRegistrationDateAndFromChatIdNotNull(
                LocalDateTime.of(1990, 1, 1, 1, 1),
                LocalDateTime.of(2011, 1, 1, 1, 2)
        ));
    }

    @Test
    void getChatIdByPid() {
        assertAll(
                () -> assertEquals(chatId1, userRepository.getChatIdByPid(user1.getPid())),
                () -> assertEquals(chatId2, userRepository.getChatIdByPid(user2.getPid())),
                () -> assertNull(userRepository.getChatIdByPid(Long.MAX_VALUE))
        );
    }

    @Test
    void findAllForUsersReport() {
        List<Object[]> actual = userRepository.findAllForUsersReport();
        assertAll(
                () -> assertEquals(2, actual.size()),
                () -> assertEquals(3, actual.get(0).length),
                () -> assertEquals(user1.getPid(), actual.get(0)[0]),
                () -> assertEquals(user1.getChatId(), actual.get(0)[1]),
                () -> assertEquals(user1.getUsername(), actual.get(0)[2]),
                () -> assertEquals(3, actual.get(1).length),
                () -> assertEquals(emptyUser.getPid(), actual.get(1)[0]),
                () -> assertEquals(emptyUser.getChatId(), actual.get(1)[1]),
                () -> assertEquals(emptyUser.getUsername(), actual.get(1)[2])
        );
        userRepository.deleteAll();
        assertEquals(new ArrayList<>(), userRepository.findAllForUsersReport());
    }
}