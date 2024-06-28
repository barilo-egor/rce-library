package tgb.btc.library.interfaces.service.bean.bot;

import tgb.btc.library.bean.bot.SpamBan;
import tgb.btc.library.interfaces.service.IBasePersistService;

import java.util.List;

public interface ISpamBanService extends IBasePersistService<SpamBan> {

    SpamBan save(Long chatId);

    Long getUserPidByPid(Long pid);

    Long getUserChatIdByPid(Long pid);

    List<Long> getPids();

    void deleteByUser_Pid(Long userPid);

    long countByPid(Long pid);
}
