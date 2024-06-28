package tgb.btc.library.repository.bot;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import tgb.btc.library.bean.bot.User;
import tgb.btc.library.repository.BaseRepository;
import tgb.btc.library.repository.bot.user.ModifyUserRepository;
import tgb.btc.library.repository.bot.user.ReadUserRepository;

@Repository
@Transactional
public interface UserRepository extends BaseRepository<User>, ReadUserRepository, ModifyUserRepository {
}
