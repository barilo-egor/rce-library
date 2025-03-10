package tgb.btc.library.repository.bot.deal;

import org.springframework.data.jpa.repository.Query;
import tgb.btc.library.bean.bot.Deal;
import tgb.btc.library.constants.enums.bot.CryptoCurrency;
import tgb.btc.library.constants.enums.bot.DealStatus;
import tgb.btc.library.constants.enums.bot.DealType;
import tgb.btc.library.constants.enums.web.merchant.dashpay.DashPayOrderStatus;
import tgb.btc.library.constants.enums.web.merchant.payscrow.OrderStatus;
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

    @Query("from Deal where payscrowOrderStatus in :orderStatuses and dealStatus != 'NEW' and dateTime > :afterDateTime")
    List<Deal> getAllNotNewByPayscrowOrderStatusesAfterDateTime(List<OrderStatus> orderStatuses, LocalDateTime afterDateTime);

    @Query("from Deal where dashPayOrderStatus in :orderStatuses and dealStatus != 'NEW' and dateTime > :afterDateTime")
    List<Deal> getAllNotNewByDashPayOrderStatusesAfterDateTime(List<DashPayOrderStatus> orderStatuses, LocalDateTime afterDateTime);

    @Query("from Deal where alfaTeamInvoiceId = :alfaTeamInvoiceId")
    Deal getDealByAlfaTeamInvoiceId(String alfaTeamInvoiceId);
}
