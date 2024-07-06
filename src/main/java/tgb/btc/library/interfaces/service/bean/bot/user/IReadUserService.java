package tgb.btc.library.interfaces.service.bean.bot.user;

import tgb.btc.library.bean.bot.ReferralUser;
import tgb.btc.library.bean.bot.User;
import tgb.btc.library.interfaces.service.IBasePersistService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface IReadUserService extends IBasePersistService<User> {

    User findByChatId(Long chatId);

    Long getPidByChatId(Long chatId);

    Integer getStepByChatId(Long chatId);

    String getCommandByChatId(Long chatId);

    boolean existsByChatId(Long chatId);

    boolean isAdminByChatId(Long chatId);

    Integer getReferralBalanceByChatId(Long chatId);

    List<ReferralUser> getUserReferralsByChatId(Long chatId);

    User getByChatId(Long chatId);

    List<Long> getAdminsChatIds();

    String getBufferVariable(Long chatId);

    List<Long> getChatIdsForMailing();

    Boolean getIsBannedByChatId(Long chatId);

    List<Long> getChatIdsByIsBanned(Boolean isBanned);

    Long getCurrentDealByChatId(Long chatId);

    String getUsernameByChatId(Long chatId);

    Integer getChargesByChatId(Long chatId);

    BigDecimal getReferralPercentByChatId(Long chatId);

    List<Long> getPids();

    /**
     * Reports
     */

    Integer countByRegistrationDate(LocalDateTime localDateTime1, LocalDateTime localDateTime2);

    List<Long> getChatIdsByRegistrationDateAndFromChatIdNotNull(LocalDateTime localDateTime1, LocalDateTime localDateTime2);

    Long getChatIdByPid(Long pid);

    List<Object[]> findAllForUsersReport();
}
