package tgb.btc.library.repository.bot;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import tgb.btc.library.bean.bot.SpamBan;
import tgb.btc.library.bean.bot.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@DataJpaTest
@ActiveProfiles("test")
class SpamBanRepositoryTest {

    @Autowired
    private SpamBanRepository spamBanRepository;

    @Autowired
    private UserRepository userRepository;

    private final int spamBansCount = 10;

    private User user1;

    @BeforeEach
    void setUp() {
        user1 = userRepository.save(User.builder().chatId(12345678L).isActive(true).isBanned(false)
                .registrationDate(LocalDateTime.now()).referralBalance(0).build());
        User user2 = userRepository.save(User.builder().chatId(87654321L).isActive(true).isBanned(false)
                .registrationDate(LocalDateTime.now()).referralBalance(0).build());
        for (int i = 0; i < spamBansCount; i++) {
            SpamBan spamBan = new SpamBan();
            User userToSet;
            if (i % 2 == 0) {
                userToSet = user1;
            } else {
                userToSet = user2;
            }
            spamBan.setUser(userToSet);
            spamBanRepository.save(spamBan);
        }
    }

    @Test
    void getUserPidByPid() {
        List<SpamBan> spamBans = spamBanRepository.findAll();
        for (SpamBan spamBan : spamBans) {
            assertEquals(spamBan.getUser().getPid(), spamBanRepository.getUserPidByPid(spamBan.getPid()));
        }
        assertNull(spamBanRepository.getUserPidByPid(Long.MAX_VALUE));
    }

    @Test
    void getUserChatIdByPid() {
        List<SpamBan> spamBans = spamBanRepository.findAll();
        for (SpamBan spamBan : spamBans) {
            assertEquals(spamBan.getUser().getChatId(), spamBanRepository.getUserChatIdByPid(spamBan.getPid()));
        }
        assertNull(spamBanRepository.getUserChatIdByPid(Long.MAX_VALUE));
    }

    @Test
    void getPids() {
        List<SpamBan> spamBans = spamBanRepository.findAll();
        assertEquals(spamBans.stream().map(SpamBan::getPid).collect(Collectors.toSet()),
                new HashSet<>(spamBanRepository.getPids()));
        spamBanRepository.deleteAll();
        assertEquals(new ArrayList<>(), spamBanRepository.getPids());
    }

    @Test
    void deleteByUser_Pid() {
        List<SpamBan> spamBans = spamBanRepository.findAll();
        Set<SpamBan> expected = spamBans.stream()
                .filter(spamBan -> !spamBan.getUser().getPid().equals(user1.getPid()))
                .collect(Collectors.toSet());
        expected.add(spamBanRepository.save(new SpamBan()));
        spamBanRepository.deleteByUser_Pid(user1.getPid());
        assertEquals(expected, new HashSet<>(spamBanRepository.findAll()));
    }

    @Test
    void countByPid() {
        List<SpamBan> spamBans = spamBanRepository.findAll();
        for (SpamBan spamBan: spamBans) {
            assertEquals(1, spamBanRepository.countByPid(spamBan.getPid()));
        }
        assertEquals(0, spamBanRepository.countByPid(Long.MAX_VALUE));
    }
}