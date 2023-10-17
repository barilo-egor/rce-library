package tgb.btc.library.service.bean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tgb.btc.library.bean.bot.ReferralUser;
import tgb.btc.library.repository.BaseRepository;
import tgb.btc.library.repository.bot.ReferralUserRepository;

@Service
public class ReferralUserService extends BasePersistService<ReferralUser> {
    private final ReferralUserRepository referralUserRepository;

    @Autowired
    public ReferralUserService(BaseRepository<ReferralUser> baseRepository,
                               ReferralUserRepository referralUserRepository) {
        super(baseRepository);
        this.referralUserRepository = referralUserRepository;
    }

    public ReferralUser save(ReferralUser referralUser) {
        return referralUserRepository.save(referralUser);
    }
}
