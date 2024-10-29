package tgb.btc.library.repository.bot;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import tgb.btc.library.bean.bot.Deal;

import java.time.LocalDateTime;

@DataJpaTest
@ActiveProfiles("test")
class DealRepositoryTest {

    @Autowired
    private DealRepository dealRepository;

    @Test
    void testSaveDeal() {
        Deal deal = new Deal();
        deal.setDateTime(LocalDateTime.now());
        dealRepository.save(deal);

        Deal foundDeal = dealRepository.findByPid(deal.getPid());

        Assertions.assertEquals(deal.getDateTime(), foundDeal.getDateTime());
    }
}