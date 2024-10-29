package tgb.btc.library.service.bean.bot.deal.read;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tgb.btc.library.constants.enums.bot.CryptoCurrency;
import tgb.btc.library.constants.enums.bot.DealStatus;
import tgb.btc.library.constants.enums.bot.DealType;
import tgb.btc.library.repository.bot.deal.read.DealCountRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DealCountServiceTest {

    @Mock
    private DealCountRepository dealCountRepository;

    @InjectMocks
    private DealCountService dealCountService;

    @Test
    void getCountDealByChatIdAndNotInDealStatus() {
        when(dealCountRepository.getCountDealByChatIdAndNotInDealStatus(anyLong(), anyList())).thenReturn(1);
        Long chatId = 12345678L;
        List<DealStatus> statuses = List.of(DealStatus.CONFIRMED, DealStatus.NEW);
        dealCountService.getCountDealByChatIdAndNotInDealStatus(chatId, statuses);
        verify(dealCountRepository).getCountDealByChatIdAndNotInDealStatus(chatId, statuses);
    }

    @Test
    void getCountConfirmedByUserChatId() {
        when(dealCountRepository.getCountByDealStatusAndChatId(anyLong(), any())).thenReturn(1L);
        Long chatId = 12345678L;
        dealCountService.getCountConfirmedByUserChatId(chatId);
        verify(dealCountRepository).getCountByDealStatusAndChatId(chatId, DealStatus.CONFIRMED);
    }

    @Test
    void getConfirmedDealsCountByUserChatIdAndDealTypeAndCryptoCurrency() {
        when(dealCountRepository.getDealsCountByUserChatIdAndDealStatusAndDealTypeAndCryptoCurrency(anyLong(), any(), any(), any()))
                .thenReturn(1L);
        Long chatId = 12345678L;
        DealType dealType = DealType.BUY;
        CryptoCurrency cryptoCurrency = CryptoCurrency.BITCOIN;
        dealCountService.getConfirmedDealsCountByUserChatIdAndDealTypeAndCryptoCurrency(chatId, dealType, cryptoCurrency);
        verify(dealCountRepository)
                .getDealsCountByUserChatIdAndDealStatusAndDealTypeAndCryptoCurrency(chatId, DealStatus.CONFIRMED, dealType, cryptoCurrency);
    }

    @Test
    void getCountConfirmedByDateTimeBetween() {
        when(dealCountRepository.getCountByDateTimeBetweenAndDealStatus(any(), any(), any()))
                .thenReturn(1);
        dealCountService.getCountConfirmedByDateTimeBetween(LocalDateTime.MIN, LocalDateTime.MAX);
        verify(dealCountRepository).getCountByDateTimeBetweenAndDealStatus(LocalDateTime.MIN, LocalDateTime.MAX, DealStatus.CONFIRMED);
    }
}