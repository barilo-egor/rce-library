package tgb.btc.library.service.web.merchant.crocopay;

import org.springframework.stereotype.Service;
import tgb.btc.library.bean.bot.Deal;
import tgb.btc.library.constants.enums.Merchant;
import tgb.btc.library.service.web.merchant.IMerchantRequisiteService;
import tgb.btc.library.vo.RequisiteVO;
import tgb.btc.library.vo.web.merchant.crocopay.CreateInvoiceResponse;

import java.util.Objects;

@Service
public class CrocoPayMerchantRequisiteService implements IMerchantRequisiteService {

    private final String ANY_BANK = "any_rub_bank";

    private final CrocoPayMerchantService crocoPayMerchantService;

    public CrocoPayMerchantRequisiteService(CrocoPayMerchantService crocoPayMerchantService) {
        this.crocoPayMerchantService = crocoPayMerchantService;
    }

    @Override
    public RequisiteVO getRequisite(Deal deal) {
        if (Objects.isNull(deal.getPaymentType().getCrocoPayMethod())) {
            return null;
        }
        CreateInvoiceResponse createInvoiceResponse = crocoPayMerchantService.createInvoice(deal);
        CreateInvoiceResponse.Response response = createInvoiceResponse.getResponse();
        RequisiteVO vo = new RequisiteVO();
        vo.setMerchant(Merchant.CROCO_PAY);
        String requisite;
        if (ANY_BANK.equals(response.getPaymentRequisites().getPaymentMethod())) {
            requisite = response.getPaymentRequisites().getRequisites();
        } else {
            requisite = response.getPaymentRequisites().getPaymentMethod() + " " + response.getPaymentRequisites().getRequisites();
        }
        vo.setRequisite(requisite);
        deal.setMerchant(Merchant.CROCO_PAY);
        deal.setMerchantOrderId(response.getTransaction().getId());
        deal.setMerchantOrderStatus(response.getTransaction().getStatus().name());
        return vo;
    }

    @Override
    public Merchant getMerchant() {
        return Merchant.CROCO_PAY;
    }
}
