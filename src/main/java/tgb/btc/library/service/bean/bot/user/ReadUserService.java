package tgb.btc.library.service.bean.bot.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tgb.btc.library.bean.bot.ReferralUser;
import tgb.btc.library.bean.bot.User;
import tgb.btc.library.interfaces.service.bean.bot.user.IReadUserService;
import tgb.btc.library.repository.BaseRepository;
import tgb.btc.library.repository.bot.user.ReadUserRepository;
import tgb.btc.library.service.bean.BasePersistService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReadUserService extends BasePersistService<User> implements IReadUserService {

    private ReadUserRepository readUserRepository;

    @Autowired
    public void setReadUserRepository(ReadUserRepository readUserRepository) {
        this.readUserRepository = readUserRepository;
    }

    @Autowired
    public ReadUserService(BaseRepository<User> baseRepository) {
        super(baseRepository);
    }

    @Override
    public User findByChatId(Long chatId) {
        return readUserRepository.findByChatId(chatId);
    }

    @Override
    public Long getPidByChatId(Long chatId) {
        return readUserRepository.getPidByChatId(chatId);
    }

    @Override
    public Integer getStepByChatId(Long chatId) {
        return readUserRepository.getStepByChatId(chatId);
    }

    @Override
    public String getCommandByChatId(Long chatId) {
        return readUserRepository.getCommandByChatId(chatId);
    }

    @Override
    public boolean existsByChatId(Long chatId) {
        return readUserRepository.existsByChatId(chatId);
    }

    @Override
    public boolean isAdminByChatId(Long chatId) {
        return readUserRepository.isAdminByChatId(chatId);
    }

    @Override
    public Integer getReferralBalanceByChatId(Long chatId) {
        return readUserRepository.getReferralBalanceByChatId(chatId);
    }

    @Override
    public List<ReferralUser> getUserReferralsByChatId(Long chatId) {
        return readUserRepository.getUserReferralsByChatId(chatId);
    }

    @Override
    public User getByChatId(Long chatId) {
        return readUserRepository.getByChatId(chatId);
    }

    @Override
    public List<Long> getAdminsChatIds() {
        return readUserRepository.getAdminsChatIds();
    }

    @Override
    public String getBufferVariable(Long chatId) {
        return readUserRepository.getBufferVariable(chatId);
    }

    @Override
    public List<Long> getChatIdsNotAdminsAndIsActiveAndNotBanned() {
        return readUserRepository.getChatIdsNotAdminsAndIsActiveAndNotBanned();
    }

    @Override
    public Boolean getIsBannedByChatId(Long chatId) {
        return readUserRepository.getIsBannedByChatId(chatId);
    }

    @Override
    public List<Long> getChatIdsByIsBanned(Boolean isBanned) {
        return readUserRepository.getChatIdsByIsBanned(isBanned);
    }

    @Override
    public Long getCurrentDealByChatId(Long chatId) {
        return readUserRepository.getCurrentDealByChatId(chatId);
    }

    @Override
    public String getUsernameByChatId(Long chatId) {
        return readUserRepository.getUsernameByChatId(chatId);
    }

    @Override
    public Integer getChargesByChatId(Long chatId) {
        return readUserRepository.getChargesByChatId(chatId);
    }

    @Override
    public BigDecimal getReferralPercentByChatId(Long chatId) {
        return readUserRepository.getReferralPercentByChatId(chatId);
    }

    @Override
    public List<Long> getPids() {
        return readUserRepository.getPids();
    }

    @Override
    public Integer countByRegistrationDate(LocalDateTime localDateTime1, LocalDateTime localDateTime2) {
        return readUserRepository.countByRegistrationDate(localDateTime1, localDateTime2);
    }

    @Override
    public List<Long> getChatIdsByRegistrationDateAndFromChatIdNotNull(LocalDateTime localDateTime1,
            LocalDateTime localDateTime2) {
        return readUserRepository.getChatIdsByRegistrationDateAndFromChatIdNotNull(localDateTime1, localDateTime2);
    }

    @Override
    public Long getChatIdByPid(Long pid) {
        return readUserRepository.getChatIdByPid(pid);
    }

    @Override
    public List<Object[]> findAllForUsersReport() {
        return readUserRepository.findAllForUsersReport();
    }

}
