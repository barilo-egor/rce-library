package tgb.btc.library.interfaces.service.bean.bot;

import tgb.btc.library.bean.bot.GroupChat;
import tgb.btc.library.constants.enums.MemberStatus;
import tgb.btc.library.interfaces.service.IBasePersistService;

import java.util.Optional;

public interface IGroupChatService extends IBasePersistService<GroupChat> {

    GroupChat register(Long chatId, String title, MemberStatus memberStatus);

    boolean isExists(Long chatId);

    Optional<GroupChat> find(Long chatId);

    void updateMemberStatus(Long chatId, MemberStatus memberStatus);

    void updateTitleByChatId(Long chatId, String title);
}
