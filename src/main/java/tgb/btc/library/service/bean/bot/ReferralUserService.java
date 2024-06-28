package tgb.btc.library.service.bean.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tgb.btc.library.bean.bot.ReferralUser;
import tgb.btc.library.interfaces.service.bean.bot.IReferralUserService;
import tgb.btc.library.repository.BaseRepository;
import tgb.btc.library.repository.bot.ReferralUserRepository;
import tgb.btc.library.service.bean.BasePersistService;

@Service
public class ReferralUserService extends BasePersistService<ReferralUser> implements IReferralUserService {
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
