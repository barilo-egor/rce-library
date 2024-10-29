package tgb.btc.library.repository.bot;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import tgb.btc.library.bean.bot.User;
import tgb.btc.library.bean.bot.UserDiscount;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class UserDiscountRepositoryTest {

    @Autowired
    private UserDiscountRepository userDiscountRepository;

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
    void getRankDiscountByUserChatId() {
        UserDiscount userDiscount = new UserDiscount();
        userDiscount.setUser(user);
        userDiscountRepository.save(userDiscount);
        assertNull(userDiscountRepository.getRankDiscountByUserChatId(user.getChatId()));
        userDiscount.setRankDiscountOn(true);
        assertTrue(userDiscountRepository.getRankDiscountByUserChatId(user.getChatId()));
    }

    @Test
    void getPersonalBuyByChatId() {
        UserDiscount userDiscount = new UserDiscount();
        userDiscount.setUser(user);
        userDiscountRepository.save(userDiscount);
        assertNull(userDiscountRepository.getPersonalBuyByChatId(user.getChatId()));
        BigDecimal variable = new BigDecimal(10);
        userDiscount.setPersonalBuy(variable);
        userDiscountRepository.save(userDiscount);
        assertEquals(0, variable.compareTo(userDiscountRepository.getPersonalBuyByChatId(user.getChatId())));
    }

    @Test
    void getPersonalSellByChatId() {
        UserDiscount userDiscount = new UserDiscount();
        userDiscount.setUser(user);
        userDiscountRepository.save(userDiscount);
        assertNull(userDiscountRepository.getPersonalSellByChatId(user.getChatId()));
        BigDecimal variable = new BigDecimal(10);
        userDiscount.setPersonalSell(variable);
        userDiscountRepository.save(userDiscount);
        assertEquals(0, variable.compareTo(userDiscountRepository.getPersonalSellByChatId(user.getChatId())));
    }

    @Test
    void countByUser_Pid() {
        userDiscountRepository.save(UserDiscount.builder().user(user).build());
        assertEquals(1, userDiscountRepository.countByUser_Pid(user.getPid()));
        assertEquals(0, userDiscountRepository.countByUser_Pid(Long.MAX_VALUE));
    }

    @Test
    void getByUserChatId() {
        assertNull(userDiscountRepository.getByUserChatId(user.getChatId()));
        UserDiscount userDiscount = userDiscountRepository.save(UserDiscount.builder().user(user).build());
        userDiscountRepository.save(UserDiscount.builder().user(
                userRepository.save(User.builder().chatId(chatId + 1).isActive(true).isBanned(false)
                        .registrationDate(LocalDateTime.now()).referralBalance(0).build())
        ).build());
        userDiscountRepository.save(UserDiscount.builder().build());
        assertEquals(userDiscount, userDiscountRepository.getByUserChatId(user.getChatId()));
    }

    @Test
    void updateIsRankDiscountOnByPid() {
        userDiscountRepository.save(UserDiscount.builder().user(user).build());
        assertNull(userDiscountRepository.getRankDiscountByUserChatId(user.getChatId()));
        userDiscountRepository.updateIsRankDiscountOnByPid(true, user.getPid());
        assertTrue(userDiscountRepository.getRankDiscountByUserChatId(user.getChatId()));
    }

    @Test
    void updatePersonalBuyByUserPid() {
        userDiscountRepository.save(UserDiscount.builder().user(user).build());
        assertNull(userDiscountRepository.getPersonalBuyByChatId(user.getChatId()));
        BigDecimal variable = new BigDecimal(30);
        userDiscountRepository.updatePersonalBuyByUserPid(variable, user.getPid());
        entityManager.clear();
        assertEquals(0, variable.compareTo(userDiscountRepository.getPersonalBuyByChatId(user.getChatId())));
    }

    @Test
    void updatePersonalSellByUserPid() {
        userDiscountRepository.save(UserDiscount.builder().user(user).build());
        assertNull(userDiscountRepository.getPersonalSellByChatId(user.getChatId()));
        BigDecimal variable = new BigDecimal(30);
        userDiscountRepository.updatePersonalSellByUserPid(variable, user.getPid());
        entityManager.clear();
        assertEquals(0, variable.compareTo(userDiscountRepository.getPersonalSellByChatId(user.getChatId())));
    }

    @Test
    void deleteByUser_ChatId() {
        Set<UserDiscount> expected = new HashSet<>();
        userDiscountRepository.save(UserDiscount.builder().user(user).build());
        expected.add(userDiscountRepository.save(UserDiscount.builder().user(
                userRepository.save(User.builder().chatId(chatId + 1).isActive(true).isBanned(false)
                        .registrationDate(LocalDateTime.now()).referralBalance(0).build())
        ).build()));
        expected.add(userDiscountRepository.save(new UserDiscount()));
        userDiscountRepository.deleteByUser_ChatId(user.getChatId());
        assertEquals(expected, new HashSet<>(userDiscountRepository.findAll()));
    }
}