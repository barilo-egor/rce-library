package tgb.btc.library.service.web.merchant.dashpay;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tgb.btc.library.bean.bot.Deal;
import tgb.btc.library.constants.enums.Merchant;
import tgb.btc.library.constants.enums.web.merchant.dashpay.DashPayOrderStatus;
import tgb.btc.library.exception.BaseException;
import tgb.btc.library.repository.bot.deal.ModifyDealRepository;
import tgb.btc.library.service.web.merchant.IMerchantRequisiteService;
import tgb.btc.library.vo.RequisiteVO;
import tgb.btc.library.vo.web.merchant.dashpay.Order;
import tgb.btc.library.vo.web.merchant.dashpay.OrderResponse;

import java.util.Objects;

@Slf4j
@Service
public class DashPayRequisiteService implements IMerchantRequisiteService {

    private final DashPayMerchantService dashPayMerchantService;

    private final ModifyDealRepository modifyDealRepository;

    public DashPayRequisiteService(DashPayMerchantService dashPayMerchantService,
                                   ModifyDealRepository modifyDealRepository) {
        this.dashPayMerchantService = dashPayMerchantService;
        this.modifyDealRepository = modifyDealRepository;
    }

    @Override
    public RequisiteVO getRequisite(Deal deal) {
        if (Objects.isNull(deal.getPaymentType().getDashPayOrderMethod())) {
            return null;
        }
        OrderResponse orderResponse;
        try {
            orderResponse = dashPayMerchantService.createOrder(deal);
            String dashPayOrderId = orderResponse.getData().getOrder().getId();
            deal.setMerchant(getMerchant());
            deal.setMerchantOrderId(dashPayOrderId);
            deal.setMerchantOrderStatus(DashPayOrderStatus.NEW.name());
            modifyDealRepository.save(deal);
        } catch (Exception e) {
            log.error("Ошибка при выполнении запроса на создание DashPay ордера.", e);
            throw new BaseException();
        }
        return RequisiteVO.builder().merchant(getMerchant()).requisite(buildRequisite(orderResponse)).build();
    }

    private String buildRequisite(OrderResponse orderResponse) {
        Order.TraderRequisites traderRequisites = orderResponse.getData().getOrder().getTraderRequisites();
        if (Objects.isNull(traderRequisites)) {
            dashPayMerchantService.cancelOrder(orderResponse.getData().getOrder().getId());
            throw new BaseException();
        }
        return traderRequisites.getBank() + " " + traderRequisites.getValue();
    }

    @Override
    public Merchant getMerchant() {
        return Merchant.DASH_PAY;
    }
}
