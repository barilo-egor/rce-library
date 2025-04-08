package tgb.btc.library.service.web.merchant.payfinity;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tgb.btc.library.bean.bot.Deal;
import tgb.btc.library.constants.enums.Merchant;
import tgb.btc.library.constants.enums.web.merchant.payfinity.PayFinityOrderType;
import tgb.btc.library.constants.enums.web.merchant.payfinity.PayFinityStatus;
import tgb.btc.library.exception.BaseException;
import tgb.btc.library.service.web.merchant.IMerchantRequisiteService;
import tgb.btc.library.vo.RequisiteVO;
import tgb.btc.library.vo.web.merchant.payfinity.CreateOrderResponse;

import java.util.Objects;

@Service
@Slf4j
public class PayFinityRequisiteMerchantService implements IMerchantRequisiteService {

    private final PayFinityMerchantService payFinityMerchantService;

    public PayFinityRequisiteMerchantService(PayFinityMerchantService payFinityMerchantService) {
        this.payFinityMerchantService = payFinityMerchantService;
    }

    @Override
    @Transactional
    public RequisiteVO getRequisite(Deal deal) {
        if (Objects.isNull(deal.getPaymentType().getPayFinityOrderType())) {
            return null;
        }
        try {
            CreateOrderResponse createOrderResponse = payFinityMerchantService.createOrder(deal);
            if (!createOrderResponse.getSuccess()) {
                throw new BaseException(createOrderResponse.getError().getMessage());
            }
            RequisiteVO requisiteVO = new RequisiteVO();
            requisiteVO.setMerchant(Merchant.PAY_FINITY);
            PayFinityOrderType payFinityOrderType = deal.getPaymentType().getPayFinityOrderType();
            if (payFinityOrderType.getType().equals("SBP")) {
                requisiteVO.setRequisite(createOrderResponse.getData().getBank() + " " + createOrderResponse.getData().getSBPPhoneNumber());
            } else {
                requisiteVO.setRequisite(createOrderResponse.getData().getBank() + " " + createOrderResponse.getData().getCardNumber());
            }
            deal.setMerchantOrderId(createOrderResponse.getData().getTrackerID());
            deal.setMerchant(Merchant.PAY_FINITY);
            deal.setMerchantOrderStatus(PayFinityStatus.PENDING.name());
            return requisiteVO;
        } catch (Exception e) {
            log.debug("Ошибка при попытке получения реквизитов от PayFinity: ", e);
            return null;
        }
    }

    @Override
    public Merchant getMerchant() {
        return Merchant.PAY_FINITY;
    }
}
