package tgb.btc.library.service.web.merchant.alfateam;

import org.springframework.stereotype.Service;
import tgb.btc.library.bean.bot.Deal;
import tgb.btc.library.constants.enums.Merchant;
import tgb.btc.library.constants.enums.web.merchant.alfateam.PaymentOption;
import tgb.btc.library.repository.bot.deal.ModifyDealRepository;
import tgb.btc.library.service.web.merchant.IMerchantRequisiteService;
import tgb.btc.library.vo.web.merchant.alfateam.CreateInvoiceResponse;

import java.util.Objects;

@Service
public class AlfaTeamAlfaRequisiteService extends AlfaTeamRequisiteService implements IMerchantRequisiteService {

    public AlfaTeamAlfaRequisiteService(AlfaTeamMerchantService alfaTeamMerchantService, ModifyDealRepository modifyDealRepository) {
        super(alfaTeamMerchantService, modifyDealRepository);
    }

    @Override
    protected CreateInvoiceResponse createInvoice(Deal deal) throws Exception {
        return alfaTeamMerchantService.createInvoice(deal, Merchant.ALFA_TEAM_ALFA, PaymentOption.SBP);
    }

    @Override
    protected boolean hasBinding(Deal deal) {
        return Objects.isNull(deal.getPaymentType().getAlfaTeamAlfaPaymentOption());
    }

    @Override
    public Merchant getMerchant() {
        return Merchant.ALFA_TEAM_ALFA;
    }
}
