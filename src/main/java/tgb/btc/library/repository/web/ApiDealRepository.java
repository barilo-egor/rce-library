package tgb.btc.library.repository.web;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tgb.btc.library.bean.web.api.ApiDeal;
import tgb.btc.library.bean.web.api.ApiUser;
import tgb.btc.library.constants.enums.ApiDealType;
import tgb.btc.library.constants.enums.bot.ReceiptFormat;
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

    @Query("select pid from ApiDeal where cast(pid as string) like %:query")
    List<Long> getPidsByQuery(String query);

    @Query("from ApiDeal d where (d.dateTime BETWEEN :startDate AND :endDate) and d.apiDealStatus='ACCEPTED' and d.apiUser.pid=:userPid")
    List<ApiDeal> getAcceptedByDateTimeBetween(LocalDateTime startDate, LocalDateTime endDate, Long userPid);

    @Query("from ApiDeal d where d.dateTime>=:dateTime and d.apiDealStatus='ACCEPTED' and d.apiUser.pid=:userPid")
    List<ApiDeal> getAcceptedByDateTimeAfter(LocalDateTime dateTime, Long userPid);

    @Query("from ApiDeal d where d.dateTime<=:dateTime and d.apiDealStatus='ACCEPTED' and d.apiUser.pid=:userPid")
    List<ApiDeal> getAcceptedByDateTimeBefore(LocalDateTime dateTime, Long userPid);

    @Query("select count(d.pid) from ApiDeal d where d.apiUser.token=:token and d.pid=:dealPid")
    Long getCountByTokenAndPid(String token, Long dealPid);

    @Query("select apiDealType from ApiDeal where pid=:pid")
    ApiDealType getApiDealTypeByPid(Long pid);

    @Query("select checkImageId from ApiDeal where pid=:pid")
    String getCheckImageIdByPid(Long pid);

    @Query("select receiptFormat from ApiDeal where pid=:pid")
    ReceiptFormat getReceiptFormatByPid(Long pid);

    /**
     * UPDATE
     */
    @Modifying
    @Query("update ApiDeal set apiDealStatus=:status where pid=:pid")
    void updateApiDealStatusByPid(ApiDealStatus status, Long pid);

    @Modifying
    @Query("update ApiDeal deal set deal.apiUser=:apiUser where deal.apiUser = (select u from ApiUser u where u.id = :oldApiUserId)")
    void updateDealsApiUser(ApiUser apiUser, String oldApiUserId);

    @Modifying
    @Query("delete from ApiDeal deal where deal.apiUser = (select u from ApiUser u where u.id = :apiUserId)")
    void deleteByApiUserId(String apiUserId);

    @Modifying
    @Query("update ApiDeal set apiRequisite=null where apiRequisite.pid=:apiRequisitePid")
    void dropApiRequisite(Long apiRequisitePid);

    @Modifying
    @Query("update ApiDeal set apiPaymentType=null where apiPaymentType.pid=:apiPaymentTypePid")
    void dropApiPaymentType(Long apiPaymentTypePid);
}
