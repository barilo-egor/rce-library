package tgb.btc.library.service.bean.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tgb.btc.library.bean.bot.ReferralUser;
import tgb.btc.library.bean.bot.User;
import tgb.btc.library.exception.BaseException;
import tgb.btc.library.repository.BaseRepository;
import tgb.btc.library.repository.bot.UserRepository;
import tgb.btc.library.service.bean.BasePersistService;

import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class UserService extends BasePersistService<User> {

    private UserRepository userRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Integer getStepByChatId(Long chatId) {
        return userRepository.getStepByChatId(chatId);
    }

    public boolean existByChatId(Long chatId) {
        return userRepository.existsByChatId(chatId);
    }

    public void setDefaultValues(Long chatId) {
        userRepository.setDefaultValues(chatId);
    }

    public boolean isAdminByChatId(Long chatId) {
        return userRepository.isAdminByChatId(chatId);
    }

    public User findByChatId(Long chatId) {
        return userRepository.findByChatId(chatId);
    }

    public Integer getReferralBalanceByChatId(Long chatId) {
        return userRepository.getReferralBalanceByChatId(chatId);
    }

    public List<ReferralUser> getUserReferralsByChatId(@Param("chatId") Long chatId) {
        return userRepository.getUserReferralsByChatId(chatId);
    }

    public void nextStep(Long chatId) {
        userRepository.nextStep(chatId);
    }

    public List<Long> getAdminsChatIds() {
        return userRepository.getAdminsChatIds();
    }

    @Override
    protected BaseRepository<User> getBaseRepository() {
        return userRepository;
    }

    @Override
    public User save(User user) {
        if (user.getReferralBalance() < 0)
            throw new BaseException("Сохранения пользователя невозможно, т.к. реферальный баланс отрицателен. " + user);
        return userRepository.save(user);
    }

    public void updateBufferVariable(Long chatId, String bufferVariable) {
        userRepository.updateBufferVariable(chatId, bufferVariable);
    }

    public String getBufferVariable(Long chatId) {
        return userRepository.getBufferVariable(chatId);
    }

    public List<Long> getChatIdsNotAdminsAndIsActiveAndNotBanned() {
        return userRepository.getChatIdsNotAdminsAndIsActiveAndNotBanned();
    }

    public void updateIsActiveByChatId(boolean isActive, Long chatId) {
        userRepository.updateIsActiveByChatId(isActive, chatId);
    }

    public void updateCurrentDealByChatId(Long dealPid, Long chatId) {
        userRepository.updateCurrentDealByChatId(dealPid, chatId);
    }

    public Long getCurrentDealByChatId(Long chatId) {
        return userRepository.getCurrentDealByChatId(chatId);
    }

    public String getUsernameByChatId(Long chatId) {
        return userRepository.getUsernameByChatId(chatId);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public void updateReferralBalanceByChatId(Integer referralBalance, Long chatId) {
        userRepository.updateReferralBalanceByChatId(referralBalance, chatId);
    }

    public void updateChargesByChatId(Integer charges, Long chatId) {
        userRepository.updateChargesByChatId(charges, chatId);
    }

    public Integer getChargesByChatId(Long chatId) {
        return userRepository.getChargesByChatId(chatId);
    }

    public boolean isReferralBalanceEmpty(Long chatId) {
        Integer referralBalance = userRepository.getReferralBalanceByChatId(chatId);
        return Objects.nonNull(referralBalance) && referralBalance == 0;
    }
}
