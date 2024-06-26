package tgb.btc.library.interfaces.service.bot.deal.read;

public interface IDealUserService {

    Long getUserChatIdByDealPid(Long pid);

    String getUserUsernameByDealPid(Long pid);
}
