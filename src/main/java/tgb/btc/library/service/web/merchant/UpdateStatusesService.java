package tgb.btc.library.service.web.merchant;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import tgb.btc.api.web.INotifier;
import tgb.btc.library.bean.bot.Deal;
import tgb.btc.library.constants.enums.web.merchant.dashpay.DashPayOrderStatus;
import tgb.btc.library.interfaces.service.bean.bot.deal.IReadDealService;
import tgb.btc.library.repository.bot.deal.ModifyDealRepository;
import tgb.btc.library.service.web.merchant.dashpay.DashPayMerchantService;
import tgb.btc.library.service.web.merchant.payscrow.PayscrowMerchantService;
import tgb.btc.library.vo.web.merchant.dashpay.OrdersResponse;
import tgb.btc.library.vo.web.merchant.payscrow.ListOrdersResponse;
import tgb.btc.library.vo.web.merchant.payscrow.Order;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class UpdateStatusesService {

    private final IReadDealService readDealService;

    private final ModifyDealRepository modifyDealRepository;

    private final INotifier notifier;

    private final DashPayMerchantService dashPayMerchantService;

    private final PayscrowMerchantService payscrowMerchantService;

    public UpdateStatusesService(IReadDealService readDealService, ModifyDealRepository modifyDealRepository,
                                 INotifier notifier, DashPayMerchantService dashPayMerchantService,
                                 PayscrowMerchantService payscrowMerchantService) {
        this.readDealService = readDealService;
        this.modifyDealRepository = modifyDealRepository;
        this.notifier = notifier;
        this.dashPayMerchantService = dashPayMerchantService;
        this.payscrowMerchantService = payscrowMerchantService;
    }

    @Scheduled(cron = "*/5 * * * * *")
    @Async
    public void updatePayscrowStatuses() {
        List<Deal> deals = readDealService.getAllNotFinalPayscrowStatuses();
        if (Objects.isNull(deals) || deals.isEmpty()) {
            return;
        }
        ListOrdersResponse listOrdersResponse = payscrowMerchantService.getLast30MinutesOrders();
        if (Objects.isNull(listOrdersResponse.getOrders()) || listOrdersResponse.getOrders().isEmpty()) {
            return;
        }
        for (Order order: listOrdersResponse.getOrders()) {
            for (Deal deal: deals) {
                if (order.getOrderId().toString().equals(deal.getMerchantOrderId())
                        && !order.getOrderStatus().name().equals(deal.getMerchantOrderStatus())) {
                    deal.setMerchantOrderStatus(order.getOrderStatus().name());
                    modifyDealRepository.save(deal);
                    notifier.merchantUpdateStatus(deal.getPid(), "Payscrow обновил статус по сделке №" + deal.getPid()
                            + " до \"" + order.getOrderStatus().getDescription() + "\".");
                }
            }
        }
    }

    @Scheduled(cron = "*/5 * * * * *")
    @Async
    public void updateDashPayStatuses() {
        List<Deal> deals = readDealService.getAllNotFinalDashPayStatuses();
        if (Objects.isNull(deals) || deals.isEmpty()) {
            return;
        }
        OrdersResponse ordersResponse = getLast30MinutesDashPayOrders();
        if (Objects.isNull(ordersResponse.getData()) || ordersResponse.getData().getOrders().isEmpty()) {
            return;
        }
        for (OrdersResponse.Data.Order order: ordersResponse.getData().getOrders()) {
            for (Deal deal: deals) {
                if (order.getId().equals(deal.getMerchantOrderId())) {
                    DashPayOrderStatus status = DashPayOrderStatus.fromCode(order.getStatus().getCode());
                    if (Objects.nonNull(status) && !Objects.equals(status, DashPayOrderStatus.valueOf(deal.getMerchantOrderStatus()))) {
                        deal.setMerchantOrderStatus(Objects.requireNonNull(DashPayOrderStatus.fromCode(order.getStatus().getCode())).name());
                        modifyDealRepository.save(deal);
                        notifier.merchantUpdateStatus(deal.getPid(), "DashPay обновил статус по сделке №" + deal.getPid()
                                + " до \"" + status.getDescription() + "\".");
                    }
                }
            }
        }
    }

    private OrdersResponse getLast30MinutesDashPayOrders() {
        LocalDateTime to = LocalDateTime.now();
        LocalDateTime from = to.minusMinutes(30);
        return dashPayMerchantService.getOrders(from, to);
    }
}
