package tgb.btc.library.interfaces.service.bean.bot;

import tgb.btc.library.bean.bot.LotteryWin;
import tgb.btc.library.interfaces.service.IBasePersistService;

public interface ILotteryWinService extends IBasePersistService<LotteryWin> {

    Long getLotteryWinCount(Long chatId);

    void deleteByUser_ChatId(Long chatId);
}
