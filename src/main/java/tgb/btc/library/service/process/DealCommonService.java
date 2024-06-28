package tgb.btc.library.service.process;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tgb.btc.library.interfaces.process.IDealCommonService;
import tgb.btc.library.interfaces.service.bot.deal.read.IDealCountService;

@Service
public class DealCommonService implements IDealCommonService {

    private IDealCountService dealCountService;

    @Autowired
    public void setDealCountService(IDealCountService dealCountService) {
        this.dealCountService = dealCountService;
    }

    @Override
    public boolean isFirstDeal(Long chatId) {
        return dealCountService.getPassedDealsCountByUserChatId(chatId) < 1;
    }
}
