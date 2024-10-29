package tgb.btc.library.repository.bot;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import tgb.btc.library.bean.bot.Deal;
import tgb.btc.library.bean.bot.PaymentReceipt;
import tgb.btc.library.bean.bot.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ActiveProfiles("test")
class PaymentReceiptRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DealRepository dealRepository;

    @Autowired
    private PaymentReceiptRepository paymentReceiptRepository;

    @Test
    void getByDealsPids() {
        Long chatId = 12345678L;
        Set<PaymentReceipt> expected = new HashSet<>();
        User user = userRepository.save(User.builder().chatId(chatId).isActive(true).isBanned(false)
                .registrationDate(LocalDateTime.now()).referralBalance(0).build());
        List<Deal> deals = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            deals.add(dealRepository.save(Deal.builder().user(user).build()));
        }
        assertEquals(new ArrayList<>(), paymentReceiptRepository.getByDealsPids(user.getChatId()));
        for (Deal deal : deals) {
            expected.add(paymentReceiptRepository.save(PaymentReceipt.builder()
                    .deal(deal)
                    .build()));
        }
        assertEquals(expected, new HashSet<>(paymentReceiptRepository.getByDealsPids(user.getChatId())));
    }
}