package tgb.btc.library.service.process;

import org.springframework.stereotype.Service;
import tgb.btc.library.bean.bot.User;
import tgb.btc.library.interfaces.service.process.ILotteryService;

import java.util.Objects;

@Service
public class LotteryService implements ILotteryService {

    @Override
    public void addLottery(User user) {
        if (Objects.nonNull(user.getLotteryCount())) {
            user.setLotteryCount(user.getLotteryCount() + 1);
        } else {
            user.setLotteryCount(1);
        }
    }
}
