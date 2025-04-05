package tgb.btc.library.service.web.merchant.nicepay;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tgb.btc.library.bean.bot.Deal;
import tgb.btc.library.constants.enums.Merchant;
import tgb.btc.library.service.web.merchant.IMerchantService;
import tgb.btc.library.vo.web.merchant.nicepay.CreateOrderRequest;
import tgb.btc.library.vo.web.merchant.nicepay.CreateOrderResponse;

@Service
public class NicePayMerchantService implements IMerchantService {

    private final String merchantId;

    private final String secret;

    public NicePayMerchantService(@Value("${}") String merchantId, String secret) {
        this.merchantId = merchantId;
        this.secret = secret;
    }

    public CreateOrderResponse createOrder(Deal deal) {
        CreateOrderRequest createOrderRequest = new CreateOrderRequest();
        createOrderRequest.setMerchantId(merchantId);
        createOrderRequest.setSecret(secret);
        createOrderRequest.setAmount(deal.getAmount().intValue());
        createOrderRequest.setCustomer(deal.getUser().getChatId().toString());
        return null;
    }

    @Override
    public void cancelOrder(String orderId) {

    }

    @Override
    public Merchant getMerchant() {
        return Merchant.NICE_PAY;
    }
}
