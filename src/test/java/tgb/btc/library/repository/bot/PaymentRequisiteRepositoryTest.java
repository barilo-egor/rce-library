package tgb.btc.library.repository.bot;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import tgb.btc.library.bean.bot.PaymentRequisite;
import tgb.btc.library.bean.bot.PaymentType;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@DataJpaTest
@ActiveProfiles("test")
class PaymentRequisiteRepositoryTest {

    @Autowired
    private PaymentRequisiteRepository paymentRequisiteRepository;

    @Autowired
    private PaymentTypeRepository paymentTypeRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void getByPaymentType() {
        PaymentType paymentType = paymentTypeRepository.save(PaymentType.builder().build());
        PaymentType paymentTypeWithoutRequisites = paymentTypeRepository.save(PaymentType.builder().build());
        assertEquals(new ArrayList<>(), paymentRequisiteRepository.getByPaymentType(paymentType));
        assertEquals(new ArrayList<>(), paymentRequisiteRepository.getByPaymentType(paymentTypeWithoutRequisites));
        Set<PaymentRequisite> expected = new HashSet<>();

        for (int i = 0; i < 6; i++) {
            PaymentRequisite paymentRequisite = new PaymentRequisite();
            if (i % 2 == 0) {
                paymentRequisite.setPaymentType(paymentType);
            }
            paymentRequisiteRepository.save(paymentRequisite);
            if (i % 2 == 0) {
                expected.add(paymentRequisite);
            }
        }
        assertEquals(expected, new HashSet<>(paymentRequisiteRepository.getByPaymentType(paymentType)));
        assertEquals(new ArrayList<>(), paymentRequisiteRepository.getByPaymentType(paymentTypeWithoutRequisites));
    }

    @Test
    void getCountByPaymentType() {
        PaymentType paymentType = paymentTypeRepository.save(PaymentType.builder().build());
        PaymentType paymentTypeWithoutRequisites = paymentTypeRepository.save(PaymentType.builder().build());
        assertEquals(0, paymentRequisiteRepository.getCountByPaymentType(paymentType));
        assertEquals(0, paymentRequisiteRepository.getCountByPaymentType(paymentTypeWithoutRequisites));
        Set<PaymentRequisite> expected = new HashSet<>();

        for (int i = 0; i < 6; i++) {
            PaymentRequisite paymentRequisite = new PaymentRequisite();
            if (i % 2 == 0) {
                paymentRequisite.setPaymentType(paymentType);
            }
            paymentRequisiteRepository.save(paymentRequisite);
            if (i % 2 == 0) {
                expected.add(paymentRequisite);
            }
        }
        assertEquals(expected.size(), paymentRequisiteRepository.getCountByPaymentType(paymentType));
        assertEquals(0, paymentRequisiteRepository.getCountByPaymentType(paymentTypeWithoutRequisites));
    }

    @Test
    void getByPaymentType_Pid() {
        PaymentType paymentType = paymentTypeRepository.save(PaymentType.builder().build());
        PaymentType paymentTypeWithoutRequisites = paymentTypeRepository.save(PaymentType.builder().build());
        assertEquals(new ArrayList<>(), paymentRequisiteRepository.getByPaymentType_Pid(paymentType.getPid()));
        assertEquals(new ArrayList<>(), paymentRequisiteRepository.getByPaymentType_Pid(paymentTypeWithoutRequisites.getPid()));
        Set<PaymentRequisite> expected = new HashSet<>();

        for (int i = 0; i < 6; i++) {
            PaymentRequisite paymentRequisite = new PaymentRequisite();
            if (i % 2 == 0) {
                paymentRequisite.setPaymentType(paymentType);
            }
            paymentRequisiteRepository.save(paymentRequisite);
            if (i % 2 == 0) {
                expected.add(paymentRequisite);
            }
        }
        assertEquals(expected, new HashSet<>(paymentRequisiteRepository.getByPaymentType_Pid(paymentType.getPid())));
        assertEquals(new ArrayList<>(), paymentRequisiteRepository.getByPaymentType_Pid(paymentTypeWithoutRequisites.getPid()));
    }

    @Test
    void getPaymentTypeByPid() {
        PaymentType paymentType = paymentTypeRepository.save(PaymentType.builder().build());
        PaymentRequisite paymentRequisite = paymentRequisiteRepository.save(PaymentRequisite.builder().paymentType(paymentType).build());
        PaymentRequisite paymentRequisiteWithoutPaymentType = paymentRequisiteRepository.save(PaymentRequisite.builder().build());
        assertEquals(paymentType, paymentRequisiteRepository.getPaymentTypeByPid(paymentRequisite.getPid()));
        assertNull(paymentRequisiteRepository.getPaymentTypeByPid(paymentRequisiteWithoutPaymentType.getPid()));
        assertNull(paymentRequisiteRepository.getPaymentTypeByPid(Long.MAX_VALUE));
    }

    @Test
    void countByPaymentTypePidAndIsOn() {
        PaymentType paymentType = paymentTypeRepository.save(PaymentType.builder().build());
        int expected = 0;
        for (int i = 0; i < 10; i++) {
            PaymentRequisite paymentRequisite = new PaymentRequisite();
            if (i % 2 == 0) {
                paymentRequisite.setPaymentType(paymentType);
            }
            paymentRequisite.setIsOn(i % 3 == 0);
            paymentRequisiteRepository.save(paymentRequisite);
            if  (i % 2 == 0 && i % 3 == 0) {
                expected++;
            }
        }
        assertEquals(expected, paymentRequisiteRepository.countByPaymentTypePidAndIsOn(paymentType.getPid()));
        assertEquals(0, paymentRequisiteRepository.countByPaymentTypePidAndIsOn(Long.MAX_VALUE));
    }

    @Test
    void updateRequisiteByPid() {
        PaymentRequisite paymentRequisite = paymentRequisiteRepository.save(PaymentRequisite.builder().build());
        entityManager.clear();

        paymentRequisiteRepository.updateRequisiteByPid("newRequisite", paymentRequisite.getPid());
        PaymentRequisite updated = paymentRequisiteRepository.getById(paymentRequisite.getPid());
        assertEquals("newRequisite", updated.getRequisite());
    }

    @Test
    void deleteByPaymentTypePid() {
        PaymentType paymentType = paymentTypeRepository.save(PaymentType.builder().build());
        Set<PaymentRequisite> expectedAfterDelete = new HashSet<>();
        Set<PaymentRequisite> deleted = new HashSet<>();
        for (int i = 0; i < 10; i++) {
            PaymentRequisite paymentRequisite = new PaymentRequisite();
            if (i % 3 == 0) {
                paymentRequisite.setPaymentType(paymentType);
            }
            paymentRequisiteRepository.save(paymentRequisite);
            if  (i % 3 == 0) {
                deleted.add(paymentRequisite);
            } else {
                expectedAfterDelete.add(paymentRequisite);
            }
        }
        assertEquals(deleted, new HashSet<>(paymentRequisiteRepository.getByPaymentType_Pid(paymentType.getPid())));
        paymentRequisiteRepository.deleteByPaymentTypePid(paymentType.getPid());
        assertEquals(new ArrayList<>(), paymentRequisiteRepository.getByPaymentType_Pid(paymentType.getPid()));
        assertEquals(expectedAfterDelete, new HashSet<>(paymentRequisiteRepository.findAll()));
    }
}