package tgb.btc.library.repository.bot;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tgb.btc.library.bean.bot.GroupChat;
import tgb.btc.library.constants.enums.MemberStatus;
import tgb.btc.library.constants.enums.bot.GroupChatType;
import tgb.btc.library.repository.BaseRepository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupChatRepository extends BaseRepository<GroupChat> {

    /**
     * UPDATE
     */
    @Modifying
    @Query("update GroupChat set memberStatus=:memberStatus where chatId=:chatId")
    void updateMemberStatusByChatId(Long chatId, MemberStatus memberStatus);

    @Modifying
    @Query("update GroupChat set title=:title where chatId=:chatId")
    void updateTitleByChatId(Long chatId, String title);

    @Modifying
    @Query("update GroupChat set type='DEFAULT' where type='DEAL_REQUEST'")
    void dropDealRequestDefault();

    @Modifying
    @Query("update GroupChat set type=:type where chatId=:chatId")
    void updateTypeByChatId(GroupChatType type, Long chatId);

    @Modifying
    @Query("update GroupChat set type=:type where pid=:pid")
    void updateTypeByPid(GroupChatType type, Long pid);

    @Modifying
    @Query("update GroupChat set isSendMessageEnabled=:isSendMessageEnabled where chatId=:chatId")
    void updateIsSendMessageEnabledByChatId(Boolean isSendMessageEnabled, Long chatId);

    /**
     * SELECT
     */
    Optional<GroupChat> getByType(GroupChatType type);

    List<GroupChat> getAllByType(GroupChatType type);

    GroupChat getByChatId(Long chatId);
}
