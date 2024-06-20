package tgb.btc.library.service.bean.web;

import org.apache.commons.lang.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tgb.btc.library.bean.web.api.ApiDeal;
import tgb.btc.library.constants.enums.web.ApiDealStatus;
import tgb.btc.library.repository.web.ApiDealRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Objects;

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

    public List<ApiDeal> getAcceptedByDateBetween(Long apiUserPid, Date start, Date end, Boolean isRange) {
        if (BooleanUtils.isNotTrue(isRange)) {
            if (Objects.isNull(start)) return apiDealRepository.findAll();
            return getAcceptedByDate(start.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), apiUserPid);
        } else {
            if (Objects.nonNull(start) && Objects.isNull(end)) {
                return apiDealRepository.getAcceptedByDateTimeAfter(
                        start.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay(), apiUserPid);
            } else if (Objects.isNull(start) && Objects.nonNull(end)) {
                return apiDealRepository.getAcceptedByDateTimeBefore(
                        end.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().plusDays(1).atStartOfDay(),
                        apiUserPid);
            }
            return apiDealRepository.getAcceptedByDateTimeBetween(
                    start.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay(),
                    end.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().plusDays(1).atStartOfDay(),
                    apiUserPid);
        }
    }

    public List<ApiDeal> getAcceptedByDate(LocalDate date, Long apiUserPid) {
        return apiDealRepository.getAcceptedByDateTimeBetween(
                date.atStartOfDay(), date.plusDays(1).atStartOfDay(), apiUserPid);
    }
}
