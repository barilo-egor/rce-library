package tgb.btc.library.repository.bot;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import tgb.btc.library.bean.bot.Deal;
import tgb.btc.library.constants.enums.bot.*;
import tgb.btc.library.repository.bot.deal.*;
import tgb.btc.library.repository.bot.deal.read.*;

import java.util.List;

@Repository
@Transactional
public interface DealRepository extends ReadDealRepository, ModifyDealRepository {

    Deal findByPid(Long pid);

    @Query("from Deal where pid in (:pids)")
    List<Deal> getDealsByPids(List<Long> pids);

    @Query("select d.pid from Deal d where d.user.chatId=:chatId and d.dealStatus=:dealStatus")
    List<Long> getListNewDeal(Long chatId, DealStatus dealStatus);

    @Query("select pid from Deal where dealStatus='PAID'")
    List<Long> getPaidDealsPids();

    @Query("select case when count(d) > :countDeals then true else false end from Deal d where d.user.chatId=:chatId and d.dealStatus=:dealStatus")
    boolean dealsByUserChatIdIsExist(Long chatId, DealStatus dealStatus, Long countDeals);

    @Query("select wallet from Deal where pid=(select max(d.pid) from Deal d where d.user.chatId=:chatId and d.dealStatus='CONFIRMED' and d.dealType=:dealType and d.cryptoCurrency=:cryptoCurrency)")
    String getWalletFromLastPassedByChatIdAndDealTypeAndCryptoCurrency(Long chatId, DealType dealType, CryptoCurrency cryptoCurrency);

}
