package tgb.btc.library.service.process;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import tgb.btc.library.bean.bot.User;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class LotteryServiceTest {

    private LotteryService lotteryService = new LotteryService();

    @Test
    void addLottery() {
        User user = new User();
        user.setLotteryCount(5);
        lotteryService.addLottery(user);
        assertEquals(6, user.getLotteryCount());
        lotteryService.addLottery(user);
        assertEquals(7, user.getLotteryCount());
    }

    @Test
    void addFirstLottery() {
        User user = new User();
        lotteryService.addLottery(user);
        assertEquals(1, user.getLotteryCount());
    }
}