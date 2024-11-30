package tgb.btc.library.service.schedule;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tgb.btc.api.web.INotificationsAPI;
import tgb.btc.api.web.INotifier;
import tgb.btc.library.bean.web.api.ApiDeal;
import tgb.btc.library.constants.enums.web.ApiDealStatus;
import tgb.btc.library.interfaces.service.bean.web.IApiDealService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Component
@Slf4j
public class ApiDealDeclineScheduler {

    private final IApiDealService apiDealService;

    private final Integer declineTime;

    private final INotificationsAPI notificationsAPI;

    private final INotifier notifier;

    public ApiDealDeclineScheduler(IApiDealService apiDealService,
                                   @Value("${api.deal.decline.time:15}") Integer declineTime,
                                   INotificationsAPI notificationsAPI, INotifier notifier) {
        this.apiDealService = apiDealService;
        this.declineTime = declineTime;
        this.notificationsAPI = notificationsAPI;
        this.notifier = notifier;
        log.debug("Автоматическое отклонение апи заявок загружено в контекст.");
    }

    @Async
    @Scheduled(fixedDelay = 10000)
    public void delete() {
        List<ApiDeal> apiDeals = apiDealService.getAllPaid();
        LocalDateTime bound = LocalDateTime.now().minusMinutes(declineTime);
        for (ApiDeal apiDeal : apiDeals) {
            if (Objects.isNull(apiDeal.getPaidDateTime())) continue;
            LocalDateTime paidDateTime = apiDeal.getPaidDateTime();
            if (paidDateTime.isBefore(bound)) {
                apiDeal.setApiDealStatus(ApiDealStatus.DECLINED);
                apiDealService.save(apiDeal);
                notificationsAPI.apiDealDeclined(apiDeal.getPid());
                notifier.apiDealDeclined(apiDeal.getPid());
            }
        }
    }
}
