package tgb.btc.library.repository.bot.user;

import org.springframework.data.jpa.repository.Query;
import tgb.btc.library.bean.bot.ReferralUser;
import tgb.btc.library.bean.bot.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface ReadUserRepository {

    User findByChatId(Long chatId);

    @Query("select pid from User where chatId=:chatId")
    Long getPidByChatId(Long chatId);

    @Query("select step from User where chatId=:chatId")
    Integer getStepByChatId(Long chatId);

    @Query("select command from User where chatId=:chatId")
    String getCommandByChatId(Long chatId);

    boolean existsByChatId(Long chatId);

    @Query("select isAdmin from User where chatId=:chatId")
    boolean isAdminByChatId(Long chatId);

    @Query("select referralBalance from User where chatId=:chatId")
    Integer getReferralBalanceByChatId(Long chatId);

    @Query("select u.referralUsers from User u where u.chatId=:chatId")
    List<ReferralUser> getUserReferralsByChatId(Long chatId);

    User getByChatId(Long chatId);

    @Query("select chatId from User where isAdmin=true")
    List<Long> getAdminsChatIds();

    @Query("select bufferVariable from User where chatId=:chatId")
    String getBufferVariable(Long chatId);

    @Query("select chatId from User where isAdmin=false and isActive=true and isBanned=false")
    List<Long> getChatIdsNotAdminsAndIsActiveAndNotBanned();

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
