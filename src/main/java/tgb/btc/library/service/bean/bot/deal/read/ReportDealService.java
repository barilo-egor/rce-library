package tgb.btc.library.service.bean.bot.deal.read;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tgb.btc.library.bean.bot.Deal;
import tgb.btc.library.constants.enums.bot.CryptoCurrency;
import tgb.btc.library.constants.enums.bot.DealType;
import tgb.btc.library.constants.enums.bot.FiatCurrency;
import tgb.btc.library.interfaces.service.bean.bot.deal.read.IReportDealService;
import tgb.btc.library.repository.BaseRepository;
import tgb.btc.library.repository.bot.deal.read.ReportDealRepository;
import tgb.btc.library.service.bean.BasePersistService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ReportDealService extends BasePersistService<Deal> implements IReportDealService {

    private ReportDealRepository reportDealRepository;

    @Autowired
    public void setReportDealRepository(ReportDealRepository reportDealRepository) {
        this.reportDealRepository = reportDealRepository;
    }

    @Override
    public BigDecimal getConfirmedCryptoAmountSum(DealType dealType, LocalDateTime dateFrom, LocalDateTime dateTo, CryptoCurrency cryptoCurrency) {
        return reportDealRepository.getConfirmedCryptoAmountSum(dealType, dateFrom, dateTo, cryptoCurrency);
    }

    @Override
    public BigDecimal getConfirmedTotalAmountSum(DealType dealType, LocalDateTime dateFrom, CryptoCurrency cryptoCurrency,
                                                 FiatCurrency fiatCurrency) {
        return getConfirmedTotalAmountSum(dealType, dateFrom, dateFrom, cryptoCurrency, fiatCurrency);
    }

    @Override
    public BigDecimal getConfirmedTotalAmountSum(DealType dealType, LocalDateTime dateFrom, LocalDateTime dateTo, CryptoCurrency cryptoCurrency, FiatCurrency fiatCurrency) {
        return reportDealRepository.getConfirmedTotalAmountSum(dealType, dateFrom, dateTo, cryptoCurrency, fiatCurrency);
    }

    @Override
    public List<Object[]> findAllForUsersReport() {
        return reportDealRepository.findAllForUsersReport();
    }

    @Override
    protected BaseRepository<Deal> getBaseRepository() {
        return reportDealRepository;
    }

}
