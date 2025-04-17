package tgb.btc.library.service.web.merchant.alfateam;

import org.springframework.stereotype.Service;
import tgb.btc.library.bean.bot.Deal;
import tgb.btc.library.constants.enums.Merchant;
import tgb.btc.library.repository.bot.deal.ModifyDealRepository;
import tgb.btc.library.service.web.merchant.IMerchantRequisiteService;
import tgb.btc.library.vo.web.merchant.alfateam.CreateInvoiceResponse;

@Service
public class AlfaTeamVTBRequisiteService extends AlfaTeamRequisiteService implements IMerchantRequisiteService {

    public AlfaTeamVTBRequisiteService(AlfaTeamMerchantService alfaTeamMerchantService, ModifyDealRepository modifyDealRepository) {
        super(alfaTeamMerchantService, modifyDealRepository);
    }

    @Override
    protected CreateInvoiceResponse createInvoice(Deal deal) throws Exception {
        return alfaTeamMerchantService.createInvoice(deal, Merchant.ALFA_TEAM_VTB);
    }

    @Override
    public Merchant getMerchant() {
        return Merchant.ALFA_TEAM_VTB;
    }
}
