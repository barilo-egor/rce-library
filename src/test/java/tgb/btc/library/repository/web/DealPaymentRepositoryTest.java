package tgb.btc.library.repository.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import tgb.btc.library.bean.bot.DealPayment;
import tgb.btc.library.repository.bot.DealRepository;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ActiveProfiles("test")
class DealPaymentRepositoryTest {

    @Autowired
    private DealRepository dealRepository;

    @Autowired
    private DealPaymentRepository dealPaymentRepository;

    @Test
    void findAllSortedDescDateTime() {
        Set<DealPayment> expected = new HashSet<>();
        LocalDateTime dateTime = LocalDateTime.of(2000, 1, 1, 1, 0);
        for (int i = 0; i < 100; i++) {
            DealPayment dealPayment = new DealPayment();
            dealPaymentRepository.save(dealPayment);
            if (i >= 50) {
                expected.add(dealPayment);
            }
        }
        assertEquals(expected, new HashSet<>(dealPaymentRepository.findAllSortedDescDateTime()));
    }
}