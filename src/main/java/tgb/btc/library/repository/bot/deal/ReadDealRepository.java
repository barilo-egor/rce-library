package tgb.btc.library.repository.bot.deal;

import org.springframework.data.jpa.repository.Query;
import tgb.btc.library.bean.bot.Deal;
import tgb.btc.library.constants.enums.Merchant;
import tgb.btc.library.constants.enums.bot.CryptoCurrency;
import tgb.btc.library.constants.enums.bot.DealStatus;
import tgb.btc.library.constants.enums.bot.DealType;
import tgb.btc.library.repository.bot.deal.read.*;

import java.time.LocalDateTime;
import java.util.List;

public interface ReadDealRepository extends DateDealRepository, DealCountRepository, DealPropertyRepository,
        DealUserRepository, ReportDealRepository {

    Deal findByPid(Long pid);

    @Query("from Deal where pid in (:pids)")
    List<Deal> getDealsByPids(List<Long> pids);

    @Query("select d.pid from Deal d where d.user.chatId=:chatId and d.dealStatus=:dealStatus")
    List<Long> getPidsByChatIdAndStatus(Long chatId, DealStatus dealStatus);

    @Query("select pid from Deal where dealStatus='PAID' or dealStatus='AWAITING_VERIFICATION' or dealStatus='VERIFICATION_REJECTED' or dealStatus='VERIFICATION_RECEIVED'")
    List<Long> getPaidDealsPids();

    @Query("select case when count(d) > :countDeals then true else false end from Deal d where d.user.chatId=:chatId and d.dealStatus=:dealStatus")
    boolean dealsByUserChatIdIsExist(Long chatId, DealStatus dealStatus, Long countDeals);

    @Query("select wallet from Deal where pid=(select max(d.pid) from Deal d where d.user.chatId=:chatId " +
            "and d.dealStatus='CONFIRMED' and d.dealType=:dealType and d.cryptoCurrency=:cryptoCurrency)")
    String getWalletFromLastPassedByChatIdAndDealTypeAndCryptoCurrency(Long chatId, DealType dealType, CryptoCurrency cryptoCurrency);

    List<Deal> getAllByDealStatusAndCryptoCurrency(DealStatus dealStatus, CryptoCurrency cryptoCurrency);

    @Query("from Deal where merchant = :merchant and merchantOrderStatus in :orderStatuses and dealStatus != 'NEW' and dateTime > :afterDateTime")
    List<Deal> getAllNotNewByMerchantAndOrderStatusesAfterDateTime(Merchant merchant, List<String> orderStatuses, LocalDateTime afterDateTime);

    Deal getByMerchantOrderId(String merchantOrderId);

    @Query("from Deal where dateTime < :dateTime and dealStatus = 'NEW'")
    List<Deal> getNewDealsByDateTimeBefore(LocalDateTime dateTime);
}
