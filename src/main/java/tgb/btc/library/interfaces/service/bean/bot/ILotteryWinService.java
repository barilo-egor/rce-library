package tgb.btc.library.interfaces.service.bean.bot;

public interface ILotteryWinService {

    Long getLotteryWinCount(Long chatId);

    void deleteByUser_ChatId(Long chatId);
}
