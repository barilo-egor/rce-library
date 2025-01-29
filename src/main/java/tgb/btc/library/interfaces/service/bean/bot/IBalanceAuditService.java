package tgb.btc.library.interfaces.service.bean.bot;

import org.springframework.data.domain.Sort;
import tgb.btc.library.bean.bot.BalanceAudit;
import tgb.btc.library.bean.bot.User;
import tgb.btc.library.constants.enums.bot.BalanceAuditType;

import java.util.List;

public interface IBalanceAuditService {
    void save(User target, User initiator, Integer amount, BalanceAuditType type);

    void save(User target, Integer amount, BalanceAuditType type);

    List<BalanceAudit> findAll(Integer page, Integer limit, Sort sort);

    List<BalanceAudit> findAll(Integer page, Integer limit);

    long count();
}
