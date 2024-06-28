package tgb.btc.library.service.bean.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tgb.btc.library.bean.bot.SpamBan;
import tgb.btc.library.bean.bot.User;
import tgb.btc.library.interfaces.service.bean.bot.ISpamBanService;
import tgb.btc.library.repository.BaseRepository;
import tgb.btc.library.repository.bot.SpamBanRepository;
import tgb.btc.library.repository.bot.UserRepository;
import tgb.btc.library.service.bean.BasePersistService;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SpamBanService extends BasePersistService<SpamBan> implements ISpamBanService {

    private SpamBanRepository spamBanRepository;

    private UserRepository userRepository;

    @Autowired
    public SpamBanService(BaseRepository<SpamBan> baseRepository) {
        super(baseRepository);
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setSpamBanRepository(SpamBanRepository spamBanRepository) {
        this.spamBanRepository = spamBanRepository;
    }

    public SpamBan save(Long chatId) {
        return spamBanRepository.save(new SpamBan(new User(userRepository.getPidByChatId(chatId)), LocalDateTime.now()));
    }

    @Override
    public Long getUserPidByPid(Long pid) {
        return spamBanRepository.getUserPidByPid(pid);
    }

    @Override
    public Long getUserChatIdByPid(Long pid) {
        return spamBanRepository.getUserChatIdByPid(pid);
    }

    @Override
    public List<Long> getPids() {
        return spamBanRepository.getPids();
    }

    @Override
    public void deleteByUser_Pid(Long userPid) {
        spamBanRepository.deleteByUser_Pid(userPid);
    }

    @Override
    public long countByPid(Long pid) {
        return spamBanRepository.countByPid(pid);
    }
}
