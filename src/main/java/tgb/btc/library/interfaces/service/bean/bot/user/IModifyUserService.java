package tgb.btc.library.interfaces.service.bean.bot.user;

import tgb.btc.library.bean.bot.User;

import java.math.BigDecimal;

public interface IModifyUserService {

    User save(User user);

    void setDefaultValues(Long chatId);

    void nextStep(Long chatId, String command);

    void nextStep(Long chatId);

    void previousStep(Long chatId);

    void updateBufferVariable(Long chatId, String bufferVariable);

    void updateIsActiveByChatId(boolean isActive, Long chatId);

    /**
     * use userService.ban() and userService.unban()
     */
    void updateIsBannedByChatId(Long chatId, Boolean isBanned);

    void updateCurrentDealByChatId(Long dealPid, Long chatId);

    void updateCommandByChatId(String command, Long chatId);

    void updateReferralBalanceByChatId(Integer referralBalance, Long chatId);

    void updateChargesByChatId(Integer charges, Long chatId);

    void updateReferralPercent(BigDecimal referralPercent, Long chatId);

    void updateStepAndCommandByChatId(Long chatId, String command, Integer step);

    void updateStepByChatId(Long chatId, Integer step);

    void updateIsAdminByChatId(Long chatId, Boolean isAdmin);
}
