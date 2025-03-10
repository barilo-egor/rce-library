package tgb.btc.library.service.web.merchant;

import tgb.btc.library.constants.enums.Merchant;

public interface IMerchantService {

    void cancelOrder(String orderId);

    Merchant getMerchant();
}
