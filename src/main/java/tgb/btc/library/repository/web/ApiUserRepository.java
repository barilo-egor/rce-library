package tgb.btc.library.repository.web;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import tgb.btc.library.bean.web.api.ApiDeal;
import tgb.btc.library.bean.web.api.ApiUser;
import tgb.btc.library.repository.BaseRepository;

@Repository
@Transactional
public interface ApiUserRepository extends BaseRepository<ApiUser> {

    long countByToken(String token);

    ApiUser getByToken(String token);

    long countById(String id);

    @Query("select isBanned from ApiUser where pid=:pid")
    Boolean isBanned(Long pid);

    @Query("select pid from ApiUser where token=:token")
    Long getPidByToken(String token);

    @Query("from ApiUser where id=:id")
    ApiUser getById(String id);

    @Query("select lastPaidDeal.pid from ApiUser where pid=:pid")
    Long getLastPaidDealPidByUserPid(Long pid);

    @Query("select u.lastPaidDeal from ApiUser u where u.pid=:userPid")
    ApiDeal getLastPaidDeal(Long userPid);

    /**
     * DELETE
     */
    @Modifying
    @Query("delete from ApiUser where id=:id")
    void deleteById(String id);
}
