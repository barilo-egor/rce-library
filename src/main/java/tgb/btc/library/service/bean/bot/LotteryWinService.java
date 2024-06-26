package tgb.btc.library.service.bean.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tgb.btc.library.bean.bot.Deal;
import tgb.btc.library.interfaces.service.bot.ILotteryWinService;
import tgb.btc.library.repository.BaseRepository;
import tgb.btc.library.repository.bot.LotteryWinRepository;
import tgb.btc.library.service.bean.BasePersistService;

@Service
public class LotteryWinService extends BasePersistService<Deal> implements ILotteryWinService {

    private LotteryWinRepository lotteryWinRepository;

    @Autowired
    public void setLotteryWinRepository(LotteryWinRepository lotteryWinRepository) {
        this.lotteryWinRepository = lotteryWinRepository;
    }

    public LotteryWinService(BaseRepository<Deal> baseRepository) {
        super(baseRepository);
    }

    @Override
    public Long getLotteryWinCount(Long chatId) {
        return lotteryWinRepository.getLotteryWinCount(chatId);
    }

    @Override
    public void deleteByUser_ChatId(Long chatId) {
        lotteryWinRepository.deleteByUser_ChatId(chatId);
    }
}
