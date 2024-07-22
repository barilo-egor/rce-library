package tgb.btc.library.interfaces.service.bean.bot.deal.read;

import tgb.btc.library.bean.bot.Deal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface IDateDealService {

    List<Deal> getConfirmedByDateTimeBetween(LocalDateTime startDate, LocalDateTime endDate);

    List<Deal> getConfirmedByDateTimeBetween(LocalDateTime startDate);

    List<Deal> getConfirmedByDateBetween(LocalDate startDate, LocalDate endDate);

    List<Deal> getConfirmedByDateBetween(LocalDate startDate);
}
