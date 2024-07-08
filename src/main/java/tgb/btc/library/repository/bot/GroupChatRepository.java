package tgb.btc.library.repository.bot;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tgb.btc.library.bean.bot.GroupChat;
import tgb.btc.library.constants.enums.MemberStatus;
import tgb.btc.library.repository.BaseRepository;

@Repository
public interface GroupChatRepository extends BaseRepository<GroupChat> {

    @Modifying
    @Query("update GroupChat set memberStatus=:memberStatus where chatId=:chatId")
    void updateMemberStatusByChatId(Long chatId, MemberStatus memberStatus);

    @Modifying
    @Query("update GroupChat set title=:title where chatId=:chatId")
    void updateTitleByChatId(Long chatId, String title);
}
