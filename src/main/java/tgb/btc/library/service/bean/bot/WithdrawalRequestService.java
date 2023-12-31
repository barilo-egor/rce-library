package tgb.btc.library.service.bean.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tgb.btc.library.bean.bot.WithdrawalRequest;
import tgb.btc.library.repository.BaseRepository;
import tgb.btc.library.repository.bot.WithdrawalRequestRepository;
import tgb.btc.library.service.bean.BasePersistService;

import java.util.List;

@Service
public class WithdrawalRequestService extends BasePersistService<WithdrawalRequest> {

    private final WithdrawalRequestRepository withdrawalRequestRepository;

    @Autowired
    public WithdrawalRequestService(BaseRepository<WithdrawalRequest> baseRepository,
                                    WithdrawalRequestRepository withdrawalRequestRepository) {
        super(baseRepository);
        this.withdrawalRequestRepository = withdrawalRequestRepository;
    }

    public List<WithdrawalRequest> findAll() {
        return withdrawalRequestRepository.findAll();
    }

    public void updateIsActiveByPid(Boolean isActive, Long pid) {
        withdrawalRequestRepository.updateIsActiveByPid(isActive, pid);
    }

    public List<WithdrawalRequest> getAllActive() {
        return withdrawalRequestRepository.getAllActive();
    }

    public long getActiveByUserChatId(Long chatId) {
        return withdrawalRequestRepository.getActiveByUserChatId(chatId);
    }

    public Long getPidByUserChatId(Long chatId) {
        return withdrawalRequestRepository.getPidByUserChatId(chatId);
    }
}
