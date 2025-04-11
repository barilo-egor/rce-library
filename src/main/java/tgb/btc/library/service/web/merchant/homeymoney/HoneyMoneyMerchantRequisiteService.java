package tgb.btc.library.service.web.merchant.homeymoney;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tgb.btc.library.bean.bot.Deal;
import tgb.btc.library.constants.enums.Merchant;
import tgb.btc.library.constants.enums.web.merchant.honeymoney.HoneyMoneyMethod;
import tgb.btc.library.constants.enums.web.merchant.honeymoney.HoneyMoneyStatus;
import tgb.btc.library.exception.BaseException;
import tgb.btc.library.service.web.merchant.IMerchantRequisiteService;
import tgb.btc.library.vo.RequisiteVO;
import tgb.btc.library.vo.web.merchant.honeymoney.CreateOrderResponse;

import java.util.Objects;

@Service
public class HoneyMoneyMerchantRequisiteService implements IMerchantRequisiteService {

    private final HoneyMoneyMerchantService honeyMoneyMerchantService;

    public HoneyMoneyMerchantRequisiteService(HoneyMoneyMerchantService honeyMoneyMerchantService) {
        this.honeyMoneyMerchantService = honeyMoneyMerchantService;
    }

    @Override
    @Transactional
    public RequisiteVO getRequisite(Deal deal) {
        if (Objects.isNull(deal.getPaymentType().getHoneyMoneyMethod())) {
            return null;
        }
        try {
            HoneyMoneyMethod honeyMoneyMethod = deal.getPaymentType().getHoneyMoneyMethod();
            CreateOrderResponse createOrderResponse = honeyMoneyMerchantService.createRequest(deal);
            RequisiteVO requisiteVO = new RequisiteVO();
            requisiteVO.setMerchant(Merchant.HONEY_MONEY);
            String requisite = switch (honeyMoneyMethod) {
                case SBP, CROSS_BORDER -> createOrderResponse.getPhoneNumber();
                default -> createOrderResponse.getCardNumber();
            };
            requisiteVO.setRequisite(createOrderResponse.getBankName() + " " + requisite);
            deal.setMerchant(getMerchant());
            deal.setMerchantOrderId(createOrderResponse.getId().toString());
            deal.setMerchantOrderStatus(HoneyMoneyStatus.PENDING.name());
            return requisiteVO;
        } catch (Exception e) {
            throw new BaseException(e);
        }
    }

    @Override
    public Merchant getMerchant() {
        return Merchant.HONEY_MONEY;
    }
}
