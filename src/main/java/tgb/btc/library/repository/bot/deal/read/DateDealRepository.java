package tgb.btc.library.repository.bot.deal.read;

import org.springframework.data.jpa.repository.Query;
import tgb.btc.library.bean.bot.Deal;
import tgb.btc.library.repository.BaseRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface DateDealRepository extends BaseRepository<Deal> {
    default List<Deal> getByDateBetween(LocalDate startDate, LocalDate endDate) {
        return getByDateTimeBetween(startDate.atStartOfDay(), endDate.plusDays(1).atStartOfDay());
    }

    @Query("from Deal d where (d.dateTime BETWEEN :startDate AND :endDate) and d.dealStatus='CONFIRMED'")
    List<Deal> getByDateTimeBetween(LocalDateTime startDate, LocalDateTime endDate);

    default List<Deal> getPassedByDate(LocalDate date) {
        return getPassedByDateTimeBetween(date.atStartOfDay(), date.plusDays(1).atStartOfDay());
    }

    @Query("from Deal d where d.dateTime between :startDate and :endDate and d.dealStatus='CONFIRMED'")
    List<Deal> getPassedByDateTimeBetween(LocalDateTime startDate, LocalDateTime endDate);
}
