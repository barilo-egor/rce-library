package tgb.btc.library.service.bean.bot;

import org.springframework.stereotype.Service;
import tgb.btc.library.bean.bot.BalanceAudit;
import tgb.btc.library.bean.bot.User;
import tgb.btc.library.interfaces.service.bean.bot.IBalanceAuditService;
import tgb.btc.library.repository.bot.BalanceAuditRepository;

@Service
public class BalanceAuditService implements IBalanceAuditService {

    private final BalanceAuditRepository balanceAuditRepository;

    public BalanceAuditService(BalanceAuditRepository balanceAuditRepository) {
        this.balanceAuditRepository = balanceAuditRepository;
    }

    public void save(User target, User initiator, Integer amount) {
        balanceAuditRepository.save(BalanceAudit.builder()
                .target(target)
                .initiator(initiator)
                .amount(amount)
                .build());
    }
}
