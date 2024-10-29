package tgb.btc.library.repository.bot.deal.read;

import org.springframework.data.jpa.repository.Query;
import tgb.btc.library.bean.bot.Deal;
import tgb.btc.library.repository.BaseRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface DateDealRepository extends BaseRepository<Deal> {

    @Query("from Deal d where (d.dateTime BETWEEN :startDate AND :endDate) and d.dealStatus='CONFIRMED'")
    List<Deal> getConfirmedByDateTimeBetween(LocalDateTime startDate, LocalDateTime endDate);
}
