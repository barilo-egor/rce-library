package tgb.btc.library.service.bean.bot.deal.read;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tgb.btc.library.bean.bot.Deal;
import tgb.btc.library.constants.enums.bot.CryptoCurrency;
import tgb.btc.library.constants.enums.bot.DealStatus;
import tgb.btc.library.constants.enums.bot.DealType;
import tgb.btc.library.interfaces.service.bean.bot.deal.read.IDealCountService;
import tgb.btc.library.repository.BaseRepository;
import tgb.btc.library.repository.bot.deal.read.DealCountRepository;
import tgb.btc.library.service.bean.BasePersistService;

import java.util.List;

@Service
public class DealCountService extends BasePersistService<Deal> implements IDealCountService {

    private DealCountRepository dealCountRepository;

    @Autowired
    public void setDealCountRepository(DealCountRepository dealCountRepository) {
        this.dealCountRepository = dealCountRepository;
    }

    @Autowired
    public DealCountService(BaseRepository<Deal> baseRepository) {
        super(baseRepository);
    }

    @Override
    public Integer getCountFinishedDeal(Long chatId, List<DealStatus> dealStatus) {
        return dealCountRepository.getCountFinishedDeal(chatId, dealStatus);
    }

    @Override
    public Long getCountPassedByUserChatId(Long chatId) {
        return dealCountRepository.getCountPassedByUserChatId(chatId);
    }

    @Override
    public Long getPassedDealsCountByUserChatIdAndDealTypeAndCryptoCurrency(Long chatId, DealType dealType, CryptoCurrency cryptoCurrency) {
        return dealCountRepository.getPassedDealsCountByUserChatIdAndDealTypeAndCryptoCurrency(chatId, dealType, cryptoCurrency);
    }

    @Override
    public Long getPassedDealsCountByUserChatId(Long chatId, DealType dealType) {
        return dealCountRepository.getPassedDealsCountByUserChatId(chatId, dealType);
    }

    @Override
    public Long getPassedDealsCountByUserChatId(Long chatId) {
        return dealCountRepository.getPassedDealsCountByUserChatId(chatId);
    }
}
