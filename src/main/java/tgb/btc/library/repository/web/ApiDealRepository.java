package tgb.btc.library.repository.web;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tgb.btc.library.bean.web.api.ApiDeal;
import tgb.btc.library.constants.enums.web.ApiDealStatus;
import tgb.btc.library.repository.BaseRepository;

import java.util.List;

@Repository
public interface ApiDealRepository extends BaseRepository<ApiDeal> {

    ApiDeal getByPid(Long pid);

    long countByPid(Long pid);

    @Query("select apiDealStatus from ApiDeal where pid=:pid")
    ApiDealStatus getApiDealStatusByPid(Long pid);

    @Modifying
    @Query("update ApiDeal set apiDealStatus=:status where pid=:pid")
    void updateApiDealStatusByPid(ApiDealStatus status, Long pid);

    long countByApiDealStatusAndApiUser_Pid(ApiDealStatus status, Long userPid);

    @Query("select pid from ApiDeal where apiDealStatus='PAID'")
    List<Long> getActiveDealsPids();
}
