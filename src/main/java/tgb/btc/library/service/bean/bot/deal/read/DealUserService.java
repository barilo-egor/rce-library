package tgb.btc.library.service.bean.bot.deal.read;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tgb.btc.library.bean.bot.Deal;
import tgb.btc.library.interfaces.service.bean.bot.deal.read.IDealUserService;
import tgb.btc.library.repository.BaseRepository;
import tgb.btc.library.repository.bot.deal.read.DealUserRepository;
import tgb.btc.library.service.bean.BasePersistService;

@Service
@Transactional(readOnly = true)
public class DealUserService extends BasePersistService<Deal> implements IDealUserService {

    private DealUserRepository dealUserRepository;

    @Autowired
    public void setDealUserRepository(DealUserRepository dealUserRepository) {
        this.dealUserRepository = dealUserRepository;
    }

    @Override
    public Long getUserChatIdByDealPid(Long pid) {
        return dealUserRepository.getUserChatIdByDealPid(pid);
    }

    @Override
    public String getUserUsernameByDealPid(Long pid) {
        return dealUserRepository.getUserUsernameByDealPid(pid);
    }

    @Override
    protected BaseRepository<Deal> getBaseRepository() {
        return dealUserRepository;
    }

}
