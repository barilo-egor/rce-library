package tgb.btc.library.interfaces.service.bot;

import tgb.btc.library.bean.bot.SpamBan;

import java.util.List;

public interface ISpamBanService {

    SpamBan save(Long chatId);

    Long getUserPidByPid(Long pid);

    Long getUserChatIdByPid(Long pid);

    List<Long> getPids();

    void deleteByUser_Pid(Long userPid);

    long countByPid(Long pid);
}
