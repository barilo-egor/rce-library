package tgb.btc.library.service.bean.bot.deal.read;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tgb.btc.library.bean.bot.Deal;
import tgb.btc.library.interfaces.service.bean.bot.deal.read.IDateDealService;
import tgb.btc.library.repository.BaseRepository;
import tgb.btc.library.repository.bot.deal.read.DateDealRepository;
import tgb.btc.library.service.bean.BasePersistService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class DateDealService extends BasePersistService<Deal> implements IDateDealService {

    private DateDealRepository dealRepository;

    @Autowired
    public void setDealRepository(DateDealRepository dealRepository) {
        this.dealRepository = dealRepository;
    }

    @Override
    public List<Deal> getConfirmedByDateTimeBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return dealRepository.getConfirmedByDateTimeBetween(startDate, endDate);
    }

    @Override
    public List<Deal> getConfirmedByDateBetween(LocalDate startDate, LocalDate endDate) {
        return getConfirmedByDateTimeBetween(startDate.atStartOfDay(), endDate.plusDays(1).atStartOfDay());
    }

    @Override
    public List<Deal> getConfirmedByDateTimeBetween(LocalDateTime startDate) {
        return getConfirmedByDateTimeBetween(startDate.toLocalDate().atStartOfDay(),
                startDate.toLocalDate().plusDays(1).atStartOfDay());
    }

    @Override
    public List<Deal> getConfirmedByDateBetween(LocalDate startDate) {
        return getConfirmedByDateBetween(startDate, startDate);
    }

    @Override
    protected BaseRepository<Deal> getBaseRepository() {
        return dealRepository;
    }

}
