package tgb.btc.library.service.bean.bot.deal.read;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tgb.btc.library.repository.bot.deal.read.DealUserRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DealUserServiceTest {

    @Mock
    private DealUserRepository dealUserRepository;

    @InjectMocks
    private DealUserService dealUserService;

    @Test
    void getUserChatIdByDealPid() {
        Long expected = 12345678L;
        Long pid = 10000L;
        when(dealUserRepository.getUserChatIdByDealPid(pid)).thenReturn(expected);
        assertEquals(expected, dealUserService.getUserChatIdByDealPid(pid));
    }

    @Test
    void getUserUsernameByDealPid() {
        String expected = "username";
        Long pid = 10000L;
        when(dealUserRepository.getUserUsernameByDealPid(pid)).thenReturn(expected);
        assertEquals(expected, dealUserService.getUserUsernameByDealPid(pid));
    }
}