package tgb.btc.library.service.web.merchant.evopay;

import org.springframework.stereotype.Service;
import tgb.btc.library.bean.bot.Deal;
import tgb.btc.library.constants.enums.Merchant;
import tgb.btc.library.constants.enums.web.merchant.evopay.EvoPayPaymentMethod;
import tgb.btc.library.constants.enums.web.merchant.evopay.EvoPayStatus;
import tgb.btc.library.service.web.merchant.IMerchantRequisiteService;
import tgb.btc.library.vo.RequisiteVO;
import tgb.btc.library.vo.web.merchant.evopay.OrderResponse;

import java.util.Objects;

@Service
public class EvoPayMerchantRequisiteService implements IMerchantRequisiteService {

    private final EvoPayMerchantService evoPayMerchantService;

    public EvoPayMerchantRequisiteService(EvoPayMerchantService evoPayMerchantService) {
        this.evoPayMerchantService = evoPayMerchantService;
    }

    @Override
    public RequisiteVO getRequisite(Deal deal) {
        EvoPayPaymentMethod evoPayPaymentMethod = deal.getPaymentType().getEvoPayPaymentMethod();
        if (Objects.isNull(evoPayPaymentMethod)) {
            return null;
        }
        OrderResponse orderResponse = evoPayMerchantService.createOrder(deal);
        try {
            Thread.sleep(4000);
        } catch (InterruptedException ignored) {
        }
        orderResponse = evoPayMerchantService.getOrder(orderResponse.getId());
        if (!EvoPayStatus.IN_PROCESS.equals(orderResponse.getOrderStatus())) {
            return null;
        }
        RequisiteVO requisiteVO = new RequisiteVO();
        requisiteVO.setMerchant(Merchant.EVO_PAY);
        requisiteVO.setRequisite(EvoPayPaymentMethod.BANK_CARD.equals(evoPayPaymentMethod)
                ? orderResponse.getRequisites().getRecipientBank() + " " + orderResponse.getRequisites().getRecipientCardNumber()
                : orderResponse.getRequisites().getRecipientBank() + " " + orderResponse.getRequisites().getRecipientPhoneNumber()
                );
        deal.setMerchant(Merchant.EVO_PAY);
        deal.setMerchantOrderId(orderResponse.getId());
        deal.setMerchantOrderStatus(orderResponse.getOrderStatus().name());
        return requisiteVO;
    }

    @Override
    public Merchant getMerchant() {
        return Merchant.EVO_PAY;
    }
}
