package tgb.btc.library.repository.bot.user;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tgb.btc.library.bean.bot.ReferralUser;
import tgb.btc.library.bean.bot.User;
import tgb.btc.library.constants.enums.bot.UserRole;
import tgb.btc.library.repository.BaseRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Repository
public interface ReadUserRepository extends BaseRepository<User> {

    User findByChatId(Long chatId);

    @Query("select pid from User where chatId=:chatId")
    Long getPidByChatId(Long chatId);

    @Query("select step from User where chatId=:chatId")
    Integer getStepByChatId(Long chatId);

    @Query("select command from User where chatId=:chatId")
    String getCommandByChatId(Long chatId);

    boolean existsByChatId(Long chatId);

    @Query("select userRole from User where chatId=:chatId")
    UserRole getUserRoleByChatId(Long chatId);

    @Query("select referralBalance from User where chatId=:chatId")
    Integer getReferralBalanceByChatId(Long chatId);

    @Query("select u.referralUsers from User u where u.chatId=:chatId")
    List<ReferralUser> getUserReferralsByChatId(Long chatId);

    // TODO удалить,самый первый метод точно такой же
    User getByChatId(Long chatId);

    @Query("select chatId from User where userRole='ADMIN'")
    List<Long> getAdminsChatIds();

    @Query("select chatId from User where userRole in (:roles)")
    List<Long> getChatIdsByRoles(Set<UserRole> roles);

    @Query("select bufferVariable from User where chatId=:chatId")
    String getBufferVariable(Long chatId);

    @Query("select chatId from User where userRole in(:roles) and isActive=true and isBanned=false")
    List<Long> getChatIdsForMailing(List<UserRole> roles);

    @Query("select isBanned from User where chatId=:chatId")
    Boolean getIsBannedByChatId(Long chatId);

    @Query("select chatId from User where isBanned=:isBanned")
    List<Long> getChatIdsByIsBanned(Boolean isBanned);

    @Query("select currentDeal from User where chatId=:chatId")
    Long getCurrentDealByChatId(Long chatId);

    @Query("select username from User where chatId=:chatId")
    String getUsernameByChatId(Long chatId);

    @Query("select charges from User where chatId=:chatId")
    Integer getChargesByChatId(Long chatId);

    @Query("select referralPercent from User where chatId=:chatId")
    BigDecimal getReferralPercentByChatId(Long chatId);

    @Query("select pid from User ")
    List<Long> getPids();

    @Query("select chatId from User where isNotificationsOn=:isNotificationsOn")
    List<Long> getChatIdsByIsNotificationsOn(Boolean isNotificationsOn);

    /**
     * Reports
     */

    @Query("select count(pid) from User where registrationDate between :localDateTime1 and :localDateTime2")
    Integer countByRegistrationDate(LocalDateTime localDateTime1, LocalDateTime localDateTime2);

    @Query("select chatId from User where registrationDate between :localDateTime1 and :localDateTime2 and fromChatId is not null")
    List<Long> getChatIdsByRegistrationDateAndFromChatIdNotNull(LocalDateTime localDateTime1, LocalDateTime localDateTime2);

    @Query("select chatId from User where pid=:pid")
    Long getChatIdByPid(Long pid);

    @Query("select pid,chatId,username from User where isBanned=false")
    List<Object[]> findAllForUsersReport();
}
