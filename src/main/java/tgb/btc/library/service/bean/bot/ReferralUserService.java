package tgb.btc.library.service.bean.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tgb.btc.library.bean.bot.ReferralUser;
import tgb.btc.library.interfaces.service.bean.bot.IReferralUserService;
import tgb.btc.library.repository.BaseRepository;
import tgb.btc.library.repository.bot.ReferralUserRepository;
import tgb.btc.library.service.bean.BasePersistService;

@Service
@Transactional
public class ReferralUserService extends BasePersistService<ReferralUser> implements IReferralUserService {
    private ReferralUserRepository referralUserRepository;

    @Autowired
    public ReferralUserService(ReferralUserRepository referralUserRepository) {
        this.referralUserRepository = referralUserRepository;
    }

    @Override
    protected BaseRepository<ReferralUser> getBaseRepository() {
        return referralUserRepository;
    }

    public ReferralUser save(ReferralUser referralUser) {
        return referralUserRepository.save(referralUser);
    }
}
