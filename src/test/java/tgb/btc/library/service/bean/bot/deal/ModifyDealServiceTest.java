package tgb.btc.library.service.bean.bot.deal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import tgb.btc.library.bean.bot.Deal;
import tgb.btc.library.bean.bot.PaymentType;
import tgb.btc.library.bean.bot.User;
import tgb.btc.library.constants.enums.CreateType;
import tgb.btc.library.constants.enums.bot.*;
import tgb.btc.library.constants.enums.strings.BotMessageConst;
import tgb.btc.library.repository.bot.deal.ModifyDealRepository;
import tgb.btc.library.service.bean.bot.PaymentRequisiteService;
import tgb.btc.library.service.bean.bot.SecurePaymentDetailsService;
import tgb.btc.library.service.bean.bot.deal.read.DealUserService;
import tgb.btc.library.service.bean.bot.user.ModifyUserService;
import tgb.btc.library.service.bean.bot.user.ReadUserService;
import tgb.btc.library.service.process.BanningUserService;
import tgb.btc.library.service.process.LotteryService;
import tgb.btc.library.service.process.ReferralService;
import tgb.btc.library.service.schedule.DealDeleteScheduler;
import tgb.btc.library.service.stub.NotifierStub;
import tgb.btc.library.service.stub.ReviewPriseProcessServiceStub;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ModifyDealServiceTest {

    @Mock
    private ModifyDealRepository modifyDealRepository;

    @Mock
    private ModifyUserService modifyUserService;

    @Mock
    private ReadUserService readUserService;

    @Mock
    private DealUserService dealUserService;

    @Mock
    private BanningUserService banningUserService;

    @Mock
    private DealDeleteScheduler dealDeleteScheduler;

    @Mock
    private ReadDealService readDealService;

    @Mock
    private ReferralService referralService;

    @Mock
    private SecurePaymentDetailsService securePaymentDetailsService;

    @Mock
    private PaymentRequisiteService paymentRequisiteService;

    @Mock
    private LotteryService lotteryService;

    @Mock
    private ReviewPriseProcessServiceStub reviewPriseProcessServiceStub;

    @Spy
    private NotifierStub notifierStub;

    @InjectMocks
    private ModifyDealService modifyDealService;

    @Test
    void createNewDeal() {
        Long chatId = 12345678L;
        User expectedUser = new User();
        expectedUser.setPid(1L);
        when(readUserService.findByChatId(chatId)).thenReturn(expectedUser);

        LocalDateTime expectedTime = LocalDateTime.of(2000, 1, 1, 1, 0);
        try (MockedStatic<LocalDateTime> mockedStatic = Mockito.mockStatic(LocalDateTime.class)) {
            mockedStatic.when(LocalDateTime::now).thenReturn(expectedTime);
        }

        Deal expectedDeal = new Deal();
        Long dealPid = 1L;
        expectedDeal.setPid(dealPid);
        expectedDeal.setDealStatus(DealStatus.NEW);
        expectedDeal.setCreateType(CreateType.BOT);
        expectedDeal.setDateTime(expectedTime);
        expectedDeal.setDealType(DealType.BUY);
        expectedDeal.setUser(expectedUser);
        when(modifyDealRepository.save(any())).thenReturn(expectedDeal);
        Deal actual = modifyDealService.createNewDeal(DealType.BUY, chatId);
        assertEquals(expectedDeal, actual);
        verify(modifyUserService).updateCurrentDealByChatId(dealPid, chatId);
    }

    @Test
    void deleteDealAndBan() {
        Long chatId = 12345678L;
        Long dealPid = 1L;
        when(dealUserService.getUserChatIdByDealPid(anyLong())).thenReturn(chatId);

        modifyDealService.deleteDeal(dealPid, true);
        verify(modifyDealRepository).deleteById(dealPid);
        verify(banningUserService).ban(chatId);
        verify(dealDeleteScheduler).deleteDeal(dealPid);
    }

    @Test
    void deleteDealAndNotBan() {
        Long chatId = 12345678L;
        Long dealPid = 1L;
        when(dealUserService.getUserChatIdByDealPid(anyLong())).thenReturn(chatId);

        modifyDealService.deleteDeal(dealPid, false);
        verify(modifyDealRepository).deleteById(dealPid);
        verify(banningUserService, never()).ban(any());
        verify(dealDeleteScheduler).deleteDeal(dealPid);
    }

    @Test
    void confirmBuyWithDiscountsAndPaymentType() {
        Long dealPid = 1L;
        Deal deal = new Deal();
        deal.setPid(dealPid);
        deal.setIsUsedReferralDiscount(true);
        deal.setDealType(DealType.BUY);
        deal.setCryptoCurrency(CryptoCurrency.BITCOIN);
        String wallet = "wallet";
        deal.setWallet(wallet);
        PaymentType paymentType = new PaymentType();
        Long paymentTypePid = 2L;
        paymentType.setPid(paymentTypePid);
        deal.setPaymentType(paymentType);
        User user = new User();
        user.setFromChatId(87654321L);
        Long chatId = 12345678L;
        user.setChatId(chatId);
        deal.setUser(user);
        when(readDealService.findByPid(dealPid)).thenReturn(deal);
        when(securePaymentDetailsService.hasAccessToPaymentTypes(eq(chatId), any())).thenReturn(true);

        modifyDealService.confirm(dealPid);

        verify(referralService).processReferralDiscount(deal);
        ArgumentCaptor<Deal> dealArgumentCaptor = ArgumentCaptor.forClass(Deal.class);
        verify(modifyDealRepository).save(dealArgumentCaptor.capture());
        Deal actualDeal = dealArgumentCaptor.getValue();
        assertEquals(DealStatus.CONFIRMED, actualDeal.getDealStatus());
        verify(referralService).processReferralBonus(deal);
        verify(dealDeleteScheduler).deleteDeal(dealPid);
        verify(paymentRequisiteService).updateOrder(paymentTypePid);
        verify(notifierStub).sendNotify(chatId, String.format(CryptoCurrency.BITCOIN.getSendMessage(), wallet));
        verify(reviewPriseProcessServiceStub).processReviewPrise(dealPid);
    }

    @Test
    void confirmSellWithoutDiscountsAndPaymentType() {
        Long dealPid = 1L;
        Deal deal = new Deal();
        deal.setPid(dealPid);
        deal.setIsUsedReferralDiscount(false);
        deal.setDealType(DealType.SELL);
        User user = new User();
        Long chatId = 12345678L;
        user.setChatId(chatId);
        deal.setUser(user);

        when(readDealService.findByPid(dealPid)).thenReturn(deal);
        when(securePaymentDetailsService.hasAccessToPaymentTypes(chatId, deal.getFiatCurrency())).thenReturn(false);

        modifyDealService.confirm(dealPid);

        verify(paymentRequisiteService, never()).updateOrder(anyLong());
        verify(referralService, never()).processReferralBonus(deal);
        verify(notifierStub).sendNotify(chatId, BotMessageConst.DEAL_CONFIRMED.getMessage());
    }

    @Test
    void updateCryptoCurrencyByPid() {
        Long pid = 1L;
        CryptoCurrency cryptoCurrency = CryptoCurrency.BITCOIN;
        modifyDealService.updateCryptoCurrencyByPid(pid, cryptoCurrency);
        verify(modifyDealRepository).updateCryptoCurrencyByPid(pid, cryptoCurrency);
    }

    @Test
    void updateCryptoAmountByPid() {
        Long pid = 1L;
        BigDecimal cryptoAmount = new BigDecimal("0.01");
        modifyDealService.updateCryptoAmountByPid(cryptoAmount, pid);
        verify(modifyDealRepository).updateCryptoAmountByPid(cryptoAmount, pid);
    }

    @Test
    void updateAmountByPid() {
        Long pid = 1L;
        BigDecimal amount = new BigDecimal(1000);
        modifyDealService.updateAmountByPid(amount, pid);
        verify(modifyDealRepository).updateAmountByPid(amount, pid);
    }

    @Test
    void updateDiscountByPid() {
        Long pid = 1L;
        BigDecimal discount = new BigDecimal(1000);
        modifyDealService.updateDiscountByPid(discount, pid);
        verify(modifyDealRepository).updateDiscountByPid(discount, pid);
    }

    @Test
    void updateCommissionByPid() {
        Long pid = 1L;
        BigDecimal commission = new BigDecimal(2);
        modifyDealService.updateCommissionByPid(commission, pid);
        verify(modifyDealRepository).updateCommissionByPid(commission, pid);
    }

    @Test
    void updateUsedReferralDiscountByPid() {
        Long pid = 1L;
        Boolean usedReferralDiscount = true;
        modifyDealService.updateUsedReferralDiscountByPid(usedReferralDiscount, pid);
        verify(modifyDealRepository).updateUsedReferralDiscountByPid(usedReferralDiscount, pid);
    }

    @Test
    void updateWalletByPid() {
        Long pid = 1L;
        String wallet = "wallet";
        modifyDealService.updateWalletByPid(wallet, pid);
        verify(modifyDealRepository).updateWalletByPid(wallet, pid);
    }

    @Test
    void updatePaymentTypeByPid() {
        Long pid = 1L;
        PaymentType paymentType = new PaymentType();
        modifyDealService.updatePaymentTypeByPid(paymentType, pid);
        verify(modifyDealRepository).updatePaymentTypeByPid(paymentType, pid);
    }

    @Test
    void updateIsUsedPromoByPid() {
        Long pid = 1L;
        Boolean usedPromo = false;
        modifyDealService.updateIsUsedPromoByPid(usedPromo, pid);
        verify(modifyDealRepository).updateIsUsedPromoByPid(usedPromo, pid);
    }

    @Test
    void updateDealStatusByPid() {
        Long pid = 1L;
        DealStatus dealStatus = DealStatus.PAID;
        modifyDealService.updateDealStatusByPid(dealStatus, pid);
        verify(modifyDealRepository).updateDealStatusByPid(dealStatus, pid);
    }

    @Test
    void updateFiatCurrencyByPid() {
        Long pid = 1L;
        FiatCurrency fiatCurrency = FiatCurrency.BYN;
        modifyDealService.updateFiatCurrencyByPid(pid, fiatCurrency);
        verify(modifyDealRepository).updateFiatCurrencyByPid(pid, fiatCurrency);
    }

    @Test
    void updateIsPersonalAppliedByPid() {
        Long pid = 1L;
        Boolean isPersonalApplied = false;
        modifyDealService.updateIsPersonalAppliedByPid(pid, isPersonalApplied);
        verify(modifyDealRepository).updateIsPersonalAppliedByPid(pid, isPersonalApplied);
    }

    @Test
    void updatePaymentTypeToNullByPaymentTypePid() {
        Long pid = 2L;
        modifyDealService.updatePaymentTypeToNullByPaymentTypePid(pid);
        verify(modifyDealRepository).updatePaymentTypeToNullByPaymentTypePid(pid);
    }

    @Test
    void updateAdditionalVerificationImageIdByPid() {
        Long pid = 1L;
        String imageId = "imageId";
        modifyDealService.updateAdditionalVerificationImageIdByPid(pid, imageId);
        verify(modifyDealRepository).updateAdditionalVerificationImageIdByPid(pid, imageId);
    }

    @Test
    void updateDeliveryTypeByPid() {
        Long pid = 1L;
        DeliveryType deliveryType = DeliveryType.STANDARD;
        modifyDealService.updateDeliveryTypeByPid(pid, deliveryType);
        verify(modifyDealRepository).updateDeliveryTypeByPid(pid, deliveryType);
    }

    @Test
    void deleteByUser_ChatId() {
        Long chatId = 12345678L;
        modifyDealService.deleteByUser_ChatId(chatId);
        verify(modifyDealRepository).deleteByUser_ChatId(chatId);
    }

    @Test
    void deleteByPidIn() {
        List<Long> pids = List.of(1L, 2L, 3L);
        modifyDealService.deleteByPidIn(pids);
        verify(modifyDealRepository).deleteByPidIn(pids);
    }
}