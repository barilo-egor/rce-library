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
        if (Objects.isNull(deal.getPaymentType().getNicePayMethod())) {
            return null;
        }
        CreateOrderResponse createOrderResponse = nicePayMerchantService.createOrder(deal);
        if (!CreateOrderResponse.Status.DETAILS_FOUND.equals(createOrderResponse.getStatus())) {
            return null;
        }
        if (Objects.isNull(createOrderResponse.getData().getSubMethod()) || Objects.isNull(createOrderResponse.getData().getDetails())) {
            return null;
        }
        deal.setMerchantOrderStatus(createOrderResponse.getStatus().name());
        deal.setMerchantOrderId(createOrderResponse.getData().getPaymentId());
        deal.setMerchant(Merchant.NICE_PAY);
        RequisiteVO requisiteVO = new RequisiteVO();
        requisiteVO.setRequisite(createOrderResponse.getData().getSubMethod().getNames().getRu() + " " + createOrderResponse.getData().getDetails().getWallet());
        requisiteVO.setMerchant(Merchant.NICE_PAY);
        return requisiteVO;
    }

    @Override
    public Merchant getMerchant() {
        return Merchant.NICE_PAY;
    }
}
