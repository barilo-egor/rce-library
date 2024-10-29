package tgb.btc.library.repository.bot.deal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import tgb.btc.library.bean.BasePersist;
import tgb.btc.library.bean.bot.Deal;
import tgb.btc.library.bean.bot.PaymentType;
import tgb.btc.library.bean.bot.User;
import tgb.btc.library.constants.enums.bot.CryptoCurrency;
import tgb.btc.library.constants.enums.bot.DealStatus;
import tgb.btc.library.constants.enums.bot.DeliveryType;
import tgb.btc.library.constants.enums.bot.FiatCurrency;
import tgb.btc.library.repository.bot.DealRepository;
import tgb.btc.library.repository.bot.PaymentTypeRepository;
import tgb.btc.library.repository.bot.UserRepository;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class ModifyDealRepositoryTest {

    @Autowired
    private DealRepository dealRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private PaymentTypeRepository paymentTypeRepository;

    private Long dealPid;

    private final Long chatId = 87654321L;

    private User user;

    @BeforeEach
    void setUp() {
        user = userRepository.save(User.builder().chatId(chatId).isActive(true).isBanned(false)
                .registrationDate(LocalDateTime.now()).referralBalance(0).build());
        dealPid = dealRepository.save(Deal.builder().user(user).build()).getPid();
    }

    @Test
    @Transactional
    void updateCryptoCurrencyByPid() {
        Deal deal = dealRepository.findByPid(dealPid);
        assertNull(deal.getCryptoCurrency());
        dealRepository.updateCryptoCurrencyByPid(dealPid, CryptoCurrency.BITCOIN);
        entityManager.clear();
        assertEquals(CryptoCurrency.BITCOIN, dealRepository.findByPid(dealPid).getCryptoCurrency());
    }

    @Test
    void updateCryptoAmountByPid() {
        Deal deal = dealRepository.findByPid(dealPid);
        assertNull(deal.getCryptoAmount());
        dealRepository.updateCryptoAmountByPid(new BigDecimal("0.001"), dealPid);
        entityManager.clear();
        assertEquals(0, new BigDecimal("0.001").compareTo(dealRepository.findByPid(dealPid).getCryptoAmount()));
    }

    @Test
    void updateAmountByPid() {
        Deal deal = dealRepository.findByPid(dealPid);
        assertNull(deal.getAmount());
        dealRepository.updateAmountByPid(new BigDecimal(5000), dealPid);
        entityManager.clear();
        assertEquals(0, new BigDecimal(5000).compareTo(dealRepository.findByPid(dealPid).getAmount()));
    }

    @Test
    void updateDiscountByPid() {
        Deal deal = dealRepository.findByPid(dealPid);
        assertNull(deal.getDiscount());
        dealRepository.updateDiscountByPid(new BigDecimal("50.51"), dealPid);
        entityManager.clear();
        assertEquals(0, new BigDecimal("50.51").compareTo(dealRepository.findByPid(dealPid).getDiscount()));
    }

    @Test
    void updateCommissionByPid() {
        Deal deal = dealRepository.findByPid(dealPid);
        assertNull(deal.getCommission());
        dealRepository.updateCommissionByPid(new BigDecimal("350.2"), dealPid);
        entityManager.clear();
        assertEquals(0, new BigDecimal("350.2").compareTo(dealRepository.findByPid(dealPid).getCommission()));
    }

    @Test
    void updateUsedReferralDiscountByPid() {
        Deal deal = dealRepository.findByPid(dealPid);
        assertNull(deal.getUsedReferralDiscount());
        dealRepository.updateUsedReferralDiscountByPid(true, dealPid);
        entityManager.clear();
        assertTrue(dealRepository.findByPid(dealPid).getUsedReferralDiscount());
    }

    @Test
    void updateWalletByPid() {
        Deal deal = dealRepository.findByPid(dealPid);
        assertNull(deal.getWallet());
        dealRepository.updateWalletByPid("foo", dealPid);
        entityManager.clear();
        assertEquals("foo", dealRepository.findByPid(dealPid).getWallet());
    }

    @Test
    void updatePaymentTypeByPid() {
        Deal deal = dealRepository.findByPid(dealPid);
        assertNull(deal.getPaymentType());
        PaymentType paymentType = paymentTypeRepository.save(PaymentType.builder().build());
        dealRepository.updatePaymentTypeByPid(paymentType, dealPid);
        entityManager.clear();
        assertEquals(paymentType.getPid(), dealRepository.findByPid(dealPid).getPaymentType().getPid());
    }

    @Test
    void updateIsUsedPromoByPid() {
        Deal deal = dealRepository.findByPid(dealPid);
        assertNull(deal.getUsedPromo());
        dealRepository.updateIsUsedPromoByPid(false, dealPid);
        entityManager.clear();
        assertFalse(dealRepository.findByPid(dealPid).getUsedPromo());
    }

    @Test
    void updateDealStatusByPid() {
        Deal deal = dealRepository.findByPid(dealPid);
        assertNull(deal.getDealStatus());
        dealRepository.updateDealStatusByPid(DealStatus.CONFIRMED, dealPid);
        entityManager.clear();
        assertEquals(DealStatus.CONFIRMED, dealRepository.findByPid(dealPid).getDealStatus());
    }

    @Test
    void updateFiatCurrencyByPid() {
        Deal deal = dealRepository.findByPid(dealPid);
        assertNull(deal.getFiatCurrency());
        dealRepository.updateFiatCurrencyByPid(dealPid, FiatCurrency.BYN);
        entityManager.clear();
        assertEquals(FiatCurrency.BYN, dealRepository.findByPid(dealPid).getFiatCurrency());
    }

    @Test
    void updateIsPersonalAppliedByPid() {
        Deal deal = dealRepository.findByPid(dealPid);
        assertNull(deal.getPersonalApplied());
        dealRepository.updateIsPersonalAppliedByPid(dealPid, false);
        entityManager.clear();
        assertFalse(dealRepository.findByPid(dealPid).getPersonalApplied());
    }

    @Test
    void updatePaymentTypeToNullByPaymentTypePid() {
        Deal deal = dealRepository.findByPid(dealPid);
        assertNull(deal.getPersonalApplied());
        PaymentType paymentType = paymentTypeRepository.save(PaymentType.builder().build());
        dealRepository.updatePaymentTypeByPid(paymentType, dealPid);
        entityManager.clear();
        assertEquals(paymentType.getPid(), dealRepository.findByPid(dealPid).getPaymentType().getPid());
        dealRepository.updatePaymentTypeToNullByPaymentTypePid(paymentType.getPid());
        entityManager.clear();
        assertNull(dealRepository.findByPid(dealPid).getPaymentType());
    }

    @Test
    void updateAdditionalVerificationImageIdByPid() {
        Deal deal = dealRepository.findByPid(dealPid);
        assertNull(deal.getAdditionalVerificationImageId());
        dealRepository.updateAdditionalVerificationImageIdByPid(dealPid, "foo");
        entityManager.clear();
        assertEquals("foo", dealRepository.findByPid(dealPid).getAdditionalVerificationImageId());
    }

    @Test
    void updateDeliveryTypeByPid() {
        Deal deal = dealRepository.findByPid(dealPid);
        assertNull(deal.getDeliveryType());
        dealRepository.updateDeliveryTypeByPid(dealPid, DeliveryType.VIP);
        entityManager.clear();
        assertEquals(DeliveryType.VIP, dealRepository.findByPid(dealPid).getDeliveryType());
    }

    @Test
    void deleteByUser_ChatId() {
        dealRepository.deleteAll();
        User secondUser = userRepository.save(User.builder().chatId(12345678L).isActive(true).isBanned(false)
                .registrationDate(LocalDateTime.now()).referralBalance(0).build());
        dealRepository.save(Deal.builder().user(user).build());
        dealRepository.save(Deal.builder().user(user).build());
        dealRepository.save(Deal.builder().user(user).build());
        dealRepository.save(Deal.builder().user(secondUser).build());
        dealRepository.save(Deal.builder().user(secondUser).build());
        assertEquals(5, dealRepository.count());
        dealRepository.deleteByUser_ChatId(chatId);
        assertEquals(2, dealRepository.count());
        List<Deal> deals = dealRepository.findAll();
        assertAll(
                () -> assertEquals(12345678L, (long) deals.get(0).getUser().getChatId()),
                () -> assertEquals(12345678L, (long) deals.get(1).getUser().getChatId())
        );
    }

    @Test
    void deleteByPidIn() {
        dealRepository.deleteAll();
        List<Long> pidsToDelete = new ArrayList<>();
        List<Long> pidsExists = new ArrayList<>();
        pidsToDelete.add(dealRepository.save(Deal.builder().build()).getPid());
        pidsToDelete.add(dealRepository.save(Deal.builder().build()).getPid());
        pidsToDelete.add(dealRepository.save(Deal.builder().build()).getPid());
        pidsExists.add(dealRepository.save(Deal.builder().build()).getPid());
        pidsExists.add(dealRepository.save(Deal.builder().build()).getPid());
        pidsExists.add(dealRepository.save(Deal.builder().build()).getPid());
        pidsExists.add(dealRepository.save(Deal.builder().build()).getPid());
        assertEquals(7, dealRepository.count());
        dealRepository.deleteByPidIn(pidsToDelete);
        List<Deal> deals = dealRepository.findAll();
        assertNotNull(deals);
        assertEquals(4, deals.size());
        assertEquals(pidsExists, deals.stream().map(BasePersist::getPid).collect(Collectors.toList()));
        dealRepository.deleteByPidIn(List.of());
        assertEquals(4, dealRepository.count());
        dealRepository.deleteByPidIn(List.of(Long.MAX_VALUE, Long.MAX_VALUE - 1));
        assertEquals(4, dealRepository.count());
    }
}