package tgb.btc.library.service.process;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import tgb.btc.api.web.INotifier;
import tgb.btc.library.bean.bot.Deal;
import tgb.btc.library.bean.bot.User;
import tgb.btc.library.constants.enums.ReferralType;
import tgb.btc.library.constants.enums.bot.FiatCurrency;
import tgb.btc.library.constants.enums.properties.VariableType;
import tgb.btc.library.constants.strings.BotMessages;
import tgb.btc.library.exception.BaseException;
import tgb.btc.library.interfaces.IModule;
import tgb.btc.library.service.bean.bot.user.ModifyUserService;
import tgb.btc.library.service.bean.bot.user.ReadUserService;
import tgb.btc.library.service.properties.VariablePropertiesReader;
import tgb.btc.library.service.util.BigDecimalService;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReferralServiceTest {

    @Mock
    private IModule<ReferralType> referralModule;

    @Mock
    private VariablePropertiesReader variablePropertiesReader;

    @Mock
    private ReadUserService readUserService;

    @Spy
    private BigDecimalService bigDecimalService;

    @Mock
    private CalculateService calculateService;

    @Mock
    private ModifyUserService modifyUserService;

    @Mock
    private INotifier notifier;

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
        when(variablePropertiesReader.isNotBlank(any())).thenReturn(true);

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
        when(variablePropertiesReader.isNotBlank(any())).thenReturn(true);

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
        when(variablePropertiesReader.isNotBlank(any())).thenReturn(true);
        referralService.processReferralDiscount(deal);
        verify(variablePropertiesReader, never()).getBigDecimal(any());
    }

    @Test
    void processReferralDiscountConvertTestBlankRubBynCourse() {
        Deal deal = new Deal();
        assertThrows(BaseException.class, () -> referralService.processReferralDiscount(deal));
    }

    @Test
    void processReferralDiscountConvertTestBlankBynRubCourse() {
        Deal deal = new Deal();
        when(variablePropertiesReader.isNotBlank("course.rub.byn")).thenReturn(true);
        assertThrows(BaseException.class, () -> referralService.processReferralDiscount(deal));
    }

    @Test
    void processReferralBonusWithoutConversionAndMainPercent() {
        BigDecimal referralPercent = new BigDecimal(1);
        Long fromChatId = 12345678L;

        User user = new User();
        user.setFromChatId(fromChatId);

        User referralUser = new User();
        referralUser.setReferralBalance(100);
        referralUser.setChatId(fromChatId);
        referralUser.setCharges(200);

        Deal deal = new Deal();
        deal.setAmount(new BigDecimal(100));
        deal.setUser(user);

        when(readUserService.findByChatId(fromChatId)).thenReturn(referralUser);
        when(readUserService.getReferralPercentByChatId(fromChatId)).thenReturn(null);
        when(variablePropertiesReader.getBigDecimal(VariableType.REFERRAL_PERCENT.getKey())).thenReturn(referralPercent);
        when(calculateService.getPercentsFactor(referralPercent)).thenReturn(new BigDecimal(0.02));
        when(referralModule.isCurrent(ReferralType.STANDARD)).thenReturn(false);

        referralService.processReferralBonus(deal);

        verify(variablePropertiesReader, never()).getBigDecimal("course.byn.rub");
        verify(modifyUserService).updateReferralBalanceByChatId(102, fromChatId);
        verify(notifier).sendNotify(fromChatId, String.format(BotMessages.FROM_REFERRAL_BALANCE_PURCHASE, 2));
        verify(modifyUserService).updateChargesByChatId(202, fromChatId);
    }

    @Test
    void processReferralBonusWithoutConversionAndPersonalPercent() {
        BigDecimal referralPercent = new BigDecimal(1);
        Long fromChatId = 12345678L;

        User user = new User();
        user.setFromChatId(fromChatId);

        User referralUser = new User();
        referralUser.setReferralBalance(100);
        referralUser.setChatId(fromChatId);
        referralUser.setCharges(200);

        Deal deal = new Deal();
        deal.setAmount(new BigDecimal(100));
        deal.setUser(user);

        when(readUserService.findByChatId(fromChatId)).thenReturn(referralUser);
        when(readUserService.getReferralPercentByChatId(fromChatId)).thenReturn(referralPercent);
        when(calculateService.getPercentsFactor(referralPercent)).thenReturn(new BigDecimal(0.05));
        when(referralModule.isCurrent(ReferralType.STANDARD)).thenReturn(false);

        referralService.processReferralBonus(deal);

        verify(calculateService).getPercentsFactor(referralPercent);
    }

    @Test
    void processReferralBonusWithStandardTypeAndRub() {
        BigDecimal referralPercent = new BigDecimal(1);
        Long fromChatId = 12345678L;

        User user = new User();
        user.setFromChatId(fromChatId);

        User referralUser = new User();
        referralUser.setReferralBalance(100);
        referralUser.setChatId(fromChatId);
        referralUser.setCharges(200);

        Deal deal = new Deal();
        deal.setAmount(new BigDecimal(100));
        deal.setUser(user);
        deal.setFiatCurrency(FiatCurrency.RUB);

        when(readUserService.findByChatId(fromChatId)).thenReturn(referralUser);
        when(readUserService.getReferralPercentByChatId(fromChatId)).thenReturn(referralPercent);
        when(calculateService.getPercentsFactor(referralPercent)).thenReturn(new BigDecimal(0.05));
        when(referralModule.isCurrent(ReferralType.STANDARD)).thenReturn(true);

        referralService.processReferralBonus(deal);

        verify(variablePropertiesReader, never()).getBigDecimal("course.byn.rub");
    }

    @Test
    void processReferralBonusWithoutRubBynCourse() {
        BigDecimal referralPercent = new BigDecimal(1);
        Long fromChatId = 12345678L;

        User user = new User();
        user.setFromChatId(fromChatId);

        User referralUser = new User();
        referralUser.setChatId(fromChatId);

        Deal deal = new Deal();
        deal.setAmount(new BigDecimal(100));
        deal.setUser(user);
        deal.setFiatCurrency(FiatCurrency.BYN);

        when(readUserService.findByChatId(fromChatId)).thenReturn(referralUser);
        when(readUserService.getReferralPercentByChatId(fromChatId)).thenReturn(referralPercent);
        when(calculateService.getPercentsFactor(referralPercent)).thenReturn(new BigDecimal(0.05));
        when(referralModule.isCurrent(ReferralType.STANDARD)).thenReturn(true);
        when(variablePropertiesReader.isNotBlank("course.rub.byn")).thenReturn(false);

        assertThrows(BaseException.class, () -> referralService.processReferralBonus(deal));
    }

    @Test
    void processReferralBonusWithoutBynRubCourse() {
        BigDecimal referralPercent = new BigDecimal(1);
        Long fromChatId = 12345678L;

        User user = new User();
        user.setFromChatId(fromChatId);

        User referralUser = new User();
        referralUser.setChatId(fromChatId);

        Deal deal = new Deal();
        deal.setAmount(new BigDecimal(100));
        deal.setUser(user);
        deal.setFiatCurrency(FiatCurrency.BYN);

        when(readUserService.findByChatId(fromChatId)).thenReturn(referralUser);
        when(readUserService.getReferralPercentByChatId(fromChatId)).thenReturn(referralPercent);
        when(calculateService.getPercentsFactor(referralPercent)).thenReturn(new BigDecimal(0.05));
        when(referralModule.isCurrent(ReferralType.STANDARD)).thenReturn(true);
        when(variablePropertiesReader.isNotBlank("course.rub.byn")).thenReturn(true);
        when(variablePropertiesReader.isNotBlank("course.byn.rub")).thenReturn(false);

        assertThrows(BaseException.class, () -> referralService.processReferralBonus(deal));
    }

    @Test
    void processReferralBonusWithConversionAndMainPercent() {
        BigDecimal referralPercent = new BigDecimal(1);
        Long fromChatId = 12345678L;

        User user = new User();
        user.setFromChatId(fromChatId);

        User referralUser = new User();
        referralUser.setReferralBalance(100);
        referralUser.setChatId(fromChatId);
        referralUser.setCharges(200);

        Deal deal = new Deal();
        deal.setAmount(new BigDecimal(100));
        deal.setUser(user);
        deal.setFiatCurrency(FiatCurrency.BYN);

        when(readUserService.findByChatId(fromChatId)).thenReturn(referralUser);
        when(readUserService.getReferralPercentByChatId(fromChatId)).thenReturn(referralPercent);
        when(calculateService.getPercentsFactor(referralPercent)).thenReturn(new BigDecimal("0.02"));
        when(referralModule.isCurrent(ReferralType.STANDARD)).thenReturn(true);
        when(variablePropertiesReader.isNotBlank(any())).thenReturn(true);
        when(variablePropertiesReader.getBigDecimal("course.byn.rub")).thenReturn(new BigDecimal("0.1"));

        referralService.processReferralBonus(deal);

        verify(modifyUserService).updateReferralBalanceByChatId(120, fromChatId);
        verify(notifier).sendNotify(fromChatId, String.format(BotMessages.FROM_REFERRAL_BALANCE_PURCHASE, 20));
        verify(modifyUserService).updateChargesByChatId(220, fromChatId);
    }

}