package tgb.btc.library.interfaces.service.bean.web;

import tgb.btc.library.bean.web.api.ApiDeal;
import tgb.btc.library.bean.web.api.ApiUser;
import tgb.btc.library.constants.enums.web.ApiDealStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface IApiDealService {

    List<ApiDeal> getAcceptedByDate(LocalDateTime dateTime);

    List<ApiDeal> getAcceptedByDateBetween(LocalDateTime start, LocalDateTime end);

    List<ApiDeal> getAcceptedByDateBetween(Long apiUserPid, Date start, Date end, Boolean isRange);

    List<ApiDeal> getAcceptedByDate(LocalDate date, Long apiUserPid);

    ApiDeal getByPid(Long pid);

    long countByPid(Long pid);

    ApiDealStatus getApiDealStatusByPid(Long pid);

    long countByApiDealStatusAndApiUser_Pid(ApiDealStatus status, Long userPid);

    List<Long> getActiveDealsPids();

    List<ApiDeal> getByDateBetween(LocalDateTime startDate, LocalDateTime endDate, ApiDealStatus apiDealStatus);

    List<ApiDeal> getByDateBetweenExcludeEnd(LocalDateTime startDate, LocalDateTime endDate, ApiDealStatus apiDealStatus);

    List<ApiDeal> getByDateBetweenExcludeStart(LocalDateTime startDate, LocalDateTime endDate, ApiDealStatus apiDealStatus);

    List<ApiDeal> getDealsByPids(List<Long> dealsPids);

    List<ApiDeal> getByApiUserId(String id);

    LocalDateTime getDateTimeByPid(Long pid);

    Long getFirstDealPid(Long userPid);

    ApiDeal getFirstDeal(Long userPid);

    ApiDeal getLastDeal(Long userPid);

    List<Long> getPidsByQuery(String query);

    List<ApiDeal> getAcceptedByDateTimeBetween(LocalDateTime startDate, LocalDateTime endDate, Long userPid);

    List<ApiDeal> getAcceptedByDateTimeBetweenExcludeEnd(LocalDateTime startDate, LocalDateTime endDate);

    List<ApiDeal> getAcceptedByDateTimeAfter(LocalDateTime dateTime, Long userPid);

    List<ApiDeal> getAcceptedByDateTimeBefore(LocalDateTime dateTime, Long userPid);

    Long getApiUserPidByDealPid(Long pid);

    Long getCountByTokenAndPid(String token, Long dealPid);

    /**
     * UPDATE
     */
    void updateApiDealStatusByPid(ApiDealStatus status, Long pid);

    void updateDealsApiUser(ApiUser apiUser, String oldApiUserId);

    void deleteByApiUserId(String apiUserId);
}
