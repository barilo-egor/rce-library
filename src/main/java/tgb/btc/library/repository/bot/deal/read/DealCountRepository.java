package tgb.btc.library.repository.bot.deal.read;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tgb.btc.library.bean.bot.Deal;
import tgb.btc.library.constants.enums.bot.CryptoCurrency;
import tgb.btc.library.constants.enums.bot.DealStatus;
import tgb.btc.library.constants.enums.bot.DealType;
import tgb.btc.library.repository.BaseRepository;

import java.util.List;

@Repository
public interface DealCountRepository extends BaseRepository<Deal> {


    @Query("select count(d) from Deal d where d.user.chatId=:chatId and d.dealStatus not in :dealStatus")
    Integer getCountFinishedDeal(Long chatId, List<DealStatus> dealStatus);

    @Query("select count(d) from Deal d where d.dealStatus='CONFIRMED' and d.user.chatId=:chatId")
    Long getCountPassedByUserChatId(Long chatId);

    @Query("select count(d) from Deal d where d.user.chatId=:chatId and d.dealStatus='CONFIRMED' and d.dealType=:dealType and d.cryptoCurrency=:cryptoCurrency")
    Long getPassedDealsCountByUserChatIdAndDealTypeAndCryptoCurrency(Long chatId, DealType dealType, CryptoCurrency cryptoCurrency);

    @Query("select count(d) from Deal d where d.user.chatId=:chatId and d.dealStatus='CONFIRMED' and d.dealType=:dealType")
    Long getPassedDealsCountByUserChatId(Long chatId, DealType dealType);

    @Query("select count(d) from Deal d where d.user.chatId=:chatId and d.dealStatus='CONFIRMED'")
    Long getPassedDealsCountByUserChatId(Long chatId);
}