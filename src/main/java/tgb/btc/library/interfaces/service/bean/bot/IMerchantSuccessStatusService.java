package tgb.btc.library.interfaces.service.bean.bot;

import tgb.btc.library.bean.bot.MerchantSuccessStatus;

public interface IMerchantSuccessStatusService {
    void delete(MerchantSuccessStatus merchantSuccessStatus);

    MerchantSuccessStatus create(String status);

    MerchantSuccessStatus save(MerchantSuccessStatus merchantSuccessStatus);
}
