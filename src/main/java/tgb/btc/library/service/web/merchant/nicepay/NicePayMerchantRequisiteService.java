package tgb.btc.library.service.web.merchant.nicepay;

import org.springframework.stereotype.Service;
import tgb.btc.library.bean.bot.Deal;
import tgb.btc.library.constants.enums.Merchant;
import tgb.btc.library.service.web.merchant.IMerchantRequisiteService;
import tgb.btc.library.vo.RequisiteVO;
import tgb.btc.library.vo.web.merchant.nicepay.CreateOrderResponse;

import java.util.Objects;

@Service
public class NicePayMerchantRequisiteService implements IMerchantRequisiteService {

    private final NicePayMerchantService nicePayMerchantService;

    public NicePayMerchantRequisiteService(NicePayMerchantService nicePayMerchantService) {
        this.nicePayMerchantService = nicePayMerchantService;
    }

    @Override
    public RequisiteVO getRequisite(Deal deal) {
        CreateOrderResponse createOrderResponse = nicePayMerchantService.createOrder(deal);
        if (!CreateOrderResponse.Status.DETAILS_FOUND.equals(createOrderResponse.getStatus())) {
            return null;
        }
        String req = null;
        if (Objects.nonNull(createOrderResponse.getData().getSubMethod()) && Objects.nonNull(createOrderResponse.getData().getSubMethod().getNames())) {
            req = createOrderResponse.getData().getSubMethod().getNames().getRu() + " " + createOrderResponse.getData().getDetails().getWallet();
        } else if (Objects.nonNull(createOrderResponse.getData().getDetails()) && Objects.nonNull(createOrderResponse.getData().getDetails().getComment())) {
            req = createOrderResponse.getData().getDetails().getComment() + " " + createOrderResponse.getData().getDetails().getWallet();
        }
        if (Objects.isNull(req)) {
            return null;
        }
        RequisiteVO requisiteVO = new RequisiteVO();
        requisiteVO.setRequisite(req);
        requisiteVO.setMerchant(Merchant.NICE_PAY);
        deal.setMerchantOrderStatus(createOrderResponse. getData().getStatus().name());
        deal.setMerchantOrderId(createOrderResponse.getData().getPaymentId());
        deal.setMerchant(Merchant.NICE_PAY);
        return requisiteVO;
    }

    @Override
    public Merchant getMerchant() {
        return Merchant.NICE_PAY;
    }
}
