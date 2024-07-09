package tgb.btc.library.repository.bot;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tgb.btc.library.bean.bot.GroupChat;
import tgb.btc.library.constants.enums.MemberStatus;
import tgb.btc.library.repository.BaseRepository;

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
    @Query("update GroupChat set isDefault=null where isDefault=true")
    void dropDefault();

    @Modifying
    @Query("update GroupChat set isDefault=true where pid=:pid")
    void setDefaultByPid(Long pid);

    /**
     * SELECT
     */

    @Query("from GroupChat where isDefault=true")
    Optional<GroupChat> getDefault();
}
