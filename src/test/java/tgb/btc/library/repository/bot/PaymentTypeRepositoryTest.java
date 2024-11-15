package tgb.btc.library.repository.bot;

import jakarta.persistence.EntityManager;
import org.apache.commons.lang3.BooleanUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import tgb.btc.library.bean.bot.PaymentType;
import tgb.btc.library.constants.enums.bot.DealType;
import tgb.btc.library.constants.enums.bot.FiatCurrency;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class PaymentTypeRepositoryTest {

    @Autowired
    private PaymentTypeRepository paymentTypeRepository;

    @Autowired
    private EntityManager entityManager;

    private final int typesCount = 20;

    @BeforeEach
    void setUp() {
        for (int i = 0; i < typesCount; i++) {
            PaymentType paymentType = new PaymentType();
            paymentType.setOn(i % 2 == 0);
            paymentType.setName("name" + i);
            paymentType.setDealType(i % 2 == 1 ? DealType.BUY : DealType.SELL);
            paymentType.setFiatCurrency(FiatCurrency.values()[i % FiatCurrency.values().length]);
            paymentType.setMinSum(new BigDecimal(i));
            paymentType.setDynamicOn(i < (typesCount / 2));
            paymentTypeRepository.save(paymentType);
        }
        for (int i = 0; i < 3; i++) {
            PaymentType paymentType = new PaymentType();
            paymentTypeRepository.save(paymentType);
        }
    }

    @Test
    void getByDealType() {
        List<PaymentType> allPaymentTypes = paymentTypeRepository.findAll();
        assertAll(
                () -> assertEquals(allPaymentTypes.stream()
                                .filter(paymentType -> DealType.BUY.equals(paymentType.getDealType()))
                                .collect(Collectors.toSet()),
                        new HashSet<>(paymentTypeRepository.getByDealType(DealType.BUY))),
                () -> assertEquals(allPaymentTypes.stream()
                                .filter(paymentType -> DealType.SELL.equals(paymentType.getDealType()))
                                .collect(Collectors.toSet()),
                        new HashSet<>(paymentTypeRepository.getByDealType(DealType.SELL)))
        );
    }

    @Test
    void getByDealTypeAndFiatCurrency() {
        List<PaymentType> allPaymentTypes = paymentTypeRepository.findAll();
        assertEquals(allPaymentTypes.stream()
                        .filter(paymentType -> DealType.BUY.equals(paymentType.getDealType())
                                && FiatCurrency.BYN.equals(paymentType.getFiatCurrency()))
                        .collect(Collectors.toSet()),
                new HashSet<>(paymentTypeRepository.getByDealTypeAndFiatCurrency(DealType.BUY, FiatCurrency.BYN)));
        assertEquals(allPaymentTypes.stream()
                        .filter(paymentType -> DealType.SELL.equals(paymentType.getDealType())
                                && FiatCurrency.RUB.equals(paymentType.getFiatCurrency()))
                        .collect(Collectors.toSet()),
                new HashSet<>(paymentTypeRepository.getByDealTypeAndFiatCurrency(DealType.SELL, FiatCurrency.RUB)));
    }

    @Test
    void countByDealTypeAndFiatCurrency() {
        List<PaymentType> allPaymentTypes = paymentTypeRepository.findAll();
        assertEquals(allPaymentTypes.stream()
                        .filter(paymentType -> DealType.BUY.equals(paymentType.getDealType())
                                && FiatCurrency.BYN.equals(paymentType.getFiatCurrency()))
                        .count(),
                paymentTypeRepository.countByDealTypeAndFiatCurrency(DealType.BUY, FiatCurrency.BYN));
        assertEquals(allPaymentTypes.stream()
                        .filter(paymentType -> DealType.SELL.equals(paymentType.getDealType())
                                && FiatCurrency.RUB.equals(paymentType.getFiatCurrency()))
                        .count(),
                paymentTypeRepository.countByDealTypeAndFiatCurrency(DealType.SELL, FiatCurrency.RUB));
    }

    @Test
    void getByDealTypeAndIsOnAndFiatCurrency() {
        List<PaymentType> allPaymentTypes = paymentTypeRepository.findAll();
        assertEquals(allPaymentTypes.stream()
                        .filter(paymentType -> DealType.BUY.equals(paymentType.getDealType())
                                && FiatCurrency.BYN.equals(paymentType.getFiatCurrency())
                                && BooleanUtils.isFalse(paymentType.getOn()))
                        .collect(Collectors.toSet()),
                new HashSet<>(
                        paymentTypeRepository.getByDealTypeAndIsOnAndFiatCurrency(DealType.BUY, false, FiatCurrency.BYN)
                )
        );
        assertEquals(allPaymentTypes.stream()
                        .filter(paymentType -> DealType.SELL.equals(paymentType.getDealType())
                                && FiatCurrency.RUB.equals(paymentType.getFiatCurrency())
                                && BooleanUtils.isTrue(paymentType.getOn()))
                        .collect(Collectors.toSet()),
                new HashSet<>(
                        paymentTypeRepository.getByDealTypeAndIsOnAndFiatCurrency(DealType.SELL, true, FiatCurrency.RUB)
                )
        );
    }

    @Test
    void countByDealTypeAndIsOnAndFiatCurrency() {
        List<PaymentType> allPaymentTypes = paymentTypeRepository.findAll();
        assertEquals(allPaymentTypes.stream()
                        .filter(paymentType -> DealType.BUY.equals(paymentType.getDealType())
                                && FiatCurrency.BYN.equals(paymentType.getFiatCurrency())
                                && BooleanUtils.isFalse(paymentType.getOn()))
                        .collect(Collectors.toSet()).size(),
                paymentTypeRepository.countByDealTypeAndIsOnAndFiatCurrency(DealType.BUY, false, FiatCurrency.BYN)

        );
        assertEquals(allPaymentTypes.stream()
                        .filter(paymentType -> DealType.SELL.equals(paymentType.getDealType())
                                && FiatCurrency.RUB.equals(paymentType.getFiatCurrency())
                                && BooleanUtils.isTrue(paymentType.getOn()))
                        .collect(Collectors.toSet()).size(),
                paymentTypeRepository.countByDealTypeAndIsOnAndFiatCurrency(DealType.SELL, true, FiatCurrency.RUB)
        );
    }

    @Test
    void getDealTypeByPid() {
        List<PaymentType> allPaymentTypes = paymentTypeRepository.findAll();
        Optional<PaymentType> paymentType = allPaymentTypes.stream()
                .filter(type -> DealType.BUY.equals(type.getDealType()))
                .findFirst();
        assertTrue(paymentType.isPresent());
        assertEquals(DealType.BUY, paymentTypeRepository.getDealTypeByPid(paymentType.get().getPid()));

        paymentType = allPaymentTypes.stream()
                .filter(type -> DealType.SELL.equals(type.getDealType()))
                .findFirst();
        assertTrue(paymentType.isPresent());
        assertEquals(DealType.SELL, paymentTypeRepository.getDealTypeByPid(paymentType.get().getPid()));

        paymentType = allPaymentTypes.stream()
                .filter(type -> Objects.isNull(type.getDealType()))
                .findFirst();
        assertTrue(paymentType.isPresent());
        assertNull(paymentTypeRepository.getDealTypeByPid(paymentType.get().getPid()));
    }

    @Test
    void countByNameLike() {
        List<PaymentType> allPaymentTypes = paymentTypeRepository.findAll();
        assertAll(
                () -> assertEquals(1, paymentTypeRepository.countByNameLike("name0")),
                () -> assertEquals(1, paymentTypeRepository.countByNameLike("name1")),
                () -> assertEquals(0, paymentTypeRepository.countByNameLike("name")),
                () -> assertEquals(0, paymentTypeRepository.countByNameLike(""))
        );
    }

    @Test
    void updateIsOnByPid() {
        PaymentType paymentType = paymentTypeRepository.save(PaymentType.builder().build());
        paymentTypeRepository.updateIsOnByPid(true, paymentType.getPid());

        entityManager.clear();
        PaymentType updated = paymentTypeRepository.getById(paymentType.getPid());
        assertEquals(true, updated.getOn());
    }

    @Test
    void updateMinSumByPid() {
        PaymentType paymentType = paymentTypeRepository.save(PaymentType.builder().build());
        paymentTypeRepository.updateMinSumByPid(new BigDecimal(100), paymentType.getPid());
        entityManager.clear();

        PaymentType updated = paymentTypeRepository.getById(paymentType.getPid());
        assertEquals(0, new BigDecimal(100).compareTo(updated.getMinSum()));
    }

    @Test
    void updateIsDynamicOnByPid() {
        PaymentType paymentType = paymentTypeRepository.save(PaymentType.builder().build());
        paymentTypeRepository.updateIsDynamicOnByPid(false, paymentType.getPid());

        entityManager.clear();
        PaymentType updated = paymentTypeRepository.getById(paymentType.getPid());
        assertEquals(false, updated.getDynamicOn());
    }
}