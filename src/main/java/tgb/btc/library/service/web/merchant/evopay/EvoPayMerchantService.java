package tgb.btc.library.service.web.merchant.evopay;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tgb.btc.library.constants.enums.Merchant;
import tgb.btc.library.service.web.merchant.IMerchantService;

@Service
public class EvoPayMerchantService implements IMerchantService {

    private final String apiKey;

    public EvoPayMerchantService(@Value("{evopay.api.key:null}") String apiKey,
                                 @Value("{evopay.api.url:null}") String baseUrl) {
        this.apiKey = apiKey;
    }



    @Override
    public void cancelOrder(String orderId) {

    }

    @Override
    public Merchant getMerchant() {
        return Merchant.EVO_PAY;
    }
}
