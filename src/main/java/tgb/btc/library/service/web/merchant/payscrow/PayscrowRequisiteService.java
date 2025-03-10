package tgb.btc.library.service.web.merchant.payscrow;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tgb.btc.library.bean.bot.Deal;
import tgb.btc.library.constants.enums.Merchant;
import tgb.btc.library.constants.enums.web.merchant.payscrow.OrderStatus;
import tgb.btc.library.constants.enums.web.merchant.payscrow.PaymentMethodType;
import tgb.btc.library.exception.BaseException;
import tgb.btc.library.repository.bot.deal.ModifyDealRepository;
import tgb.btc.library.service.web.merchant.IMerchantRequisiteService;
import tgb.btc.library.vo.RequisiteVO;
import tgb.btc.library.vo.web.merchant.payscrow.PayscrowOrderResponse;

import java.util.Objects;

@Service
@Slf4j
public class PayscrowRequisiteService implements IMerchantRequisiteService {

    private final PayscrowMerchantService payscrowMerchantService;

    private final ModifyDealRepository modifyDealRepository;

    public PayscrowRequisiteService(PayscrowMerchantService payscrowMerchantService,
                                    ModifyDealRepository modifyDealRepository) {
        this.payscrowMerchantService = payscrowMerchantService;
        this.modifyDealRepository = modifyDealRepository;
    }

    @Override
    public RequisiteVO getRequisite(Deal deal) {
        PayscrowOrderResponse payscrowOrderResponse;
        if (Objects.isNull(deal.getPaymentType().getPayscrowPaymentMethodId())) {
            return null;
        }
        try {
            payscrowOrderResponse = payscrowMerchantService.createBuyOrder(deal);
        } catch (Exception e) {
            log.error("Ошибка при выполнении запроса на создание Payscrow ордера.", e);
            throw new BaseException();
        }
        if (!payscrowOrderResponse.getSuccess()) {
            log.warn("Неуспешный ответ от Payscrow при создании Buy ордера для сделки №{}: {}", deal.getPid(), payscrowOrderResponse);
            throw new BaseException();
        }
        deal.setPayscrowOrderId(payscrowOrderResponse.getOrderId());
        deal.setPayscrowOrderStatus(OrderStatus.UNPAID);
        modifyDealRepository.save(deal);
        return RequisiteVO.builder().merchant(Merchant.PAYSCROW).requisite(buildRequisiteString(payscrowOrderResponse)).build();
    }

    private static String buildRequisiteString(PayscrowOrderResponse payscrowOrderResponse) {
        String result;
        String holderAccount = payscrowOrderResponse.getHolderAccount();
        if (PaymentMethodType.BANK_CARD.equals(payscrowOrderResponse.getPaymentMethodType())) {
            StringBuilder card = new StringBuilder();
            for (int i = 0; i < holderAccount.length(); i++) {
                card.append(holderAccount.charAt(i));

                if ((i + 1) % 4 == 0 && i != holderAccount.length() - 1) {
                    card.append(" ");
                }
            }
            result = payscrowOrderResponse.getMethodName() + " " + card;
        } else {
            result = payscrowOrderResponse.getMethodName() + " " + holderAccount;
        }
        return result;
    }

    @Override
    public Merchant getMerchant() {
        return Merchant.PAYSCROW;
    }
}
