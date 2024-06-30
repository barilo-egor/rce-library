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

    @Autowired
    public DateDealService(BaseRepository<Deal> baseRepository) {
        super(baseRepository);
    }

    @Override
    public List<Deal> getByDateBetween(LocalDate startDate, LocalDate endDate) {
        return dealRepository.getByDateBetween(startDate, endDate);
    }

    @Override
    public List<Deal> getByDateTimeBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return dealRepository.getByDateTimeBetween(startDate, endDate);
    }

    @Override
    public List<Deal> getPassedByDate(LocalDate date) {
        return dealRepository.getPassedByDate(date);
    }

    @Override
    public List<Deal> getPassedByDateTimeBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return dealRepository.getPassedByDateTimeBetween(startDate, endDate);
    }

    @Override
    public List<Deal> getByDate(LocalDate dateTime) {
        return dealRepository.getPassedByDate(dateTime);
    }
}
