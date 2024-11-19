package tgb.btc.library.repository.bot;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import tgb.btc.library.bean.bot.WithdrawalRequest;
import tgb.btc.library.repository.BaseRepository;

import java.util.List;

@Repository
@Transactional
public interface WithdrawalRequestRepository extends BaseRepository<WithdrawalRequest> {

    @Modifying
    @Query("update WithdrawalRequest set isActive=:isActive where pid=:pid")
    void updateIsActiveByPid(Boolean isActive, Long pid);

    @Query("select count(w) from WithdrawalRequest w where w.user.pid in (select pid from User where chatId=:chatId) and w.isActive=true")
    long getActiveByUserChatId(Long chatId);

    @Query("from WithdrawalRequest where isActive=true")
    List<WithdrawalRequest> getAllActive();

    @Query("select w.pid from WithdrawalRequest w where w.user.pid in (select pid from User where chatId=:chatId) and w.isActive=true")
    Long getPidByUserChatId(Long chatId);

    @Modifying
    void deleteByUser_ChatId(Long userChatId);
}
