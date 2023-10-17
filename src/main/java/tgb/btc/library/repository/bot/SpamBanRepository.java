package tgb.btc.library.repository.bot;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tgb.btc.library.bean.bot.SpamBan;
import tgb.btc.library.repository.BaseRepository;

import java.util.List;

@Repository
public interface SpamBanRepository extends BaseRepository<SpamBan> {

    @Query("select user.pid from SpamBan where pid=:pid")
    Long getUserPidByPid(Long pid);

    @Query("select user.chatId from SpamBan where pid=:pid")
    Long getUserChatIdByPid(Long pid);

    @Query("select pid from SpamBan")
    List<Long> getPids();

    @Modifying
    void deleteByUser_Pid(Long userPid);

    long countByPid(Long pid);
}
