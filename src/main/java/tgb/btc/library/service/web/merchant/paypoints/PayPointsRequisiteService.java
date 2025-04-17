package tgb.btc.library.service.web.merchant.paypoints;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tgb.btc.library.bean.bot.Deal;
import tgb.btc.library.constants.enums.Merchant;
import tgb.btc.library.constants.enums.web.merchant.paypoints.PayPointsMethod;
import tgb.btc.library.constants.enums.web.merchant.paypoints.PayPointsStatus;
import tgb.btc.library.exception.BaseException;
import tgb.btc.library.repository.bot.deal.ModifyDealRepository;
import tgb.btc.library.service.web.merchant.IMerchantRequisiteService;
import tgb.btc.library.vo.RequisiteVO;
import tgb.btc.library.vo.web.merchant.paypoints.CardTransactionResponse;
import tgb.btc.library.vo.web.merchant.paypoints.SbpTransactionResponse;

@Service
@Slf4j
public class PayPointsRequisiteService implements IMerchantRequisiteService {

    private final PayPointsMerchantService payPointsMerchantService;

    private final ModifyDealRepository modifyDealRepository;

    public PayPointsRequisiteService(PayPointsMerchantService payPointsMerchantService, ModifyDealRepository modifyDealRepository) {
        this.payPointsMerchantService = payPointsMerchantService;
        this.modifyDealRepository = modifyDealRepository;
    }

    @Override
    public RequisiteVO getRequisite(Deal deal) {
        PayPointsMethod payPointsMethod = deal.getPaymentType().getPayPointsMethod();
        try {
            Long id;
            RequisiteVO requisiteVO;
            switch (payPointsMethod) {
                case SBP: {
                    SbpTransactionResponse response = payPointsMerchantService.createSbpTransaction(deal);
                    if (!response.isOk()) {
                        log.debug("Ошибка в ответе при попытке создании транзакции: {}", response);
                        return null;
                    }
                    requisiteVO = new RequisiteVO();
                    requisiteVO.setMerchant(Merchant.PAY_POINTS);
                    requisiteVO.setRequisite(response.getBankName() + " " + response.getPhoneNumber());
                    id = response.getId();
                    break;
                }
                case CARD: {
                    CardTransactionResponse response = payPointsMerchantService.createCardTransaction(deal);
                    if (!response.isOk()) {
                        log.debug("Ошибка в ответе при попытке создании транзакции: {}", response);
                        return null;
                    }
                    requisiteVO = new RequisiteVO();
                    requisiteVO.setMerchant(Merchant.PAY_POINTS);
                    requisiteVO.setRequisite(response.getBankName() + " " + response.getCardNumber());
                    id = response.getId();
                    break;
                }
                case TRANSGRAN_SBP:
                    SbpTransactionResponse response = payPointsMerchantService.createTransgranSbpTransaction(deal);
                    if (!response.isOk()) {
                        log.debug("Ошибка в ответе при попытке создании транзакции: {}", response);
                        return null;
                    }
                    requisiteVO = new RequisiteVO();
                    requisiteVO.setMerchant(Merchant.PAY_POINTS);
                    requisiteVO.setRequisite(response.getBankName() + " " + response.getPhoneNumber());
                    id = response.getId();
                    break;
                default: throw new BaseException("Не определен метод");
            }
            deal.setMerchant(getMerchant());
            deal.setMerchantOrderId(id.toString());
            deal.setMerchantOrderStatus(PayPointsStatus.PROCESS.name());
            modifyDealRepository.save(deal);
            return requisiteVO;
        } catch (Exception e) {
            log.debug("Ошибка при попытке создать транзакцию PayPoints: ", e);
        }
        return null;
    }

    @Override
    public Merchant getMerchant() {
        return Merchant.PAY_POINTS;
    }
}
