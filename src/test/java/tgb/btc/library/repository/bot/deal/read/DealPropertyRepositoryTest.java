package tgb.btc.library.repository.bot.deal.read;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import tgb.btc.library.bean.bot.Deal;
import tgb.btc.library.constants.enums.bot.*;
import tgb.btc.library.repository.bot.DealRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class DealPropertyRepositoryTest {

    @Autowired
    private DealRepository dealRepository;

    private Long firstDealPid;

    private Long secondDealPid;

    private Long emptyDealPid;

    @BeforeEach
    void setUp() {
        firstDealPid = dealRepository.save(
                Deal.builder().cryptoCurrency(CryptoCurrency.BITCOIN).commission(new BigDecimal(10))
                        .amount(new BigDecimal(20)).cryptoAmount(new BigDecimal("0.001")).discount(new BigDecimal(30))
                        .dealType(DealType.BUY).dateTime(LocalDateTime.of(2000, 1, 1, 0, 0))
                        .fiatCurrency(FiatCurrency.BYN).additionalVerificationImageId("additionalVerification1")
                        .deliveryType(DeliveryType.STANDARD).creditedAmount(new BigDecimal(40))
                        .dealStatus(DealStatus.NEW)
                        .isUsedPromo(true).build()
        ).getPid();
        secondDealPid = dealRepository.save(
                Deal.builder().cryptoCurrency(CryptoCurrency.MONERO).commission(new BigDecimal(50))
                        .amount(new BigDecimal(60)).cryptoAmount(new BigDecimal("0.5")).discount(new BigDecimal(70))
                        .dealType(DealType.SELL).dateTime(LocalDateTime.of(2020, 11, 11, 1, 1))
                        .fiatCurrency(FiatCurrency.RUB).additionalVerificationImageId("additionalVerification2")
                        .deliveryType(DeliveryType.VIP).creditedAmount(new BigDecimal(80)).dealStatus(DealStatus.PAID)
                        .isUsedPromo(false).build()
        ).getPid();
        emptyDealPid = dealRepository.save(Deal.builder().build()).getPid();
    }

    @Test
    void getCryptoCurrencyByPid() {
        assertAll(
                () -> assertEquals(CryptoCurrency.BITCOIN, dealRepository.getCryptoCurrencyByPid(firstDealPid)),
                () -> assertEquals(CryptoCurrency.MONERO, dealRepository.getCryptoCurrencyByPid(secondDealPid)),
                () -> assertNull(dealRepository.getCryptoCurrencyByPid(emptyDealPid)),
                () -> assertNull(dealRepository.getCryptoCurrencyByPid(100L))
        );
    }

    @Test
    void getCommissionByPid() {
        assertAll(
                () -> assertEquals(0, new BigDecimal(10).compareTo(dealRepository.getCommissionByPid(firstDealPid))),
                () -> assertEquals(0, new BigDecimal(50).compareTo(dealRepository.getCommissionByPid(secondDealPid))),
                () -> assertNull(dealRepository.getCommissionByPid(emptyDealPid)),
                () -> assertNull(dealRepository.getCommissionByPid(100L))
        );
    }

    @Test
    void getAmountByPid() {
        assertAll(
                () -> assertEquals(0, new BigDecimal(20).compareTo(dealRepository.getAmountByPid(firstDealPid))),
                () -> assertEquals(0, new BigDecimal(60).compareTo(dealRepository.getAmountByPid(secondDealPid))),
                () -> assertNull(dealRepository.getAmountByPid(emptyDealPid)),
                () -> assertNull(dealRepository.getAmountByPid(100L))
        );
    }

    @Test
    void getCryptoAmountByPid() {
        assertAll(
                () -> assertEquals(0,
                        new BigDecimal("0.001").compareTo(dealRepository.getCryptoAmountByPid(firstDealPid))),
                () -> assertEquals(0,
                        new BigDecimal("0.5").compareTo(dealRepository.getCryptoAmountByPid(secondDealPid))),
                () -> assertNull(dealRepository.getCryptoAmountByPid(emptyDealPid)),
                () -> assertNull(dealRepository.getCryptoAmountByPid(100L))
        );
    }

    @Test
    void getDiscountByPid() {
        assertAll(
                () -> assertEquals(0, new BigDecimal(30).compareTo(dealRepository.getDiscountByPid(firstDealPid))),
                () -> assertEquals(0, new BigDecimal(70).compareTo(dealRepository.getDiscountByPid(secondDealPid))),
                () -> assertNull(dealRepository.getDiscountByPid(emptyDealPid)),
                () -> assertNull(dealRepository.getDiscountByPid(100L))
        );
    }

    @Test
    void getDealTypeByPid() {
        assertAll(
                () -> assertEquals(DealType.BUY, dealRepository.getDealTypeByPid(firstDealPid)),
                () -> assertEquals(DealType.SELL, dealRepository.getDealTypeByPid(secondDealPid)),
                () -> assertNull(dealRepository.getDealTypeByPid(emptyDealPid)),
                () -> assertNull(dealRepository.getDealTypeByPid(100L))
        );
    }

    @Test
    void getDateTimeByPid() {
        assertAll(
                () -> assertEquals(0, LocalDateTime.of(2000, 1, 1, 0, 0)
                        .compareTo(dealRepository.getDateTimeByPid(firstDealPid))),
                () -> assertEquals(0, LocalDateTime.of(2020, 11, 11, 1, 1)
                        .compareTo(dealRepository.getDateTimeByPid(secondDealPid))),
                () -> assertNull(dealRepository.getDateTimeByPid(emptyDealPid)),
                () -> assertNull(dealRepository.getDateTimeByPid(100L))
        );
    }

    @Test
    void getFiatCurrencyByPid() {
        assertAll(
                () -> assertEquals(FiatCurrency.BYN, dealRepository.getFiatCurrencyByPid(firstDealPid)),
                () -> assertEquals(FiatCurrency.RUB, dealRepository.getFiatCurrencyByPid(secondDealPid)),
                () -> assertNull(dealRepository.getFiatCurrencyByPid(emptyDealPid)),
                () -> assertNull(dealRepository.getFiatCurrencyByPid(100L))
        );
    }

    @Test
    void getAdditionalVerificationImageIdByPid() {
        assertAll(
                () -> assertEquals("additionalVerification1",
                        dealRepository.getAdditionalVerificationImageIdByPid(firstDealPid)),
                () -> assertEquals("additionalVerification2",
                        dealRepository.getAdditionalVerificationImageIdByPid(secondDealPid)),
                () -> assertNull(dealRepository.getAdditionalVerificationImageIdByPid(emptyDealPid)),
                () -> assertNull(dealRepository.getAdditionalVerificationImageIdByPid(100L))
        );
    }

    @Test
    void getDeliveryTypeByPid() {
        assertAll(
                () -> assertEquals(DeliveryType.STANDARD, dealRepository.getDeliveryTypeByPid(firstDealPid)),
                () -> assertEquals(DeliveryType.VIP, dealRepository.getDeliveryTypeByPid(secondDealPid)),
                () -> assertNull(dealRepository.getDeliveryTypeByPid(emptyDealPid)),
                () -> assertNull(dealRepository.getDeliveryTypeByPid(100L))
        );
    }

    @Test
    void getCreditedAmountByPid() {
        assertAll(
                () -> assertEquals(0,
                        new BigDecimal(40).compareTo(dealRepository.getCreditedAmountByPid(firstDealPid))),
                () -> assertEquals(0,
                        new BigDecimal(80).compareTo(dealRepository.getCreditedAmountByPid(secondDealPid))),
                () -> assertNull(dealRepository.getCreditedAmountByPid(emptyDealPid)),
                () -> assertNull(dealRepository.getCreditedAmountByPid(100L))
        );
    }

    @Test
    void getDealStatusByPid() {
        assertAll(
                () -> assertEquals(DealStatus.NEW, dealRepository.getDealStatusByPid(firstDealPid)),
                () -> assertEquals(DealStatus.PAID, dealRepository.getDealStatusByPid(secondDealPid)),
                () -> assertNull(dealRepository.getDealStatusByPid(emptyDealPid)),
                () -> assertNull(dealRepository.getDealStatusByPid(100L))
        );
    }

    @Test
    void getIsUsedPromoByPid() {
        assertAll(
                () -> assertTrue(dealRepository.getIsUsedPromoByPid(firstDealPid)),
                () -> assertFalse(dealRepository.getIsUsedPromoByPid(secondDealPid)),
                () -> assertNull(dealRepository.getIsUsedPromoByPid(emptyDealPid)),
                () -> assertNull(dealRepository.getIsUsedPromoByPid(100L))
        );
    }
}