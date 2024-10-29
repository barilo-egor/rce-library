package tgb.btc.library.interfaces.service.bean.bot;

import tgb.btc.library.bean.bot.GroupChat;
import tgb.btc.library.constants.enums.MemberStatus;
import tgb.btc.library.constants.enums.bot.GroupChatType;
import tgb.btc.library.interfaces.service.IBasePersistService;

import java.util.List;
import java.util.Optional;

public interface IGroupChatService extends IBasePersistService<GroupChat> {

    GroupChat register(Long chatId, String title, MemberStatus memberStatus, GroupChatType groupChatType);

    boolean isExists(Long chatId);

    Optional<GroupChat> find(Long chatId);

    void updateMemberStatus(Long chatId, MemberStatus memberStatus);

    void updateTitleByChatId(Long chatId, String title);

    void updateIsSendMessageEnabledByChatId(Boolean isSendMessageEnabled, Long chatId);

    void updateTypeByChatId(GroupChatType type, Long chatId);

    void updateTypeByPid(GroupChatType type, Long pid);

    void dropDealRequestDefault();

    boolean hasDealRequests();

    boolean hasAutoWithdrawal();

    List<GroupChat> getAllByType(GroupChatType type);

    boolean existsByChatId(Long chatId);

    void deleteIfExistsByChatId(Long chatId);

    boolean isDealRequest(Long chatId);

    Optional<GroupChat> getByApiUserPid(Long apiUserPid);

    Optional<Long> getPidByApiUserPid(Long apiUserPid);

    boolean hasGroupChat(Long apiUserPid);
}
