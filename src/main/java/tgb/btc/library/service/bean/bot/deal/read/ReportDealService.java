package tgb.btc.library.service.bean.bot.deal.read;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tgb.btc.library.bean.bot.Deal;
import tgb.btc.library.constants.enums.bot.CryptoCurrency;
import tgb.btc.library.constants.enums.bot.DealStatus;
import tgb.btc.library.constants.enums.bot.DealType;
import tgb.btc.library.constants.enums.bot.FiatCurrency;
import tgb.btc.library.interfaces.service.bean.bot.deal.read.IReportDealService;
import tgb.btc.library.repository.BaseRepository;
import tgb.btc.library.repository.bot.deal.read.ReportDealRepository;
import tgb.btc.library.service.bean.BasePersistService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReportDealService extends BasePersistService<Deal> implements IReportDealService {

    private ReportDealRepository reportDealRepository;

    @Autowired
    public void setReportDealRepository(ReportDealRepository reportDealRepository) {
        this.reportDealRepository = reportDealRepository;
    }

    @Autowired
    public ReportDealService(BaseRepository<Deal> baseRepository) {
        super(baseRepository);
    }

    @Override
    public BigDecimal getCryptoAmountSum(DealType dealType, LocalDate date, CryptoCurrency cryptoCurrency) {
        return reportDealRepository.getCryptoAmountSum(dealType, date, cryptoCurrency);
    }

    @Override
    public BigDecimal getCryptoAmountSumByDate(DealType dealType, LocalDateTime startDateTime, LocalDateTime endDateTime, CryptoCurrency cryptoCurrency) {
        return reportDealRepository.getCryptoAmountSumByDate(dealType, startDateTime, endDateTime, cryptoCurrency);
    }

    @Override
    public BigDecimal getCryptoAmountSum(DealType dealType, LocalDateTime dateFrom, LocalDateTime dateTo, CryptoCurrency cryptoCurrency) {
        return reportDealRepository.getCryptoAmountSum(dealType, dateFrom, dateTo, cryptoCurrency);
    }

    @Override
    public BigDecimal getTotalAmountSum(DealType dealType, LocalDateTime dateTime, CryptoCurrency cryptoCurrency) {
        return reportDealRepository.getTotalAmountSum(dealType, dateTime, cryptoCurrency);
    }

    @Override
    public BigDecimal getTotalAmountSum(DealType dealType, LocalDateTime dateTime, CryptoCurrency cryptoCurrency, FiatCurrency fiatCurrency) {
        return reportDealRepository.getTotalAmountSum(dealType, dateTime, cryptoCurrency, fiatCurrency);
    }

    @Override
    public BigDecimal getTotalAmountSum(DealType dealType, LocalDateTime dateFrom, LocalDateTime dateTo, CryptoCurrency cryptoCurrency) {
        return reportDealRepository.getTotalAmountSum(dealType, dateFrom, dateTo, cryptoCurrency);
    }

    @Override
    public BigDecimal getTotalAmountSum(DealType dealType, LocalDateTime dateFrom, LocalDateTime dateTo, CryptoCurrency cryptoCurrency, FiatCurrency fiatCurrency) {
        return reportDealRepository.getTotalAmountSum(dealType, dateFrom, dateTo, cryptoCurrency, fiatCurrency);
    }

    @Override
    public Integer getCountPassedByChatId(Long chatId) {
        return reportDealRepository.getCountPassedByChatId(chatId);
    }

    @Override
    public Long getCountByChatIdAndStatus(Long chatId, DealStatus dealStatus) {
        return reportDealRepository.getCountByChatIdAndStatus(chatId, dealStatus);
    }

    @Override
    public Integer getCountByPeriod(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return reportDealRepository.getCountByPeriod(startDateTime, endDateTime);
    }

    @Override
    public BigDecimal getUserCryptoAmountSum(Long chatId, CryptoCurrency cryptoCurrency, DealType dealType) {
        return reportDealRepository.getUserCryptoAmountSum(chatId, cryptoCurrency, dealType);
    }

    @Override
    public BigDecimal getUserAmountSum(Long chatId, DealType dealType) {
        return reportDealRepository.getUserAmountSum(chatId, dealType);
    }

    @Override
    public BigDecimal getUserAmountSumByDealTypeAndFiatCurrency(Long chatId, DealType dealType, FiatCurrency fiatCurrency) {
        return reportDealRepository.getUserAmountSumByDealTypeAndFiatCurrency(chatId, dealType, fiatCurrency);
    }

    @Override
    public List<Deal> getByUser_ChatId(Long chatId) {
        return reportDealRepository.getByUser_ChatId(chatId);
    }

    @Override
    public Boolean getIsUsedPromoByPid(Long pid) {
        return reportDealRepository.getIsUsedPromoByPid(pid);
    }

    @Override
    public List<Object[]> findAllForUsersReport() {
        return reportDealRepository.findAllForUsersReport();
    }
}
