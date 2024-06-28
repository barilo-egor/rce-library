package tgb.btc.library.interfaces.service.bot;

import tgb.btc.library.bean.bot.WithdrawalRequest;

import java.util.List;

public interface IWithdrawalRequestService {

    void updateIsActiveByPid(Boolean isActive, Long pid);

    long getActiveByUserChatId(Long chatId);

    List<WithdrawalRequest> getAllActive();

    Long getPidByUserChatId(Long chatId);

    void deleteByUser_ChatId(Long userChatId);
}
