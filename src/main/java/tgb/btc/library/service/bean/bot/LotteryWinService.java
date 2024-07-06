package tgb.btc.library.service.bean.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tgb.btc.library.bean.bot.LotteryWin;
import tgb.btc.library.interfaces.service.bean.bot.ILotteryWinService;
import tgb.btc.library.repository.BaseRepository;
import tgb.btc.library.repository.bot.LotteryWinRepository;
import tgb.btc.library.service.bean.BasePersistService;

@Service
@Transactional
public class LotteryWinService extends BasePersistService<LotteryWin> implements ILotteryWinService {

    private LotteryWinRepository lotteryWinRepository;

    @Autowired
    public void setLotteryWinRepository(LotteryWinRepository lotteryWinRepository) {
        this.lotteryWinRepository = lotteryWinRepository;
    }

    @Override
    public Long getLotteryWinCount(Long chatId) {
        return lotteryWinRepository.getLotteryWinCount(chatId);
    }

    @Override
    public void deleteByUser_ChatId(Long chatId) {
        lotteryWinRepository.deleteByUser_ChatId(chatId);
    }

    @Override
    protected BaseRepository<LotteryWin> getBaseRepository() {
        return lotteryWinRepository;
    }

}
