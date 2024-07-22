package tgb.btc.library.repository.bot.deal.read;

import org.springframework.data.jpa.repository.Query;
import tgb.btc.library.bean.bot.Deal;
import tgb.btc.library.constants.enums.bot.CryptoCurrency;
import tgb.btc.library.constants.enums.bot.DealStatus;
import tgb.btc.library.constants.enums.bot.DealType;
import tgb.btc.library.repository.BaseRepository;

import java.util.List;

public interface DealCountRepository extends BaseRepository<Deal> {

    @Query("select count(d) from Deal d where d.user.chatId=:chatId and d.dealStatus not in :dealStatus")
    Integer getCountDealByChatIdAndNotInDealStatus(Long chatId, List<DealStatus> dealStatus);

    @Query("select count(d) from Deal d where d.dealStatus=:dealStatus and d.user.chatId=:chatId")
    Long getCountByDealStatusAndChatId(Long chatId, DealStatus dealStatus);

    @Query("select count(d) from Deal d where d.user.chatId=:chatId and d.dealStatus=:dealStatus and d.dealType=:dealType and d.cryptoCurrency=:cryptoCurrency")
    Long getDealsCountByUserChatIdAndDealStatusAndDealTypeAndCryptoCurrency(Long chatId, DealStatus dealStatus, DealType dealType, CryptoCurrency cryptoCurrency);

    @Query("select count(d) from Deal d where d.user.chatId=:chatId and d.dealStatus=:dealStatus")
    Long getDealsCountByUserChatIdAndDealStatus(Long chatId, DealStatus dealStatus);
}
