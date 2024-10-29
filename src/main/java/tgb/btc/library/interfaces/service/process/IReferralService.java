package tgb.btc.library.interfaces.service.process;

import tgb.btc.library.bean.bot.Deal;

public interface IReferralService {
    void processReferralDiscount(Deal deal);

    void processReferralBonus(Deal deal);
}
