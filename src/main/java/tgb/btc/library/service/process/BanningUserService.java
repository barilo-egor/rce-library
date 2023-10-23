package tgb.btc.library.service.process;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tgb.btc.library.repository.bot.UserRepository;

@Service
public class BanningUserService {

    private BannedUserCache bannedUserCache;

    private UserRepository userRepository;

    @Autowired
    public void setBannedUserCache(BannedUserCache bannedUserCache) {
        this.bannedUserCache = bannedUserCache;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void ban(Long chatId) {
        bannedUserCache.add(chatId, true);
        userRepository.updateIsBannedByChatId(chatId, true);
    }

    public void unban(Long chatId) {
        bannedUserCache.add(chatId, false);
        userRepository.updateIsBannedByChatId(chatId, false);
    }

}
