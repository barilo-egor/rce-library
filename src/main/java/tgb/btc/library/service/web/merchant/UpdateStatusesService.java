package tgb.btc.library.service.web.merchant;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import tgb.btc.api.web.INotifier;
import tgb.btc.library.bean.bot.Deal;
import tgb.btc.library.constants.enums.web.merchant.dashpay.DashPayOrderStatus;
import tgb.btc.library.constants.enums.web.merchant.evopay.EvoPayStatus;
import tgb.btc.library.constants.enums.web.merchant.nicepay.NicePayStatus;
import tgb.btc.library.constants.enums.web.merchant.onlypays.OnlyPaysStatus;
import tgb.btc.library.constants.enums.web.merchant.paypoints.PayPointsStatus;
import tgb.btc.library.interfaces.service.bean.bot.deal.IReadDealService;
import tgb.btc.library.repository.bot.deal.ModifyDealRepository;
import tgb.btc.library.service.web.merchant.dashpay.DashPayMerchantService;
import tgb.btc.library.service.web.merchant.evopay.EvoPayMerchantService;
import tgb.btc.library.service.web.merchant.nicepay.NicePayMerchantService;
import tgb.btc.library.service.web.merchant.onlypays.OnlyPaysMerchantService;
import tgb.btc.library.service.web.merchant.paypoints.PayPointsMerchantService;
import tgb.btc.library.service.web.merchant.payscrow.PayscrowMerchantService;
import tgb.btc.library.vo.web.merchant.dashpay.OrdersResponse;
import tgb.btc.library.vo.web.merchant.evopay.OrderResponse;
import tgb.btc.library.vo.web.merchant.nicepay.GetOrderResponse;
import tgb.btc.library.vo.web.merchant.onlypays.GetStatusResponse;
import tgb.btc.library.vo.web.merchant.payscrow.ListOrdersResponse;
import tgb.btc.library.vo.web.merchant.payscrow.Order;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class UpdateStatusesService {

    private final IReadDealService readDealService;

    private final ModifyDealRepository modifyDealRepository;

    private final INotifier notifier;

    private final DashPayMerchantService dashPayMerchantService;

    private final PayscrowMerchantService payscrowMerchantService;

    private final PayPointsMerchantService payPointsMerchantService;

    private final OnlyPaysMerchantService onlyPaysMerchantService;

    private final EvoPayMerchantService evoPayMerchantService;

    private final NicePayMerchantService nicePayMerchantService;

    public UpdateStatusesService(IReadDealService readDealService, ModifyDealRepository modifyDealRepository,
                                 INotifier notifier, DashPayMerchantService dashPayMerchantService,
                                 PayscrowMerchantService payscrowMerchantService, PayPointsMerchantService payPointsMerchantService,
                                 OnlyPaysMerchantService onlyPaysMerchantService, EvoPayMerchantService evoPayMerchantService,
                                 NicePayMerchantService nicePayMerchantService) {
        this.readDealService = readDealService;
        this.modifyDealRepository = modifyDealRepository;
        this.notifier = notifier;
        this.dashPayMerchantService = dashPayMerchantService;
        this.payscrowMerchantService = payscrowMerchantService;
        this.payPointsMerchantService = payPointsMerchantService;
        this.onlyPaysMerchantService = onlyPaysMerchantService;
        this.evoPayMerchantService = evoPayMerchantService;
        this.nicePayMerchantService = nicePayMerchantService;
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

    @Scheduled(cron = "*/5 * * * * *")
    @Async
    public void updatePayPointsStatuses() {
        List<Deal> deals = readDealService.getAllNotFinalPayPointsStatuses();
        if (Objects.isNull(deals) || deals.isEmpty()) {
            return;
        }
        for (Deal deal: deals) {
            PayPointsStatus payPointsStatus = payPointsMerchantService.getStatus(Long.valueOf(deal.getMerchantOrderId()));
            if (!PayPointsStatus.valueOf(deal.getMerchantOrderStatus()).equals(payPointsStatus)) {
                deal.setMerchantOrderStatus(payPointsStatus.name());
                modifyDealRepository.save(deal);
                notifier.merchantUpdateStatus(deal.getPid(), "PayPoints обновил статус по сделке №" + deal.getPid()
                        + " до \"" + payPointsStatus.getDisplayName() + "\".");
            }
            try {
                Thread.sleep(200);
            } catch (InterruptedException ignored) {
            }
        }
    }

    @Scheduled(cron = "*/5 * * * * *")
    @Async
    public void updateOnlyPaysStatuses() {
        List<Deal> deals = readDealService.getAllNotFinalOnlyPaysStatuses();
        if (Objects.isNull(deals) || deals.isEmpty()) {
            return;
        }
        for (Deal deal: deals) {
            GetStatusResponse getStatusResponse = onlyPaysMerchantService.statusRequest(deal.getMerchantOrderId());
            if (!getStatusResponse.isSuccess()) {
                log.warn("Не удалось получить статус для сделки {}, ответ неуспешен: {}", deal.getPid(), getStatusResponse);
                continue;
            }
            OnlyPaysStatus onlyPaysStatus = getStatusResponse.getData().getStatus();
            if (!OnlyPaysStatus.valueOf(deal.getMerchantOrderStatus()).equals(onlyPaysStatus)) {
                deal.setMerchantOrderStatus(onlyPaysStatus.name());
                modifyDealRepository.save(deal);
                notifier.merchantUpdateStatus(deal.getPid(), "OnlyPays обновил статус по сделке №" + deal.getPid()
                        + " до \"" + onlyPaysStatus.getDescription() + "\".");
            }
            try {
                Thread.sleep(200);
            } catch (InterruptedException ignored) {
            }
        }
    }

    @Scheduled(cron = "*/5 * * * * *")
    @Async
    public void updateEvoPayStatuses() {
        List<Deal> deals = readDealService.getAllNotFinalEvoPayStatuses();
        if (Objects.isNull(deals) || deals.isEmpty()) {
            return;
        }
        List<OrderResponse> orderResponses = evoPayMerchantService.getOrders(EvoPayStatus.NOT_FINAL_STATUSES);
        for (OrderResponse orderResponse : orderResponses) {
            for (Deal deal: deals) {
                if (orderResponse.getId().equals(deal.getMerchantOrderId())) {
                    EvoPayStatus status = orderResponse.getOrderStatus();
                    if (Objects.nonNull(status) && !Objects.equals(status, EvoPayStatus.valueOf(deal.getMerchantOrderStatus()))) {
                        deal.setMerchantOrderStatus(status.name());
                        modifyDealRepository.save(deal);
                        notifier.merchantUpdateStatus(deal.getPid(), "EvoPay обновил статус по сделке №" + deal.getPid()
                                + " до \"" + status.getDescription() + "\".");
                    }
                }
            }
        }
    }

    @Scheduled(cron = "*/5 * * * * *")
    @Async
    public void updateNicePayStatuses() {
        List<Deal> deals = readDealService.getAllNotFinalNicePayStatuses();
        if (Objects.isNull(deals) || deals.isEmpty()) {
            return;
        }
        for (Deal deal: deals) {
            GetOrderResponse getOrderResponse = nicePayMerchantService.getOrder(deal.getMerchantOrderId());
            if (!"success".equals(getOrderResponse.getStatus())) {
                log.warn("Не удалось получить статус для сделки {}, ответ неуспешен: {}", deal.getPid(), getOrderResponse);
                continue;
            }
            NicePayStatus nicePayStatus = getOrderResponse.getData().getStatus();
            if (!NicePayStatus.valueOf(deal.getMerchantOrderStatus()).equals(nicePayStatus)) {
                deal.setMerchantOrderStatus(nicePayStatus.name());
                modifyDealRepository.save(deal);
                notifier.merchantUpdateStatus(deal.getPid(), "NicePay обновил статус по сделке №" + deal.getPid()
                        + " до \"" + nicePayStatus.getDescription() + "\".");
            }
            try {
                Thread.sleep(200);
            } catch (InterruptedException ignored) {
            }
        }
    }
}
