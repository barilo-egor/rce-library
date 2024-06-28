package tgb.btc.library.service.bean.web;

import org.apache.commons.lang.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tgb.btc.library.bean.web.api.ApiDeal;
import tgb.btc.library.bean.web.api.ApiUser;
import tgb.btc.library.constants.enums.web.ApiDealStatus;
import tgb.btc.library.interfaces.service.bean.web.IApiDealService;
import tgb.btc.library.repository.BaseRepository;
import tgb.btc.library.repository.web.ApiDealRepository;
import tgb.btc.library.service.bean.BasePersistService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class ApiDealService extends BasePersistService<ApiDeal> implements IApiDealService {

    private ApiDealRepository apiDealRepository;

    @Autowired
    public ApiDealService(BaseRepository<ApiDeal> baseRepository) {
        super(baseRepository);
    }

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

    @Override
    public ApiDeal getByPid(Long pid) {
        return apiDealRepository.getByPid(pid);
    }

    @Override
    public long countByPid(Long pid) {
        return apiDealRepository.countByPid(pid);
    }

    @Override
    public ApiDealStatus getApiDealStatusByPid(Long pid) {
        return apiDealRepository.getApiDealStatusByPid(pid);
    }

    @Override
    public long countByApiDealStatusAndApiUser_Pid(ApiDealStatus status, Long userPid) {
        return apiDealRepository.countByApiDealStatusAndApiUser_Pid(status, userPid);
    }

    @Override
    public List<Long> getActiveDealsPids() {
        return apiDealRepository.getActiveDealsPids();
    }

    @Override
    public List<ApiDeal> getByDateBetween(LocalDateTime startDate, LocalDateTime endDate, ApiDealStatus apiDealStatus) {
        return apiDealRepository.getByDateBetween(startDate, endDate, apiDealStatus);
    }

    @Override
    public List<ApiDeal> getByDateBetweenExcludeEnd(LocalDateTime startDate, LocalDateTime endDate,
            ApiDealStatus apiDealStatus) {
        return apiDealRepository.getByDateBetweenExcludeEnd(startDate, endDate, apiDealStatus);
    }

    @Override
    public List<ApiDeal> getByDateBetweenExcludeStart(LocalDateTime startDate, LocalDateTime endDate,
            ApiDealStatus apiDealStatus) {
        return apiDealRepository.getByDateBetweenExcludeStart(startDate, endDate, apiDealStatus);
    }

    @Override
    public List<ApiDeal> getDealsByPids(List<Long> dealsPids) {
        return apiDealRepository.getDealsByPids(dealsPids);
    }

    @Override
    public List<ApiDeal> getByApiUserId(String id) {
        return apiDealRepository.getByApiUserId(id);
    }

    @Override
    public LocalDateTime getDateTimeByPid(Long pid) {
        return apiDealRepository.getDateTimeByPid(pid);
    }

    @Override
    public Long getFirstDealPid(Long userPid) {
        return apiDealRepository.getFirstDealPid(userPid);
    }

    @Override
    public ApiDeal getFirstDeal(Long userPid) {
        return apiDealRepository.getFirstDeal(userPid);
    }

    @Override
    public ApiDeal getLastDeal(Long userPid) {
        return apiDealRepository.getLastDeal(userPid);
    }

    @Override
    public List<Long> getPidsByQuery(String query) {
        return apiDealRepository.getPidsByQuery(query);
    }

    @Override
    public List<ApiDeal> getAcceptedByDateTimeBetween(LocalDateTime startDate, LocalDateTime endDate, Long userPid) {
        return apiDealRepository.getAcceptedByDateTimeBetween(startDate, endDate, userPid);
    }

    @Override
    public List<ApiDeal> getAcceptedByDateTimeBetweenExcludeEnd(LocalDateTime startDate, LocalDateTime endDate) {
        return apiDealRepository.getAcceptedByDateTimeBetweenExcludeEnd(startDate, endDate);
    }

    @Override
    public List<ApiDeal> getAcceptedByDateTimeAfter(LocalDateTime dateTime, Long userPid) {
        return apiDealRepository.getAcceptedByDateTimeAfter(dateTime, userPid);
    }

    @Override
    public List<ApiDeal> getAcceptedByDateTimeBefore(LocalDateTime dateTime, Long userPid) {
        return apiDealRepository.getAcceptedByDateTimeBefore(dateTime, userPid);
    }

    @Override
    public Long getApiUserPidByDealPid(Long pid) {
        return apiDealRepository.getApiUserPidByDealPid(pid);
    }

    @Override
    public Long getCountByTokenAndPid(String token, Long dealPid) {
        return apiDealRepository.getCountByTokenAndPid(token, dealPid);
    }

    @Override
    public void updateApiDealStatusByPid(ApiDealStatus status, Long pid) {
        apiDealRepository.updateApiDealStatusByPid(status, pid);
    }

    @Override
    public void updateDealsApiUser(ApiUser apiUser, String oldApiUserId) {
        apiDealRepository.updateDealsApiUser(apiUser, oldApiUserId);
    }

    @Override
    public void deleteByApiUserId(String apiUserId) {
        apiDealRepository.deleteByApiUserId(apiUserId);
    }

}
