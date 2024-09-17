package tgb.btc.library.repository.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import tgb.btc.library.bean.bot.DealPayment;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ActiveProfiles("test")
class DealPaymentRepositoryTest {

    @Autowired
    private DealPaymentRepository dealPaymentRepository;

    @Test
    void findAllSortedDescDateTime() {
        List<DealPayment> expected = new ArrayList<>();
        LocalDateTime dateTime = LocalDateTime.of(2000, 1, 1, 1, 0);
        for (int i = 0; i < 100; i++) {
            DealPayment dealPayment = new DealPayment();
            dealPayment.setDateTime(dateTime.plusMinutes(i));
            dealPaymentRepository.save(dealPayment);
            if (i >= 50) {
                expected.add(dealPayment);
            }
        }
        Collections.reverse(expected);
        assertEquals(expected, dealPaymentRepository.findAllSortedDescDateTime());
    }
}