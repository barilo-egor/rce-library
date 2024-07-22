package tgb.btc.library.service.bean.common.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tgb.btc.library.interfaces.service.bean.bot.deal.read.IDealCountService;
import tgb.btc.library.interfaces.service.bean.common.bot.IDealCommonService;

@Service
public class DealCommonService implements IDealCommonService {

    private IDealCountService dealCountService;

    @Autowired
    public void setDealCountService(IDealCountService dealCountService) {
        this.dealCountService = dealCountService;
    }

    @Override
    public boolean isFirstDeal(Long chatId) {
        return dealCountService.getConfirmedDealsCountByUserChatId(chatId) < 1;
    }
}
