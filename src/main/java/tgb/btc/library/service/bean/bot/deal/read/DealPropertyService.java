package tgb.btc.library.service.bean.bot.deal.read;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tgb.btc.library.bean.bot.Deal;
import tgb.btc.library.constants.enums.bot.*;
import tgb.btc.library.interfaces.service.bean.bot.deal.read.IDealPropertyService;
import tgb.btc.library.repository.BaseRepository;
import tgb.btc.library.repository.bot.deal.read.DealPropertyRepository;
import tgb.btc.library.service.bean.BasePersistService;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@Transactional(readOnly = true)
public class DealPropertyService extends BasePersistService<Deal> implements IDealPropertyService {

    private DealPropertyRepository dealPropertyRepository;

    @Autowired
    public void setDealPropertyRepository(DealPropertyRepository dealPropertyRepository) {
        this.dealPropertyRepository = dealPropertyRepository;
    }

    @Autowired
    public DealPropertyService(BaseRepository<Deal> baseRepository) {
        super(baseRepository);
    }

    @Override
    public CryptoCurrency getCryptoCurrencyByPid(Long pid) {
        return dealPropertyRepository.getCryptoCurrencyByPid(pid);
    }

    @Override
    public BigDecimal getCommissionByPid(Long pid) {
        return dealPropertyRepository.getCommissionByPid(pid);
    }

    @Override
    public BigDecimal getAmountByPid(Long pid) {
        return dealPropertyRepository.getAmountByPid(pid);
    }

    @Override
    public BigDecimal getCryptoAmountByPid(Long pid) {
        return dealPropertyRepository.getCryptoAmountByPid(pid);
    }

    @Override
    public BigDecimal getDiscountByPid(Long pid) {
        return dealPropertyRepository.getDiscountByPid(pid);
    }

    @Override
    public DealType getDealTypeByPid(Long pid) {
        return dealPropertyRepository.getDealTypeByPid(pid);
    }

    @Override
    public LocalDateTime getDateTimeByPid(Long pid) {
        return dealPropertyRepository.getDateTimeByPid(pid);
    }

    @Override
    public FiatCurrency getFiatCurrencyByPid(Long pid) {
        return dealPropertyRepository.getFiatCurrencyByPid(pid);
    }

    @Override
    public String getAdditionalVerificationImageIdByPid(Long pid) {
        return dealPropertyRepository.getAdditionalVerificationImageIdByPid(pid);
    }

    @Override
    public DeliveryType getDeliveryTypeByPid(Long pid) {
        return dealPropertyRepository.getDeliveryTypeByPid(pid);
    }

    @Override
    public BigDecimal getCreditedAmountByPid(Long pid) {
        return dealPropertyRepository.getCreditedAmountByPid(pid);
    }

    @Override
    public DealStatus getDealStatusByPid(Long pid) {
        return dealPropertyRepository.getDealStatusByPid(pid);
    }
}
