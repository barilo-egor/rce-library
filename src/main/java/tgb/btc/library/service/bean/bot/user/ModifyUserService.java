package tgb.btc.library.service.bean.bot.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tgb.btc.library.bean.bot.User;
import tgb.btc.library.constants.enums.bot.UserRole;
import tgb.btc.library.interfaces.service.bean.bot.user.IModifyUserService;
import tgb.btc.library.repository.BaseRepository;
import tgb.btc.library.repository.bot.user.ModifyUserRepository;
import tgb.btc.library.repository.bot.user.ReadUserRepository;
import tgb.btc.library.service.bean.BasePersistService;

import java.math.BigDecimal;

@Service
@Transactional
public class ModifyUserService extends BasePersistService<User> implements IModifyUserService {

    private ModifyUserRepository modifyUserRepository;

    private ReadUserRepository readUserRepository;

    @Autowired
    public void setReadUserRepository(ReadUserRepository readUserRepository) {
        this.readUserRepository = readUserRepository;
    }

    @Autowired
    public void setModifyUserRepository(ModifyUserRepository modifyUserRepository) {
        this.modifyUserRepository = modifyUserRepository;
    }

    @Override
    protected BaseRepository<User> getBaseRepository() {
        return modifyUserRepository;
    }

    @Override
    public User save(User user) {
        return modifyUserRepository.save(user);
    }

    @Override
    public void setDefaultValues(Long chatId) {
        modifyUserRepository.setDefaultValues(chatId);
    }

    @Override
    public void nextStep(Long chatId, String command) {
        modifyUserRepository.nextStep(chatId, command);
    }

    @Override
    public void nextStep(Long chatId) {
        modifyUserRepository.nextStep(chatId);
    }

    @Override
    public void previousStep(Long chatId) {
        modifyUserRepository.previousStep(chatId);
    }

    @Override
    public void updateBufferVariable(Long chatId, String bufferVariable) {
        modifyUserRepository.updateBufferVariable(chatId, bufferVariable);
    }

    @Override
    public void updateIsActiveByChatId(boolean isActive, Long chatId) {
        modifyUserRepository.updateIsActiveByChatId(isActive, chatId);
    }

    @Override
    public void updateIsBannedByChatId(Long chatId, Boolean isBanned) {
        modifyUserRepository.updateIsBannedByChatId(chatId, isBanned);
    }

    @Override
    public void updateCurrentDealByChatId(Long dealPid, Long chatId) {
        modifyUserRepository.updateCurrentDealByChatId(dealPid, chatId);
    }

    @Override
    public void updateCommandByChatId(String command, Long chatId) {
        modifyUserRepository.updateCommandByChatId(command, chatId);
    }

    @Override
    public void updateReferralBalanceByChatId(Integer referralBalance, Long chatId) {
        modifyUserRepository.updateReferralBalanceByChatId(referralBalance, chatId);
    }

    @Override
    public void updateChargesByChatId(Integer charges, Long chatId) {
        modifyUserRepository.updateChargesByChatId(charges, chatId);
    }

    @Override
    public void updateReferralPercent(BigDecimal referralPercent, Long chatId) {
        modifyUserRepository.updateReferralPercent(referralPercent, chatId);
    }

    @Override
    public void updateStepAndCommandByChatId(Long chatId, String command, Integer step) {
        modifyUserRepository.updateStepAndCommandByChatId(chatId, command, step);
    }

    @Override
    public void updateStepByChatId(Long chatId, Integer step) {
        modifyUserRepository.updateStepByChatId(chatId, step);
    }

    @Override
    public void updateUserRoleByChatId(UserRole userRole, Long chatId) {
        modifyUserRepository.updateUserRoleByChatId(userRole, chatId);
    }

    @Override
    public void updateIsNotificationsOn(Long chatId, Boolean isNotificationsOn) {
        User user = readUserRepository.findByChatId(chatId);
        user.setNotificationsOn(isNotificationsOn);
        modifyUserRepository.save(user);
    }

}
