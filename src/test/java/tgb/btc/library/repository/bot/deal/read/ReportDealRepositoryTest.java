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
import tgb.btc.library.constants.enums.bot.FiatCurrency;
import tgb.btc.library.repository.bot.DealRepository;
import tgb.btc.library.repository.bot.UserRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class ReportDealRepositoryTest {

    @Autowired
    private DealRepository dealRepository;

    @Autowired
    private UserRepository userRepository;

    private final Long chatId1 = 87654321L;

    private final Long chatId2 = 12345678L;

    private Long userPid1;

    private Long userPid2;

    @Test
    @DisplayName("Должен вернуть сумму крипты сделок, отобранных по параметрам.")
    void getConfirmedCryptoAmountSum() {
        assertAll(
                () -> assertEquals(0, new BigDecimal("0.6").compareTo(
                        dealRepository.getConfirmedCryptoAmountSum(DealType.BUY,
                        LocalDateTime.of(2000, 1, 1, 0, 0),
                        LocalDateTime.of(2000, 1, 3, 0, 0), CryptoCurrency.BITCOIN))
                ),
                () -> assertEquals(0, new BigDecimal("0.4").compareTo(
                        dealRepository.getConfirmedCryptoAmountSum(DealType.BUY,
                                LocalDateTime.of(1999, 1, 1, 0, 0),
                                LocalDateTime.of(2000, 1, 2, 0, 0), CryptoCurrency.BITCOIN))
                ),
                () -> assertEquals(0, new BigDecimal("0.2").compareTo(
                        dealRepository.getConfirmedCryptoAmountSum(DealType.SELL,
                                LocalDateTime.of(1999, 1, 1, 0, 0),
                                LocalDateTime.of(2000, 1, 3, 0, 0), CryptoCurrency.BITCOIN))
                ),
                () -> assertEquals(0, new BigDecimal("0.4").compareTo(
                        dealRepository.getConfirmedCryptoAmountSum(DealType.BUY,
                                LocalDateTime.of(2000, 1, 1, 2, 0),
                                LocalDateTime.of(2020, 1, 2, 0, 0), CryptoCurrency.BITCOIN))
                ),
                () -> assertEquals(0, new BigDecimal("0.2").compareTo(
                        dealRepository.getConfirmedCryptoAmountSum(DealType.BUY,
                                LocalDateTime.of(2000, 1, 1, 0, 0),
                                LocalDateTime.of(2000, 1, 1, 0, 0), CryptoCurrency.BITCOIN))
                )
        );
    }

    @Test
    @DisplayName("Должен вернуть null при отсутствии найденных сделок.")
    void getConfirmedCryptoAmountSumNoDeals() {
        assertNull(dealRepository.getConfirmedCryptoAmountSum(DealType.BUY,
                LocalDateTime.of(2000, 10, 1, 0, 0),
                LocalDateTime.of(2000, 11, 1, 0, 0), CryptoCurrency.BITCOIN)
        );
    }

    @Test
    @DisplayName("Должен вернуть сумму фиата сделок, отобранных по параметрам.")
    void getConfirmedTotalAmountSum() {
        assertAll(
                () -> assertEquals(0, new BigDecimal(30).compareTo(
                        dealRepository.getConfirmedTotalAmountSum(DealType.BUY,
                                LocalDateTime.of(2000, 1, 1, 0, 0),
                                LocalDateTime.of(2000, 1, 3, 0, 0),
                                CryptoCurrency.BITCOIN, FiatCurrency.BYN))
                ),
                () -> assertEquals(0, new BigDecimal(10).compareTo(
                        dealRepository.getConfirmedTotalAmountSum(DealType.BUY,
                                LocalDateTime.of(1999, 1, 1, 0, 0),
                                LocalDateTime.of(2000, 1, 1, 0, 0),
                                CryptoCurrency.BITCOIN, FiatCurrency.BYN))
                ),
                () -> assertEquals(0, new BigDecimal(20).compareTo(
                        dealRepository.getConfirmedTotalAmountSum(DealType.BUY,
                                LocalDateTime.of(2000, 1, 2, 0, 0),
                                LocalDateTime.of(2000, 1, 31, 23, 0),
                                CryptoCurrency.BITCOIN, FiatCurrency.BYN))
                )
        );
    }

    @Test
    @DisplayName("Должен вернуть null при отсутствии найденных сделок.")
    void getConfirmedTotalAmountSumNoDeals() {
        assertNull(dealRepository.getConfirmedTotalAmountSum(DealType.BUY,
                LocalDateTime.of(1980, 10, 1, 0, 0),
                LocalDateTime.of(1990, 11, 1, 0, 0),
                CryptoCurrency.BITCOIN, FiatCurrency.BYN)
        );
    }


    @Test
    void findAllForUsersReport() {
        List<Object[]> actual = dealRepository.findAllForUsersReport();
        assertNotNull(actual);
        assertAll(
                () -> assertEquals(5, actual.size()),
                () -> assertEquals(userPid1, actual.get(0)[1]),
                () -> assertEquals(DealType.BUY, actual.get(1)[2]),
                () -> assertEquals(CryptoCurrency.BITCOIN, actual.get(2)[3]),
                () -> assertEquals(0, new BigDecimal("0.2").compareTo((BigDecimal) actual.get(3)[4])),
                () -> assertEquals(FiatCurrency.BYN, actual.get(4)[5]),
                () -> assertEquals(0, new BigDecimal(10).compareTo((BigDecimal) actual.get(0)[6]))
        );
    }

    @BeforeEach
    void setUp() {
        User user1 = userRepository.save(User.builder().chatId(chatId1).isActive(true).isBanned(false)
                .registrationDate(LocalDateTime.now()).referralBalance(0).build());
        userPid1 = user1.getPid();
        User user2 = userRepository.save(User.builder().chatId(chatId2).isActive(true).isBanned(false)
                .registrationDate(LocalDateTime.now()).referralBalance(0).build());
        userPid2 = user2.getPid();
        dealRepository.save(Deal.builder().dealType(DealType.BUY)
                .dateTime(LocalDateTime.of(2000, 1, 1, 0, 0))
                .cryptoCurrency(CryptoCurrency.BITCOIN)
                .dealStatus(DealStatus.CONFIRMED)
                .cryptoAmount(new BigDecimal("0.2"))
                .fiatCurrency(FiatCurrency.BYN)
                .user(user1)
                .amount(new BigDecimal(10))
                .build());
        dealRepository.save(Deal.builder().dealType(DealType.BUY)
                .dateTime(LocalDateTime.of(2000, 1, 2, 0, 0))
                .cryptoCurrency(CryptoCurrency.BITCOIN)
                .dealStatus(DealStatus.CONFIRMED)
                .cryptoAmount(new BigDecimal("0.2"))
                .fiatCurrency(FiatCurrency.BYN)
                .user(user1)
                .amount(new BigDecimal(10))
                .build());
        dealRepository.save(Deal.builder().dealType(DealType.BUY)
                .dateTime(LocalDateTime.of(2000, 1, 3, 0, 0))
                .cryptoCurrency(CryptoCurrency.BITCOIN)
                .dealStatus(DealStatus.CONFIRMED)
                .cryptoAmount(new BigDecimal("0.2"))
                .fiatCurrency(FiatCurrency.BYN)
                .user(user2)
                .amount(new BigDecimal(10))
                .build());
        dealRepository.save(Deal.builder().dealType(DealType.SELL)
                .dateTime(LocalDateTime.of(2000, 1, 3, 0, 0))
                .cryptoCurrency(CryptoCurrency.BITCOIN)
                .dealStatus(DealStatus.CONFIRMED)
                .cryptoAmount(new BigDecimal("0.2"))
                .fiatCurrency(FiatCurrency.BYN)
                .user(user1)
                .amount(new BigDecimal(10))
                .build());
        dealRepository.save(Deal.builder().dealType(DealType.BUY)
                .dateTime(LocalDateTime.of(2000, 2, 1, 0, 0))
                .cryptoCurrency(CryptoCurrency.MONERO)
                .dealStatus(DealStatus.CONFIRMED)
                .cryptoAmount(new BigDecimal("0.2"))
                .fiatCurrency(FiatCurrency.BYN)
                .user(user1)
                .amount(new BigDecimal(10))
                .build());
        dealRepository.save(Deal.builder().dealType(DealType.BUY)
                .dateTime(LocalDateTime.of(2000, 2, 1, 0, 0))
                .cryptoCurrency(CryptoCurrency.MONERO)
                .dealStatus(DealStatus.AWAITING_VERIFICATION)
                .cryptoAmount(new BigDecimal("0.2"))
                .fiatCurrency(FiatCurrency.BYN)
                .user(user1)
                .amount(new BigDecimal(10))
                .build());
    }
}