package tgb.btc.library.service.web.merchant.paypoints;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tgb.btc.library.bean.bot.Deal;
import tgb.btc.library.constants.enums.Merchant;
import tgb.btc.library.constants.enums.web.merchant.paypoints.PayPointsMethod;
import tgb.btc.library.service.web.merchant.IMerchantRequisiteService;
import tgb.btc.library.vo.RequisiteVO;
import tgb.btc.library.vo.web.merchant.paypoints.CardTransactionResponse;
import tgb.btc.library.vo.web.merchant.paypoints.SbpTransactionResponse;

import java.util.Objects;

@Service
@Slf4j
public class PayPointsRequisiteService implements IMerchantRequisiteService {

    private final PayPointsMerchantService payPointsMerchantService;

    public PayPointsRequisiteService(PayPointsMerchantService payPointsMerchantService) {
        this.payPointsMerchantService = payPointsMerchantService;
    }

    @Override
    public RequisiteVO getRequisite(Deal deal) {
        PayPointsMethod payPointsMethod = deal.getPaymentType().getPayPointsMethod();
        if (Objects.isNull(payPointsMethod)) {
            return null;
        }
        try {
            switch (payPointsMethod) {
                case SBP: {
                    SbpTransactionResponse response = payPointsMerchantService.createSbpTransaction(deal);
                    if (!response.isOk()) {
                        log.debug("Ошибка в ответе при попытке создании транзакции: {}", response);
                        return null;
                    }
                    RequisiteVO requisiteVO = new RequisiteVO();
                    requisiteVO.setMerchant(Merchant.PAY_POINTS);
                    requisiteVO.setRequisite(response.getBankName() + " " + response.getPhoneNumber());
                    return requisiteVO;
                }
                case CARD: {
                    CardTransactionResponse response = payPointsMerchantService.createCardTransaction(deal);
                    if (!response.isOk()) {
                        log.debug("Ошибка в ответе при попытке создании транзакции: {}", response);
                        return null;
                    }
                    RequisiteVO requisiteVO = new RequisiteVO();
                    requisiteVO.setMerchant(Merchant.PAY_POINTS);
                    requisiteVO.setRequisite(response.getBankName() + " " + response.getCardNumber());
                    return requisiteVO;
                }
                case TRANSGRAN_SBP:
                    SbpTransactionResponse response = payPointsMerchantService.createTransgranSbpTransaction(deal);
                    if (!response.isOk()) {
                        log.debug("Ошибка в ответе при попытке создании транзакции: {}", response);
                        return null;
                    }
                    RequisiteVO requisiteVO = new RequisiteVO();
                    requisiteVO.setMerchant(Merchant.PAY_POINTS);
                    requisiteVO.setRequisite(response.getBankName() + " " + response.getPhoneNumber());
                    return requisiteVO;
            };
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
