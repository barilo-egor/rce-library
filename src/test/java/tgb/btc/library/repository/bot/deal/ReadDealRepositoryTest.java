package tgb.btc.library.repository.bot.deal;

import org.apache.commons.lang.math.RandomUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import tgb.btc.library.bean.bot.Deal;
import tgb.btc.library.bean.bot.User;
import tgb.btc.library.constants.enums.bot.DealStatus;
import tgb.btc.library.repository.bot.DealRepository;
import tgb.btc.library.repository.bot.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class ReadDealRepositoryTest {

    @Autowired
    private DealRepository dealRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void findByPid() {
        Deal deal = dealRepository.save(Deal.builder().build());
        Deal foundedDeal = dealRepository.findByPid(deal.getPid());
        assertEquals(deal.getPid(), foundedDeal.getPid());
        Deal notExistingDeal = dealRepository.findByPid(Long.MAX_VALUE);
        assertNull(notExistingDeal);
        dealRepository.deleteById(foundedDeal.getPid());
        Deal deletedDeal = dealRepository.findByPid(foundedDeal.getPid());
        assertNull(deletedDeal);
    }

    @Test
    void getDealsByPids() {
        List<Long> pidsToFind = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Deal deal = dealRepository.save(Deal.builder().build());
            if (i < 5) pidsToFind.add(deal.getPid());
        }
        List<Deal> deals = dealRepository.getDealsByPids(pidsToFind);
        assertAll(
                () -> assertEquals(pidsToFind.size(), deals.size()),
                () -> assertEquals(pidsToFind, deals.stream().map(Deal::getPid).collect(Collectors.toList())),
                () -> assertEquals(new ArrayList<>(), dealRepository.getDealsByPids(new ArrayList<>()))
        );
    }

    @Test
    void getPidsByChatIdAndStatus() {
        User user1 = userRepository.save(User.builder().chatId(12345678L).isActive(true).isBanned(false)
                .registrationDate(LocalDateTime.now()).referralBalance(0).build());
        User user2 = userRepository.save(User.builder().chatId(87654321L).isActive(true).isBanned(false)
                .registrationDate(LocalDateTime.now()).referralBalance(0).build());
        List<Long> dealsPids = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Deal deal = new Deal();
            if (i % 2 == 0) {
                deal.setDealStatus(DealStatus.NEW);
            } else {
                deal.setDealStatus(DealStatus.CONFIRMED);
            }
            if (i < 5) {
                deal.setUser(user1);
            } else {
                deal.setUser(user2);
            }
            dealRepository.save(deal);
            dealsPids.add(deal.getPid());
        }
        List<Long> user1DealsPids = dealRepository.getPidsByChatIdAndStatus(user1.getChatId(), DealStatus.NEW);
        List<Long> user2NewDealsPids = dealRepository.getPidsByChatIdAndStatus(user2.getChatId(), DealStatus.NEW);
        List<Long> user2ConfirmedDealsPids = dealRepository.getPidsByChatIdAndStatus(user2.getChatId(), DealStatus.CONFIRMED);
        assertAll(
                () -> assertEquals(3, user1DealsPids.size()),
                () -> assertEquals(dealsPids.get(0), user1DealsPids.get(0)),
                () -> assertEquals(dealsPids.get(2), user1DealsPids.get(1)),
                () -> assertEquals(dealsPids.get(4), user1DealsPids.get(2)),
                () -> assertEquals(2, user2NewDealsPids.size()),
                () -> assertEquals(dealsPids.get(6), user2NewDealsPids.get(0)),
                () -> assertEquals(dealsPids.get(8), user2NewDealsPids.get(1)),
                () -> assertEquals(3, user2ConfirmedDealsPids.size()),
                () -> assertEquals(dealsPids.get(5), user2ConfirmedDealsPids.get(0)),
                () -> assertEquals(dealsPids.get(7), user2ConfirmedDealsPids.get(1)),
                () -> assertEquals(dealsPids.get(9), user2ConfirmedDealsPids.get(2)),
                () -> assertEquals(0, dealRepository.getPidsByChatIdAndStatus(Long.MAX_VALUE, DealStatus.NEW).size())
        );
    }

    @Test
    void getPaidDealsPids() {
        Map<DealStatus, List<Deal>> deals = new EnumMap<>(DealStatus.class);
        for (DealStatus dealStatus : DealStatus.values()) {
            int randomDealsCount = RandomUtils.nextInt(10);
            List<Deal> statusDeals = new ArrayList<>();
            for (int i = 0; i < randomDealsCount; i++) {
                statusDeals.add(dealRepository.save(Deal.builder().dealStatus(dealStatus).build()));
            }
            deals.put(dealStatus, statusDeals);
        }
    }

    @Test
    void dealsByUserChatIdIsExist() {
    }

    @Test
    void getWalletFromLastPassedByChatIdAndDealTypeAndCryptoCurrency() {
    }
}