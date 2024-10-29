package tgb.btc.library.repository.bot;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import tgb.btc.library.bean.bot.User;
import tgb.btc.library.bean.bot.UserData;
import tgb.btc.library.constants.enums.bot.CryptoCurrency;
import tgb.btc.library.constants.enums.bot.DealType;
import tgb.btc.library.constants.enums.bot.FiatCurrency;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@DataJpaTest
@ActiveProfiles("test")
class UserDataRepositoryTest {

    @Autowired
    private UserDataRepository userDataRepository;

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
    void getLongByUserPid() {
        Long variable = 10L;
        UserData userData = userDataRepository.save(UserData.builder().user(user).build());
        assertNull(userDataRepository.getLongByUserPid(user.getPid()));
        userData.setLongVariable(variable);
        userDataRepository.save(userData);
        assertEquals(variable, userDataRepository.getLongByUserPid(user.getPid()));
        assertNull(userDataRepository.getLongByUserPid(Long.MAX_VALUE));
    }

    @Test
    void getStringByUserChatId() {
        String variable = "str";
        UserData userData = userDataRepository.save(UserData.builder().user(user).build());
        assertNull(userDataRepository.getStringByUserChatId(user.getChatId()));
        userData.setStringVariable(variable);
        userDataRepository.save(userData);
        assertEquals(variable, userDataRepository.getStringByUserChatId(user.getChatId()));
        assertNull(userDataRepository.getStringByUserChatId(Long.MAX_VALUE));
    }

    @Test
    void getDealTypeByChatId() {
        DealType variable = DealType.BUY;
        UserData userData = userDataRepository.save(UserData.builder().user(user).build());
        assertNull(userDataRepository.getDealTypeByChatId(user.getChatId()));
        userData.setDealTypeVariable(variable);
        userDataRepository.save(userData);
        assertEquals(variable, userDataRepository.getDealTypeByChatId(user.getChatId()));
        assertNull(userDataRepository.getDealTypeByChatId(Long.MAX_VALUE));
    }

    @Test
    void getFiatCurrencyByChatId() {
        FiatCurrency variable = FiatCurrency.RUB;
        UserData userData = userDataRepository.save(UserData.builder().user(user).build());
        assertNull(userDataRepository.getFiatCurrencyByChatId(user.getChatId()));
        userData.setFiatCurrency(variable);
        userDataRepository.save(userData);
        assertEquals(variable, userDataRepository.getFiatCurrencyByChatId(user.getChatId()));
        assertNull(userDataRepository.getFiatCurrencyByChatId(Long.MAX_VALUE));
    }

    @Test
    void getCryptoCurrencyByChatId() {
        CryptoCurrency variable = CryptoCurrency.MONERO;
        UserData userData = userDataRepository.save(UserData.builder().user(user).build());
        assertNull(userDataRepository.getCryptoCurrencyByChatId(user.getChatId()));
        userData.setCryptoCurrency(variable);
        userDataRepository.save(userData);
        assertEquals(variable, userDataRepository.getCryptoCurrencyByChatId(user.getChatId()));
        assertNull(userDataRepository.getCryptoCurrencyByChatId(Long.MAX_VALUE));
    }

    @Test
    void countByUserPid() {
        assertEquals(0, userDataRepository.countByUserPid(user.getPid()));
        userDataRepository.save(UserData.builder().user(user).build());
        assertEquals(1, userDataRepository.countByUserPid(user.getPid()));
        assertEquals(0, userDataRepository.countByUserPid(Long.MAX_VALUE));
    }

    @Test
    void updateLongByUserPid() {
        userDataRepository.save(UserData.builder().user(user).build());
        assertNull(userDataRepository.getLongByUserPid(user.getPid()));
        Long variable = 10L;
        userDataRepository.updateLongByUserPid(user.getPid(), variable);
        entityManager.clear();
        assertEquals(variable, userDataRepository.getLongByUserPid(user.getPid()));
    }

    @Test
    void updateStringByUserChatId() {
        userDataRepository.save(UserData.builder().user(user).build());
        assertNull(userDataRepository.getStringByUserChatId(user.getChatId()));
        String variable = "str";
        userDataRepository.updateStringByUserChatId(user.getChatId(), variable);
        entityManager.clear();
        assertEquals(variable, userDataRepository.getStringByUserChatId(user.getChatId()));
    }

    @Test
    void updateDealTypeByUserChatId() {
        userDataRepository.save(UserData.builder().user(user).build());
        assertNull(userDataRepository.getDealTypeByChatId(user.getChatId()));
        DealType variable = DealType.SELL;
        userDataRepository.updateDealTypeByUserChatId(user.getChatId(), variable);
        entityManager.clear();
        assertEquals(variable, userDataRepository.getDealTypeByChatId(user.getChatId()));
    }

    @Test
    void updateFiatCurrencyByUserChatId() {
        userDataRepository.save(UserData.builder().user(user).build());
        assertNull(userDataRepository.getFiatCurrencyByChatId(user.getChatId()));
        FiatCurrency variable = FiatCurrency.RUB;
        userDataRepository.updateFiatCurrencyByUserChatId(user.getChatId(), variable);
        entityManager.clear();
        assertEquals(variable, userDataRepository.getFiatCurrencyByChatId(user.getChatId()));
    }

    @Test
    void deleteByUser_ChatId() {
        User notBeDeletedDataUser = userRepository.save(User.builder().chatId(chatId + 1).isActive(true).isBanned(false)
                .registrationDate(LocalDateTime.now()).referralBalance(0).build());
        Set<UserData> expected = new HashSet<>();
        expected.add(userDataRepository.save(UserData.builder().user(notBeDeletedDataUser).build()));
        userDataRepository.save(UserData.builder().user(user).build());
        expected.add(userDataRepository.save(UserData.builder().build()));
        userDataRepository.deleteByUser_ChatId(user.getChatId());
        assertEquals(expected, new HashSet<>(userDataRepository.findAll()));
    }

    @Test
    void updateCryptoCurrencyByChatId() {
        userDataRepository.save(UserData.builder().user(user).build());
        assertNull(userDataRepository.getCryptoCurrencyByChatId(user.getChatId()));
        CryptoCurrency variable = CryptoCurrency.USDT;
        userDataRepository.updateCryptoCurrencyByChatId(user.getChatId(), variable);
        entityManager.clear();
        assertEquals(variable, userDataRepository.getCryptoCurrencyByChatId(user.getChatId()));
    }
}