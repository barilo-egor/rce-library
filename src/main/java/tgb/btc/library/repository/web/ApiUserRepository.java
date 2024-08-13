package tgb.btc.library.repository.web;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import tgb.btc.library.bean.bot.GroupChat;
import tgb.btc.library.bean.web.WebUser;
import tgb.btc.library.bean.web.api.ApiDeal;
import tgb.btc.library.bean.web.api.ApiUser;
import tgb.btc.library.constants.enums.bot.FiatCurrency;
import tgb.btc.library.repository.BaseRepository;

import java.util.List;

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

    @Query("select apiUser.pid from ApiUser apiUser join apiUser.webUsers webUser where webUser.username=:username")
    Long getPidByUsername(String username);

    @Query("select apiUser.fiatCurrency from ApiUser apiUser join apiUser.webUsers webUser where webUser.username=:username")
    FiatCurrency getFiatCurrencyByUsername(String username);

    @Query("from ApiUser apiUser join apiUser.webUsers webUser where webUser.username=:username")
    ApiUser getByUsername(String username);

    @Query("select u.webUsers from ApiUser u where u.pid=:pid")
    List<WebUser> getWebUsers(Long pid);

    @Query("from ApiUser where groupChat.pid = :groupChatPid")
    ApiUser getByGroupChatPid(Long groupChatPid);

    @Query("from ApiUser where groupChat.chatId = :groupChatId")
    ApiUser getByGroupChatId(Long groupChatId);

    @Query("select u.id from ApiUser u join u.paymentTypes pt where pt.pid=:paymentTypePid")
    List<String> getIdByPaymentTypePid(Long paymentTypePid);

    @Query("select u.id from ApiUser u left join u.paymentTypes pt on pt.pid = :paymentTypePid where pt is null")
    List<String> getIdExcludePaymentTypePid(Long paymentTypePid);

    @Query("select id from ApiUser where lower(id) like lower(:query)")
    List<String> getIdLikeQuery(String query);

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
    @Query("update ApiUser set groupChat = :groupChat where pid = :apiUserPid")
    void updateGroupChat(GroupChat groupChat, Long apiUserPid);
}
