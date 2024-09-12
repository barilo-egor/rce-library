package tgb.btc.library.service.bean.bot.deal.read;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tgb.btc.library.constants.enums.bot.*;
import tgb.btc.library.repository.bot.deal.read.DealPropertyRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DealPropertyServiceTest {

    @Mock
    private DealPropertyRepository dealPropertyRepository;

    @InjectMocks
    private DealPropertyService dealPropertyService;

    @Test
    void getCryptoCurrencyByPid() {
        CryptoCurrency expected = CryptoCurrency.BITCOIN;
        Long chatId = 12345678L;
        when(dealPropertyRepository.getCryptoCurrencyByPid(chatId)).thenReturn(expected);
        assertEquals(expected, dealPropertyService.getCryptoCurrencyByPid(chatId));
    }

    @Test
    void getCommissionByPid() {
        BigDecimal expected = new BigDecimal(1);
        Long chatId = 12345678L;
        when(dealPropertyRepository.getCommissionByPid(chatId)).thenReturn(expected);
        assertEquals(expected, dealPropertyService.getCommissionByPid(chatId));
    }

    @Test
    void getAmountByPid() {
        BigDecimal expected = new BigDecimal(1);
        Long chatId = 12345678L;
        when(dealPropertyRepository.getAmountByPid(chatId)).thenReturn(expected);
        assertEquals(expected, dealPropertyService.getAmountByPid(chatId));
    }

    @Test
    void getCryptoAmountByPid() {
        BigDecimal expected = new BigDecimal(1);
        Long chatId = 12345678L;
        when(dealPropertyRepository.getCryptoAmountByPid(chatId)).thenReturn(expected);
        assertEquals(expected, dealPropertyService.getCryptoAmountByPid(chatId));
    }

    @Test
    void getDiscountByPid() {
        BigDecimal expected = new BigDecimal(1);
        Long chatId = 12345678L;
        when(dealPropertyRepository.getDiscountByPid(chatId)).thenReturn(expected);
        assertEquals(expected, dealPropertyService.getDiscountByPid(chatId));
    }

    @Test
    void getDealTypeByPid() {
        DealType expected = DealType.BUY;
        Long chatId = 12345678L;
        when(dealPropertyRepository.getDealTypeByPid(chatId)).thenReturn(expected);
        assertEquals(expected, dealPropertyService.getDealTypeByPid(chatId));
    }

    @Test
    void getDateTimeByPid() {
        LocalDateTime expected = LocalDateTime.MIN;
        Long chatId = 12345678L;
        when(dealPropertyRepository.getDateTimeByPid(chatId)).thenReturn(expected);
        assertEquals(expected, dealPropertyService.getDateTimeByPid(chatId));
    }

    @Test
    void getFiatCurrencyByPid() {
        FiatCurrency expected = FiatCurrency.BYN;
        Long chatId = 12345678L;
        when(dealPropertyRepository.getFiatCurrencyByPid(chatId)).thenReturn(expected);
        assertEquals(expected, dealPropertyService.getFiatCurrencyByPid(chatId));
    }

    @Test
    void getAdditionalVerificationImageIdByPid() {
        String expected = "imageId";
        Long chatId = 12345678L;
        when(dealPropertyRepository.getAdditionalVerificationImageIdByPid(chatId)).thenReturn(expected);
        assertEquals(expected, dealPropertyService.getAdditionalVerificationImageIdByPid(chatId));
    }

    @Test
    void getDeliveryTypeByPid() {
        DeliveryType expected = DeliveryType.VIP;
        Long chatId = 12345678L;
        when(dealPropertyRepository.getDeliveryTypeByPid(chatId)).thenReturn(expected);
        assertEquals(expected, dealPropertyService.getDeliveryTypeByPid(chatId));
    }

    @Test
    void getCreditedAmountByPid() {
        BigDecimal expected = new BigDecimal(1);
        Long chatId = 12345678L;
        when(dealPropertyRepository.getCreditedAmountByPid(chatId)).thenReturn(expected);
        assertEquals(expected, dealPropertyService.getCreditedAmountByPid(chatId));
    }

    @Test
    void getDealStatusByPid() {
        DealStatus expected = DealStatus.CONFIRMED;
        Long chatId = 12345678L;
        when(dealPropertyRepository.getDealStatusByPid(chatId)).thenReturn(expected);
        assertEquals(expected, dealPropertyService.getDealStatusByPid(chatId));
    }

    @Test
    void getIsUsedPromoByPid() {
        Boolean expected = true;
        Long chatId = 12345678L;
        when(dealPropertyRepository.getIsUsedPromoByPid(chatId)).thenReturn(expected);
        assertEquals(expected, dealPropertyService.getIsUsedPromoByPid(chatId));
    }
}