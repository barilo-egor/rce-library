package tgb.btc.library.service.bean.bot.deal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import tgb.btc.library.bean.bot.Deal;
import tgb.btc.library.bean.bot.User;
import tgb.btc.library.constants.enums.CreateType;
import tgb.btc.library.constants.enums.bot.DealStatus;
import tgb.btc.library.constants.enums.bot.DealType;
import tgb.btc.library.repository.bot.deal.ModifyDealRepository;
import tgb.btc.library.service.bean.bot.deal.read.DealUserService;
import tgb.btc.library.service.bean.bot.user.ModifyUserService;
import tgb.btc.library.service.bean.bot.user.ReadUserService;
import tgb.btc.library.service.process.BanningUserService;
import tgb.btc.library.service.schedule.DealDeleteScheduler;

import java.time.LocalDateTime;

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
    void confirmWithUsedReferralDiscountTest() {

    }

    @Test
    void updateCryptoCurrencyByPid() {
    }

    @Test
    void updateCryptoAmountByPid() {
    }

    @Test
    void updateAmountByPid() {
    }

    @Test
    void updateDiscountByPid() {
    }

    @Test
    void updateCommissionByPid() {
    }

    @Test
    void updateUsedReferralDiscountByPid() {
    }

    @Test
    void updateWalletByPid() {
    }

    @Test
    void updatePaymentTypeByPid() {
    }

    @Test
    void updateIsUsedPromoByPid() {
    }

    @Test
    void updateDealStatusByPid() {
    }

    @Test
    void updateFiatCurrencyByPid() {
    }

    @Test
    void updateIsPersonalAppliedByPid() {
    }

    @Test
    void updatePaymentTypeToNullByPaymentTypePid() {
    }

    @Test
    void updateAdditionalVerificationImageIdByPid() {
    }

    @Test
    void updateDeliveryTypeByPid() {
    }

    @Test
    void deleteByUser_ChatId() {
    }

    @Test
    void deleteByPidIn() {
    }
}