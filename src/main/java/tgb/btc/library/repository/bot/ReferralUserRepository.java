package tgb.btc.library.repository.bot;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import tgb.btc.library.bean.bot.ReferralUser;
import tgb.btc.library.repository.BaseRepository;

@Repository
@Transactional
public interface ReferralUserRepository  extends BaseRepository<ReferralUser> {
}
