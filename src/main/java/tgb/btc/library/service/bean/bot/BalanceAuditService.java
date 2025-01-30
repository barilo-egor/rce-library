package tgb.btc.library.service.bean.bot;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import tgb.btc.library.bean.bot.BalanceAudit;
import tgb.btc.library.bean.bot.User;
import tgb.btc.library.constants.enums.bot.BalanceAuditType;
import tgb.btc.library.interfaces.service.bean.bot.IBalanceAuditService;
import tgb.btc.library.repository.bot.BalanceAuditRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

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

    @Override
    public List<BalanceAudit> findAll(Long targetChatId, Long initiatorChatId, Integer page, Integer limit, Sort sort) {
        return balanceAuditRepository.findAll(Example.of(getExample(targetChatId, initiatorChatId)),
                PageRequest.of(page, limit, sort)).toList();
    }

    @Override
    public List<BalanceAudit> findAll(Long targetChatId, Long initiatorChatId, Integer page, Integer limit) {
        return balanceAuditRepository.findAll(Example.of(getExample(targetChatId, initiatorChatId)),
                PageRequest.of(page, limit)).toList();
    }

    private BalanceAudit getExample(Long targetChatId, Long initiatorChatId) {
        BalanceAudit example = new BalanceAudit();
        if (Objects.nonNull(targetChatId)) {
            User user = new User();
            user.setChatId(targetChatId);
            user.setUserRole(null);
            user.setReferralBalance(null);
            example.setTarget(user);
        }
        if (Objects.nonNull(initiatorChatId)) {
            User user = new User();
            user.setChatId(initiatorChatId);
            user.setUserRole(null);
            user.setReferralBalance(null);
            example.setInitiator(user);
        }
        return example;
    }

    @Override
    public long count(Long targetChatId, Long initiatorChatId) {
        return balanceAuditRepository.count(Example.of(getExample(targetChatId, initiatorChatId)));
    }
}
