package tgb.btc.library.repository.bot.user;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tgb.btc.library.bean.bot.User;
import tgb.btc.library.constants.enums.bot.UserRole;
import tgb.btc.library.repository.BaseRepository;

import java.math.BigDecimal;

@Repository
public interface ModifyUserRepository extends BaseRepository<User> {

    @Modifying
    @Query("update User set isActive=:isActive where chatId=:chatId")
    void updateIsActiveByChatId(boolean isActive, Long chatId);

    /**
     * use userService.ban() and userService.unban()
     */
    @Modifying
    @Query("update User set isBanned=:isBanned where chatId=:chatId")
    void updateIsBannedByChatId(Long chatId, Boolean isBanned);

    @Modifying
    @Query("update User set currentDeal=:dealPid where chatId=:chatId")
    void updateCurrentDealByChatId(Long dealPid, Long chatId);

    @Modifying
    @Query("update User set referralBalance=:referralBalance where chatId=:chatId")
    void updateReferralBalanceByChatId(Integer referralBalance, Long chatId);

    @Modifying
    @Query("update User set charges=:charges where chatId=:chatId")
    void updateChargesByChatId(Integer charges, Long chatId);

    @Query("update User set referralPercent=:referralPercent where chatId=:chatId")
    @Modifying
    void updateReferralPercent(BigDecimal referralPercent, Long chatId);

    @Modifying
    @Query("update User set userRole=:userRole where chatId=:chatId")
    void updateUserRoleByChatId(UserRole userRole, Long chatId);
}
