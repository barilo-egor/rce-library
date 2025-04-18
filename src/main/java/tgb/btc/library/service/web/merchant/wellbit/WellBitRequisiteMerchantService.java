package tgb.btc.library.service.web.merchant.wellbit;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;
import tgb.btc.library.bean.bot.Deal;
import tgb.btc.library.constants.enums.Merchant;
import tgb.btc.library.constants.enums.web.merchant.wellbit.WellBitMethod;
import tgb.btc.library.constants.enums.web.merchant.wellbit.WellBitStatus;
import tgb.btc.library.exception.BaseException;
import tgb.btc.library.service.web.merchant.IMerchantRequisiteService;
import tgb.btc.library.vo.RequisiteVO;
import tgb.btc.library.vo.web.merchant.wellbit.Order;

import java.util.Objects;

@Service
public class WellBitRequisiteMerchantService implements IMerchantRequisiteService {

    private final WellBitMerchantService wellBitMerchantService;

    public WellBitRequisiteMerchantService(WellBitMerchantService wellBitMerchantService) {
        this.wellBitMerchantService = wellBitMerchantService;
    }

    @Override
    public RequisiteVO getRequisite(Deal deal) {
        WellBitMethod wellBitMethod = deal.getPaymentType().getWellBitMethod();
        Order order;
        try {
            order = wellBitMerchantService.createOrder(deal);
        } catch (JsonProcessingException e) {
            throw new BaseException(e);
        }
        if (Objects.isNull(order)) {
            return null;
        }
        RequisiteVO requisiteVO = new RequisiteVO();
        requisiteVO.setMerchant(getMerchant());
        requisiteVO.setRequisite(order.getPayment().getCredentialAdditionalBank() + " " + order.getPayment().getCredential());
        deal.setMerchant(getMerchant());
        deal.setMerchantOrderStatus(WellBitStatus.NEW.name());
        deal.setMerchantOrderId(order.getPayment().getId().toString());
        return requisiteVO;
    }

    @Override
    public Merchant getMerchant() {
        return Merchant.WELL_BIT;
    }
}
