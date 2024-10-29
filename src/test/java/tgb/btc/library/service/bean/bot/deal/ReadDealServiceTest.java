package tgb.btc.library.service.bean.bot.deal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tgb.btc.library.bean.bot.Deal;
import tgb.btc.library.bean.bot.PaymentReceipt;
import tgb.btc.library.constants.enums.bot.CryptoCurrency;
import tgb.btc.library.constants.enums.bot.DealStatus;
import tgb.btc.library.constants.enums.bot.DealType;
import tgb.btc.library.repository.bot.deal.ReadDealRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReadDealServiceTest {

    @Mock
    private ReadDealRepository readDealRepository;

    @InjectMocks
    private ReadDealService readDealService;

    @Test
    void findByPid() {
        Long pid = 1L;
        readDealService.findByPid(pid);
        verify(readDealRepository).findByPid(pid);
    }

    @Test
    void getDealsByPids() {
        List<Long> pids = List.of(1L, 2L, 3L);
        readDealService.getDealsByPids(pids);
        verify(readDealRepository).getDealsByPids(pids);
    }

    @Test
    void getPidsByChatIdAndStatus() {
        Long chatId = 1L;
        DealStatus status = DealStatus.VERIFICATION_REJECTED;
        readDealService.getPidsByChatIdAndStatus(chatId, status);
        verify(readDealRepository).getPidsByChatIdAndStatus(chatId, status);
    }

    @Test
    void getPaidDealsPids() {
        readDealService.getPaidDealsPids();
        verify(readDealRepository).getPaidDealsPids();
    }

    @Test
    void dealsByUserChatIdIsExist() {
        Long chatId = 1L;
        DealStatus status = DealStatus.CONFIRMED;
        Long countDeals = 5L;
        readDealService.dealsByUserChatIdIsExist(chatId, status, countDeals);
        readDealRepository.dealsByUserChatIdIsExist(chatId, status, countDeals);
    }

    @Test
    void getWalletFromLastPassedByChatIdAndDealTypeAndCryptoCurrency() {
        Long chatId = 1L;
        DealType dealType = DealType.BUY;
        CryptoCurrency cryptoCurrency = CryptoCurrency.LITECOIN;
        readDealService.getWalletFromLastPassedByChatIdAndDealTypeAndCryptoCurrency(chatId, dealType, cryptoCurrency);
        verify(readDealRepository).getWalletFromLastPassedByChatIdAndDealTypeAndCryptoCurrency(chatId, dealType, cryptoCurrency);
    }

    @Test
    void getPaymentReceipts() {
        Long pid = 1L;
        Deal deal = new Deal();
        deal.setPid(pid);
        List<PaymentReceipt> expected = List.of(
                PaymentReceipt.builder().deal(deal).build(),
                PaymentReceipt.builder().deal(deal).build()
        );
        deal.setPaymentReceipts(expected);
        when(readDealRepository.findByPid(pid)).thenReturn(deal);
        List<PaymentReceipt> actual = readDealService.getPaymentReceipts(pid);
        assertNotSame(expected, actual);
        assertEquals(expected, actual);
    }
}