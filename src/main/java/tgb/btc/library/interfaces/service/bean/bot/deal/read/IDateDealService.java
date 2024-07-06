package tgb.btc.library.interfaces.service.bean.bot.deal.read;

import tgb.btc.library.bean.bot.Deal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface IDateDealService {

    List<Deal> getByDateBetween(LocalDate startDate, LocalDate endDate);

    List<Deal> getByDateTimeBetween(LocalDateTime startDate, LocalDateTime endDate);

    List<Deal> getPassedByDate(LocalDate date);

    List<Deal> getPassedByDateTimeBetween(LocalDateTime startDate, LocalDateTime endDate);

    List<Deal> getByDate(LocalDate dateTime);
}
