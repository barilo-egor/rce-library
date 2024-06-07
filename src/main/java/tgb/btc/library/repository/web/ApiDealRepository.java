package tgb.btc.library.repository.web;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tgb.btc.library.bean.web.api.ApiDeal;
import tgb.btc.library.bean.web.api.ApiUser;
import tgb.btc.library.constants.enums.web.ApiDealStatus;
import tgb.btc.library.repository.BaseRepository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ApiDealRepository extends BaseRepository<ApiDeal> {

    ApiDeal getByPid(Long pid);

    long countByPid(Long pid);

    @Query("select apiDealStatus from ApiDeal where pid=:pid")
    ApiDealStatus getApiDealStatusByPid(Long pid);

    long countByApiDealStatusAndApiUser_Pid(ApiDealStatus status, Long userPid);

    @Query("select pid from ApiDeal where apiDealStatus='PAID'")
    List<Long> getActiveDealsPids();

    @Query("from ApiDeal d where (d.dateTime BETWEEN :startDate AND :endDate) and d.apiDealStatus=:apiDealStatus")
    List<ApiDeal> getByDateBetween(LocalDateTime startDate, LocalDateTime endDate, ApiDealStatus apiDealStatus);

    @Query("from ApiDeal d where d.dateTime >= :startDate AND d.dateTime < :endDate and d.apiDealStatus=:apiDealStatus")
    List<ApiDeal> getByDateBetweenExcludeEnd(LocalDateTime startDate, LocalDateTime endDate, ApiDealStatus apiDealStatus);

    @Query("from ApiDeal where pid in (:dealsPids)")
    List<ApiDeal> getDealsByPids(List<Long> dealsPids);

    @Query("from ApiDeal where apiUser.id=:id")
    List<ApiDeal> getByApiUserId(String id);

    @Query("select dateTime from ApiDeal where pid=:pid")
    LocalDateTime getDateTimeByPid(Long pid);

    @Query("select pid from ApiDeal where dateTime = (select min(dateTime) from ApiDeal where apiUser.pid=:userPid) and apiUser.pid=:userPid")
    Long getFirstDealPid(Long userPid);

    @Query("from ApiDeal where dateTime = (select min(dateTime) from ApiDeal where apiUser.pid=:userPid) and apiUser.pid=:userPid")
    ApiDeal getFirstDeal(Long userPid);

    @Query("from ApiDeal where dateTime = (select max(dateTime) from ApiDeal where apiUser.pid=:userPid) and apiUser.pid=:userPid")
    ApiDeal getLastDeal(Long userPid);

    /**
     * UPDATE
     */
    @Modifying
    @Query("update ApiDeal set apiDealStatus=:status where pid=:pid")
    void updateApiDealStatusByPid(ApiDealStatus status, Long pid);

    @Modifying
    @Query("update ApiDeal set apiUser=:apiUser where apiUser.id=:oldApiUserId")
    void updateDealsApiUser(ApiUser apiUser, String oldApiUserId);

    @Modifying
    @Query("delete from ApiDeal where apiUser.id=:apiUserId")
    void deleteByApiUserId(String apiUserId);
}
