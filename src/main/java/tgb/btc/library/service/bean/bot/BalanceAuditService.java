package tgb.btc.library.service.bean.bot;

import org.springframework.stereotype.Service;
import tgb.btc.library.bean.bot.BalanceAudit;
import tgb.btc.library.bean.bot.User;
import tgb.btc.library.constants.enums.bot.BalanceAuditType;
import tgb.btc.library.interfaces.service.bean.bot.IBalanceAuditService;
import tgb.btc.library.repository.bot.BalanceAuditRepository;

import java.time.LocalDateTime;

@Service
public class BalanceAuditService implements IBalanceAuditService {

    private final BalanceAuditRepository balanceAuditRepository;

    public BalanceAuditService(BalanceAuditRepository balanceAuditRepository) {
        this.balanceAuditRepository = balanceAuditRepository;
    }

    @Override
    public void save(User target, User initiator, Integer amount, BalanceAuditType type) {
        BalanceAudit balanceAudit = BalanceAudit.builder()
                .target(target)
                .initiator(initiator)
                .amount(amount)
                .newBalance(target.getReferralBalance())
                .type(type)
                .build();
        balanceAudit.setDateTime(LocalDateTime.now());
        balanceAuditRepository.save(balanceAudit);
    }

    @Override
    public void save(User target, Integer amount, BalanceAuditType type) {
        BalanceAudit balanceAudit = BalanceAudit.builder()
                .target(target)
                .amount(amount)
                .newBalance(target.getReferralBalance())
                .type(type)
                .build();
        balanceAudit.setDateTime(LocalDateTime.now());
        balanceAuditRepository.save(balanceAudit);
    }
}
