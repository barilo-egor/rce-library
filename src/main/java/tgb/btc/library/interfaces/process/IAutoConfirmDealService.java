package tgb.btc.library.interfaces.process;

import tgb.btc.library.bean.bot.Deal;

public interface IAutoConfirmDealService {
    boolean match(Deal deal, String status);

    void autoConfirmDeal(Deal deal);
}
