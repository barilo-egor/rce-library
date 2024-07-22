package tgb.btc.library.repository.bot.deal.read;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import tgb.btc.library.bean.bot.Deal;
import tgb.btc.library.bean.bot.User;
import tgb.btc.library.constants.enums.bot.CryptoCurrency;
import tgb.btc.library.constants.enums.bot.DealStatus;
import tgb.btc.library.constants.enums.bot.DealType;
import tgb.btc.library.repository.bot.DealRepository;
import tgb.btc.library.repository.bot.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ActiveProfiles("test")
public class DealCountRepositoryTest {

    @Autowired
    private DealRepository dealRepository;

    @Autowired
    private UserRepository userRepository;

    private final Long chatId1 = 87654321L;

    private final Long chatId2 = 12345678L;

    @Test
    @DisplayName("Должен вернуть количество сделок по указанному chatId и вне указанных статусов.")
    void getCountDealByChatIdAndNotInDealStatus() {
        assertAll(
                () -> assertEquals(2, dealRepository.getCountDealByChatIdAndNotInDealStatus(chatId1,
                        List.of(DealStatus.PAID, DealStatus.CONFIRMED, DealStatus.VERIFICATION_REJECTED))),
                () -> assertEquals(2, dealRepository.getCountDealByChatIdAndNotInDealStatus(chatId1,
                        List.of(DealStatus.NEW, DealStatus.VERIFICATION_REJECTED))),
                () -> assertEquals(7, dealRepository.getCountDealByChatIdAndNotInDealStatus(chatId1,
                        List.of())),
                () -> assertEquals(4, dealRepository.getCountDealByChatIdAndNotInDealStatus(chatId1,
                        List.of(DealStatus.AWAITING_VERIFICATION, DealStatus.VERIFICATION_REJECTED))),
                () -> assertEquals(0, dealRepository.getCountDealByChatIdAndNotInDealStatus(chatId1,
                        List.of(DealStatus.NEW, DealStatus.PAID, DealStatus.CONFIRMED, DealStatus.VERIFICATION_REJECTED))),
                () -> assertEquals(1, dealRepository.getCountDealByChatIdAndNotInDealStatus(chatId2,
                        List.of(DealStatus.PAID, DealStatus.VERIFICATION_REJECTED)))
        );
    }

    @Test
    @DisplayName("Должен вернуть количество сделок по указанным chatId и статусу.")
    void getCountByDealStatusAndChatId() {
        assertAll(
                () -> assertEquals(1, dealRepository.getCountByDealStatusAndChatId(chatId2, DealStatus.PAID)),
                () -> assertEquals(0, dealRepository.getCountByDealStatusAndChatId(chatId2, DealStatus.AWAITING_VERIFICATION)),
                () -> assertEquals(2, dealRepository.getCountByDealStatusAndChatId(chatId1, DealStatus.NEW)),
                () -> assertEquals(1, dealRepository.getCountByDealStatusAndChatId(chatId1, DealStatus.PAID))
        );
    }

    @Test
    @DisplayName("Должен вернуть количество сделок по указанным chatId, статусу, типу сделки и криптовалюте.")
    void getDealsCountByUserChatIdAndDealStatusAndDealTypeAndCryptoCurrency() {
        assertAll(
                () -> assertEquals(1, dealRepository.getDealsCountByUserChatIdAndDealStatusAndDealTypeAndCryptoCurrency(
                        chatId1, DealStatus.NEW, DealType.BUY, CryptoCurrency.BITCOIN)),
                () -> assertEquals(0, dealRepository.getDealsCountByUserChatIdAndDealStatusAndDealTypeAndCryptoCurrency(
                        chatId1, DealStatus.AWAITING_VERIFICATION, DealType.BUY, CryptoCurrency.BITCOIN)),
                () -> assertEquals(0, dealRepository.getDealsCountByUserChatIdAndDealStatusAndDealTypeAndCryptoCurrency(
                        chatId1, DealStatus.NEW, DealType.BUY, CryptoCurrency.LITECOIN)),
                () -> assertEquals(3, dealRepository.getDealsCountByUserChatIdAndDealStatusAndDealTypeAndCryptoCurrency(
                        chatId1, DealStatus.VERIFICATION_REJECTED, DealType.SELL, CryptoCurrency.LITECOIN))
        );
    }

    @BeforeEach
    void setUp() {
        User user1 = userRepository.save(User.builder().chatId(chatId1).isActive(true).isBanned(false)
                .registrationDate(LocalDateTime.now()).referralBalance(0).build());
        User user2 = userRepository.save(User.builder().chatId(chatId2).isActive(true).isBanned(false)
                .registrationDate(LocalDateTime.now()).referralBalance(0).build());
        dealRepository.save(Deal.builder().dealStatus(DealStatus.NEW).user(user1)
                .dealType(DealType.BUY).cryptoCurrency(CryptoCurrency.BITCOIN).build());
        dealRepository.save(Deal.builder().dealStatus(DealStatus.NEW).user(user1)
                .dealType(DealType.SELL).cryptoCurrency(CryptoCurrency.BITCOIN).build());
        dealRepository.save(Deal.builder().dealStatus(DealStatus.PAID).user(user1)
                .dealType(DealType.SELL).cryptoCurrency(CryptoCurrency.MONERO).build());
        dealRepository.save(Deal.builder().dealStatus(DealStatus.CONFIRMED).user(user1)
                .dealType(DealType.BUY).cryptoCurrency(CryptoCurrency.USDT).build());
        dealRepository.save(Deal.builder().dealStatus(DealStatus.PAID).user(user2)
                .dealType(DealType.BUY).cryptoCurrency(CryptoCurrency.MONERO).build());
        dealRepository.save(Deal.builder().dealStatus(DealStatus.NEW).user(user2)
                .dealType(DealType.BUY).cryptoCurrency(CryptoCurrency.MONERO).build());
        dealRepository.save(Deal.builder().dealStatus(DealStatus.VERIFICATION_REJECTED).user(user1)
                .dealType(DealType.SELL).cryptoCurrency(CryptoCurrency.LITECOIN).build());
        dealRepository.save(Deal.builder().dealStatus(DealStatus.VERIFICATION_REJECTED).user(user1)
                .dealType(DealType.SELL).cryptoCurrency(CryptoCurrency.LITECOIN).build());
        dealRepository.save(Deal.builder().dealStatus(DealStatus.VERIFICATION_REJECTED).user(user1)
                .dealType(DealType.SELL).cryptoCurrency(CryptoCurrency.LITECOIN).build());
    }
}
