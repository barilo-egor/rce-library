package tgb.btc.library.service.web.merchant.alfateam;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tgb.btc.library.bean.bot.Deal;
import tgb.btc.library.constants.enums.Merchant;
import tgb.btc.library.constants.enums.web.merchant.alfateam.InvoiceStatus;
import tgb.btc.library.exception.BaseException;
import tgb.btc.library.repository.bot.deal.ModifyDealRepository;
import tgb.btc.library.service.web.merchant.IMerchantRequisiteService;
import tgb.btc.library.vo.RequisiteVO;
import tgb.btc.library.vo.web.merchant.alfateam.CreateInvoiceResponse;
import tgb.btc.library.vo.web.merchant.alfateam.DealDTO;

import java.util.Objects;

@Service
@Slf4j
public class AlfaTeamRequisiteService implements IMerchantRequisiteService {

    protected final AlfaTeamMerchantService alfaTeamMerchantService;

    private final ModifyDealRepository modifyDealRepository;

    public AlfaTeamRequisiteService(AlfaTeamMerchantService alfaTeamMerchantService,
                                    ModifyDealRepository modifyDealRepository) {
        this.alfaTeamMerchantService = alfaTeamMerchantService;
        this.modifyDealRepository = modifyDealRepository;
    }

    @Override
    public RequisiteVO getRequisite(Deal deal) {
        if (Objects.isNull(deal.getPaymentType().getAlfaTeamPaymentOption())) {
            return null;
        }
        CreateInvoiceResponse invoiceResponse;
        try {
            invoiceResponse = createInvoice(deal);
        } catch (Exception e) {
            log.error("Ошибка при выполнении запроса на создание AlfaTeam ордера.", e);
            throw new BaseException();
        }
        if (!invoiceResponse.hasRequisites()) {
            return null;
        }
        String alfaTeamInvoiceId = invoiceResponse.getId();
        deal.setMerchant(getMerchant());
        deal.setMerchantOrderId(alfaTeamInvoiceId);
        deal.setMerchantOrderStatus(InvoiceStatus.NEW.name());
        modifyDealRepository.save(deal);
        return RequisiteVO.builder().merchant(getMerchant()).requisite(buildRequisite(invoiceResponse)).build();
    }

    protected CreateInvoiceResponse createInvoice(Deal deal) throws Exception {
        return alfaTeamMerchantService.createInvoice(deal);
    }

    private String buildRequisite(CreateInvoiceResponse invoiceResponse) {
        DealDTO dealDTO = invoiceResponse.getDeals().get(0);
        return dealDTO.getPaymentMethod().getDisplayName() + " " + dealDTO.getRequisites().getRequisites();
    }

    @Override
    public Merchant getMerchant() {
        return Merchant.ALFA_TEAM;
    }
}
