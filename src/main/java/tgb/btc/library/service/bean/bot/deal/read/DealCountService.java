package tgb.btc.library.service.bean.bot.deal.read;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
@Transactional(readOnly = true)
public class DealCountService extends BasePersistService<Deal> implements IDealCountService {

    private DealCountRepository dealCountRepository;

    @Autowired
    public void setDealCountRepository(DealCountRepository dealCountRepository) {
        this.dealCountRepository = dealCountRepository;
    }

    @Override
    public Integer getCountDealByChatIdAndNotInDealStatus(Long chatId, List<DealStatus> dealStatus) {
        return dealCountRepository.getCountDealByChatIdAndNotInDealStatus(chatId, dealStatus);
    }

    @Override
    public Long getCountConfirmedByUserChatId(Long chatId) {
        return dealCountRepository.getCountByDealStatusAndChatId(chatId, DealStatus.CONFIRMED);
    }

    @Override
    public Long getConfirmedDealsCountByUserChatIdAndDealTypeAndCryptoCurrency(Long chatId, DealType dealType, CryptoCurrency cryptoCurrency) {
        return dealCountRepository.getDealsCountByUserChatIdAndDealStatusAndDealTypeAndCryptoCurrency(chatId, DealStatus.CONFIRMED, dealType, cryptoCurrency);
    }

    @Override
    protected BaseRepository<Deal> getBaseRepository() {
        return dealCountRepository;
    }

}
