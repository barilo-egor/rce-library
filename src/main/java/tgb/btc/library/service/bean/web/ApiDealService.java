package tgb.btc.library.service.bean.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tgb.btc.library.bean.web.api.ApiDeal;
import tgb.btc.library.constants.enums.web.ApiDealStatus;
import tgb.btc.library.repository.web.ApiDealRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class ApiDealService {

    private ApiDealRepository apiDealRepository;

    @Autowired
    public void setApiDealRepository(ApiDealRepository apiDealRepository) {
        this.apiDealRepository = apiDealRepository;
    }

    public List<ApiDeal> getAcceptedByDate(LocalDateTime dateTime) {
        LocalDateTime startDay = dateTime.truncatedTo(ChronoUnit.DAYS);
        LocalDateTime endDay = dateTime
                .plus(1, ChronoUnit.DAYS)
                .truncatedTo(ChronoUnit.DAYS)
                .minusNanos(1);
        return apiDealRepository.getByDateBetween(startDay, endDay, ApiDealStatus.ACCEPTED);
    }

    public List<ApiDeal> getAcceptedByDateBetween(LocalDateTime start, LocalDateTime end) {
        LocalDateTime startDay = start.truncatedTo(ChronoUnit.DAYS);
        LocalDateTime endDay = end
                .plus(1, ChronoUnit.DAYS)
                .truncatedTo(ChronoUnit.DAYS)
                .minusNanos(1);
        return apiDealRepository.getByDateBetween(startDay, endDay, ApiDealStatus.ACCEPTED);
    }

}
