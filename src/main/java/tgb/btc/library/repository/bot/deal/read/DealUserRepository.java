package tgb.btc.library.repository.bot.deal.read;

import org.springframework.data.jpa.repository.Query;

public interface DealUserRepository {

    @Query("select d.user.chatId from Deal d where d.pid=:pid")
    Long getUserChatIdByDealPid(Long pid);

    @Query("select d.user.username from Deal d where d.pid=:pid")
    String getUserUsernameByDealPid(Long pid);

}
