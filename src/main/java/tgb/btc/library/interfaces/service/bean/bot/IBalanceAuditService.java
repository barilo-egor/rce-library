package tgb.btc.library.interfaces.service.bean.bot;

import tgb.btc.library.bean.bot.User;
import tgb.btc.library.constants.enums.bot.BalanceAuditType;

public interface IBalanceAuditService {
    void save(User target, User initiator, Integer amount, BalanceAuditType type);

    void save(User target, Integer amount, BalanceAuditType type);
}
