package tgb.btc.library.interfaces.service.bean.bot;

import tgb.btc.library.bean.bot.WithdrawalRequest;
import tgb.btc.library.interfaces.service.IBasePersistService;

import java.util.List;

public interface IWithdrawalRequestService extends IBasePersistService<WithdrawalRequest> {

    void updateIsActiveByPid(Boolean isActive, Long pid);

    long getActiveByUserChatId(Long chatId);

    List<WithdrawalRequest> getAllActive();

    Long getPidByUserChatId(Long chatId);

    void deleteByUser_ChatId(Long userChatId);
}
