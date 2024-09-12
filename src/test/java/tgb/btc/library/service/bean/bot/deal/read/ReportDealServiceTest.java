package tgb.btc.library.service.bean.bot.deal.read;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tgb.btc.library.constants.enums.bot.CryptoCurrency;
import tgb.btc.library.constants.enums.bot.DealType;
import tgb.btc.library.constants.enums.bot.FiatCurrency;
import tgb.btc.library.repository.bot.deal.read.ReportDealRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReportDealServiceTest {

    @Mock
    private ReportDealRepository reportDealRepository;

    @InjectMocks
    private ReportDealService reportDealService;

    @Test
    void getConfirmedCryptoAmountSum() {
        BigDecimal expected = new BigDecimal(1);
        when(reportDealRepository.getConfirmedCryptoAmountSum(any(), any(), any(), any())).thenReturn(expected);
        assertEquals(expected, reportDealService.getConfirmedCryptoAmountSum(
                DealType.BUY, LocalDateTime.MIN, LocalDateTime.MAX, CryptoCurrency.BITCOIN)
        );
        verify(reportDealRepository).getConfirmedCryptoAmountSum(
                DealType.BUY, LocalDateTime.MIN, LocalDateTime.MAX, CryptoCurrency.BITCOIN
        );
    }

    @Test
    void getConfirmedTotalAmountSum() {
        BigDecimal expected = new BigDecimal(1);
        when(reportDealRepository.getConfirmedTotalAmountSum(any(), any(), any(), any(), any())).thenReturn(expected);
        assertEquals(expected, reportDealService.getConfirmedTotalAmountSum(
                DealType.BUY, LocalDateTime.MIN, CryptoCurrency.BITCOIN, FiatCurrency.BYN)
        );
        verify(reportDealRepository).getConfirmedTotalAmountSum(
                DealType.BUY, LocalDateTime.MIN, LocalDateTime.MIN, CryptoCurrency.BITCOIN, FiatCurrency.BYN
        );
    }

    @Test
    void testGetConfirmedTotalAmountSum() {
        BigDecimal expected = new BigDecimal(1);
        when(reportDealRepository.getConfirmedTotalAmountSum(any(), any(), any(), any(), any())).thenReturn(expected);
        assertEquals(expected, reportDealService.getConfirmedTotalAmountSum(
                DealType.BUY, LocalDateTime.MIN, LocalDateTime.MAX, CryptoCurrency.BITCOIN, FiatCurrency.BYN)
        );
        verify(reportDealRepository).getConfirmedTotalAmountSum(
                DealType.BUY, LocalDateTime.MIN, LocalDateTime.MAX, CryptoCurrency.BITCOIN, FiatCurrency.BYN
        );
    }

    @Test
    void findAllForUsersReport() {
        List<Object[]> expected = new ArrayList<>();
        when(reportDealRepository.findAllForUsersReport()).thenReturn(expected);
        assertEquals(expected, reportDealService.findAllForUsersReport());
        verify(reportDealRepository).findAllForUsersReport();
    }
}