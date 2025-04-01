package tgb.btc.library.service.web.merchant.onlypays;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tgb.btc.library.bean.bot.Deal;
import tgb.btc.library.constants.enums.Merchant;
import tgb.btc.library.constants.enums.web.merchant.onlypays.OnlyPaysStatus;
import tgb.btc.library.repository.bot.deal.ModifyDealRepository;
import tgb.btc.library.service.web.merchant.IMerchantRequisiteService;
import tgb.btc.library.vo.RequisiteVO;
import tgb.btc.library.vo.web.merchant.onlypays.GetRequisiteResponse;

@Service
@Slf4j
public class OnlyPaysRequisiteService implements IMerchantRequisiteService {

    private final OnlyPaysMerchantService onlyPaysMerchantService;

    private final ModifyDealRepository modifyDealRepository;

    public OnlyPaysRequisiteService(OnlyPaysMerchantService onlyPaysMerchantService, ModifyDealRepository modifyDealRepository) {
        this.onlyPaysMerchantService = onlyPaysMerchantService;
        this.modifyDealRepository = modifyDealRepository;
    }

    @Override
    public RequisiteVO getRequisite(Deal deal) {
        GetRequisiteResponse getRequisiteResponse = onlyPaysMerchantService.requisiteRequest(deal);
        if (!getRequisiteResponse.isSuccess()) {
            log.debug("Неуспешный ответ при получении реквизитов onlypays: {}", getRequisiteResponse);
            return null;
        }
        RequisiteVO requisiteVO = new RequisiteVO();
        requisiteVO.setMerchant(getMerchant());
        requisiteVO.setRequisite(getRequisiteResponse.getData().getBank() + " " + getRequisiteResponse.getData().getRequisite());

        deal.setMerchant(getMerchant());
        deal.setMerchantOrderId(getRequisiteResponse.getData().getId());
        deal.setMerchantOrderStatus(OnlyPaysStatus.WAITING.name());
        modifyDealRepository.save(deal);
        return requisiteVO;
    }

    @Override
    public Merchant getMerchant() {
        return Merchant.ONLY_PAYS;
    }
}
