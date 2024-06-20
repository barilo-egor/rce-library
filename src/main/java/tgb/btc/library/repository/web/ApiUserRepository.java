package tgb.btc.library.repository.web;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import tgb.btc.library.bean.web.WebUser;
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

    @Query("select pid from ApiUser where webUser.username=:username")
    Long getPidByUsername(String username);

    @Query("select u.webUser from ApiUser u where u.pid=:pid")
    WebUser getWebUser(Long pid);

    /**
     * DELETE
     */
    @Modifying
    @Query("delete from ApiUser where id=:id")
    void deleteById(String id);

    /**
     * UPDATE
     */
    @Modifying
    @Query("update ApiUser u set u.lastPaidDeal=:lastPaidDeal where u.pid=:userPid")
    void updateLastPidDeal(Long userPid, ApiDeal lastPaidDeal);

    @Modifying
    @Query("update ApiUser set webUser=:webUser where pid=:pid")
    void updateWebUser(Long pid, WebUser webUser);
}
