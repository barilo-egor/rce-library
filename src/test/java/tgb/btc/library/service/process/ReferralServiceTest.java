package tgb.btc.library.service.process;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tgb.btc.library.bean.bot.Deal;
import tgb.btc.library.bean.bot.User;
import tgb.btc.library.constants.enums.ReferralType;
import tgb.btc.library.constants.enums.bot.FiatCurrency;
import tgb.btc.library.interfaces.IModule;
import tgb.btc.library.service.properties.VariablePropertiesReader;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReferralServiceTest {

    @Mock
    private IModule<ReferralType> referralModule;

    @Mock
    private VariablePropertiesReader variablePropertiesReader;

    @InjectMocks
    private ReferralService referralService;

    @Test
    void processReferralDiscountWithoutConvertAndBalanceMoreThanPrice() {
        Deal deal = new Deal();
        User user = new User();
        user.setReferralBalance(1000);
        deal.setUser(user);
        deal.setOriginalPrice(new BigDecimal(2000));

        when(referralModule.isCurrent(ReferralType.STANDARD)).thenReturn(false);

        referralService.processReferralDiscount(deal);
        assertAll(
                () -> assertEquals(0, deal.getUser().getReferralBalance()),
                () -> assertEquals(0, new BigDecimal(1000).compareTo(deal.getAmount()))
        );
    }

    @Test
    void processReferralDiscountWithoutConvertAndBalanceLessThanPrice() {
        Deal deal = new Deal();
        User user = new User();
        user.setReferralBalance(3000);
        deal.setUser(user);
        deal.setOriginalPrice(new BigDecimal(1500));

        when(referralModule.isCurrent(ReferralType.STANDARD)).thenReturn(false);

        referralService.processReferralDiscount(deal);
        assertAll(
                () -> assertEquals(1500, deal.getUser().getReferralBalance()),
                () -> assertEquals(0, BigDecimal.ZERO.compareTo(deal.getAmount()))
        );
    }

    @Test
    void processReferralDiscountWithConvertAndBalanceMoreThanPrice() {
        Deal deal = new Deal();
        User user = new User();
        user.setReferralBalance(1000);
        deal.setUser(user);
        deal.setFiatCurrency(FiatCurrency.BYN);
        deal.setOriginalPrice(new BigDecimal(200));

        when(referralModule.isCurrent(ReferralType.STANDARD)).thenReturn(true);
        when(variablePropertiesReader.isNotBlank(any())).thenReturn(true);
        when(variablePropertiesReader.getBigDecimal("course.rub.byn")).thenReturn(new BigDecimal("0.1"));

        referralService.processReferralDiscount(deal);
        assertAll(
                () -> assertEquals(0, deal.getUser().getReferralBalance()),
                () -> assertEquals(0, new BigDecimal(100).compareTo(deal.getAmount()))
        );
    }

    @Test
    void processReferralDiscountWithConvertAndBalanceLessThanPrice() {
        Deal deal = new Deal();
        User user = new User();
        user.setReferralBalance(1000);
        deal.setUser(user);
        deal.setFiatCurrency(FiatCurrency.BYN);
        deal.setOriginalPrice(new BigDecimal(50));

        when(referralModule.isCurrent(ReferralType.STANDARD)).thenReturn(true);
        when(variablePropertiesReader.isNotBlank(any())).thenReturn(true);
        when(variablePropertiesReader.getBigDecimal(any())).thenReturn(new BigDecimal("0.1"));

        referralService.processReferralDiscount(deal);
        assertAll(
                () -> assertEquals(500, deal.getUser().getReferralBalance()),
                () -> assertEquals(0, BigDecimal.ZERO.compareTo(deal.getAmount()))
        );
    }

    @Test
    void processReferralDiscountConvertTestNotBynFiat() {
        Deal deal = new Deal();
        User user = new User();
        user.setReferralBalance(1000);
        deal.setUser(user);
        deal.setFiatCurrency(FiatCurrency.RUB);
        deal.setOriginalPrice(new BigDecimal(50));

        when(referralModule.isCurrent(ReferralType.STANDARD)).thenReturn(true);
        referralService.processReferralDiscount(deal);
        verify(variablePropertiesReader, never()).isNotBlank(any());
        verify(variablePropertiesReader, never()).getBigDecimal(any());
    }

    @Test
    void processReferralDiscountConvertTestBlankCourse() {
        Deal deal = new Deal();
        User user = new User();
        user.setReferralBalance(1000);
        deal.setUser(user);
        deal.setFiatCurrency(FiatCurrency.BYN);
        deal.setOriginalPrice(new BigDecimal(50));

        when(referralModule.isCurrent(ReferralType.STANDARD)).thenReturn(true);

        referralService.processReferralDiscount(deal);
        verify(variablePropertiesReader, never()).getBigDecimal(any());
    }
}