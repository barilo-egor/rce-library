package tgb.btc.library.service.bean.bot.deal.read;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tgb.btc.library.repository.bot.deal.read.DateDealRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DateDealServiceTest {

    @Mock
    private DateDealRepository dateDealRepository;

    @InjectMocks
    private DateDealService dateDealService;

    @Test
    void getConfirmedByDateTimeBetween() {
        when(dateDealRepository.getConfirmedByDateTimeBetween(any(), any())).thenReturn(new ArrayList<>());
        dateDealService.getConfirmedByDateTimeBetween(LocalDateTime.MIN, LocalDateTime.MAX);
        verify(dateDealRepository).getConfirmedByDateTimeBetween(LocalDateTime.MIN, LocalDateTime.MAX);
    }

    @Test
    void getConfirmedByDateBetween() {
        when(dateDealRepository.getConfirmedByDateTimeBetween(any(), any())).thenReturn(new ArrayList<>());
        LocalDate start = LocalDate.of(2000, 1 ,1);
        LocalDate end = LocalDate.of(2000, 2 ,5);
        dateDealService.getConfirmedByDateBetween(start, end);
        verify(dateDealRepository).getConfirmedByDateTimeBetween(
                LocalDateTime.of(2000, 1, 1, 0, 0),
                LocalDateTime.of(2000, 2, 6, 0, 0)
        );
    }

    @Test
    void testGetConfirmedByDateTimeBetween() {
        when(dateDealRepository.getConfirmedByDateTimeBetween(any(), any())).thenReturn(new ArrayList<>());
        dateDealService.getConfirmedByDateTimeBetween(LocalDateTime.of(2000, 1, 1, 13, 5));
        verify(dateDealRepository).getConfirmedByDateTimeBetween(
                LocalDateTime.of(2000, 1, 1, 0, 0),
                LocalDateTime.of(2000, 1, 2, 0, 0)
        );
    }

    @Test
    void testGetConfirmedByDateBetween() {
        when(dateDealRepository.getConfirmedByDateTimeBetween(any(), any())).thenReturn(new ArrayList<>());
        dateDealService.getConfirmedByDateBetween(LocalDate.of(2000, 1 ,1));
        verify(dateDealRepository).getConfirmedByDateTimeBetween(
                LocalDateTime.of(2000, 1, 1, 0, 0),
                LocalDateTime.of(2000, 1, 2, 0, 0)
        );
    }
}