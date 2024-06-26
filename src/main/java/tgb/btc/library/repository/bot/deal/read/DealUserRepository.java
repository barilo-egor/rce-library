package tgb.btc.library.repository.bot.deal.read;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tgb.btc.library.bean.bot.Deal;
import tgb.btc.library.repository.BaseRepository;

@Repository
public interface DealUserRepository extends BaseRepository<Deal> {

    @Query("select d.user.chatId from Deal d where d.pid=:pid")
    Long getUserChatIdByDealPid(Long pid);

    @Query("select d.user.username from Deal d where d.pid=:pid")
    String getUserUsernameByDealPid(Long pid);


}
