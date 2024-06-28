package tgb.btc.library.service.bean.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tgb.btc.library.bean.bot.PaymentType;
import tgb.btc.library.constants.enums.bot.DealType;
import tgb.btc.library.constants.enums.bot.FiatCurrency;
import tgb.btc.library.exception.BaseException;
import tgb.btc.library.interfaces.service.bot.IPaymentTypeService;
import tgb.btc.library.repository.BaseRepository;
import tgb.btc.library.repository.bot.DealRepository;
import tgb.btc.library.repository.bot.PaymentRequisiteRepository;
import tgb.btc.library.repository.bot.PaymentTypeRepository;
import tgb.btc.library.repository.bot.UserRepository;
import tgb.btc.library.service.bean.BasePersistService;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PaymentTypeService extends BasePersistService<PaymentType> implements IPaymentTypeService {

    private PaymentTypeRepository paymentTypeRepository;

    private DealRepository dealRepository;

    private UserRepository userRepository;

    private PaymentRequisiteRepository paymentRequisiteRepository;

    @Autowired
    public PaymentTypeService(BaseRepository<PaymentType> baseRepository) {
        super(baseRepository);
    }

    @Autowired
    public void setPaymentRequisiteRepository(PaymentRequisiteRepository paymentRequisiteRepository) {
        this.paymentRequisiteRepository = paymentRequisiteRepository;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setPaymentTypeRepository(PaymentTypeRepository paymentTypeRepository) {
        this.paymentTypeRepository = paymentTypeRepository;
    }

    @Autowired
    public void setDealRepository(DealRepository dealRepository) {
        this.dealRepository = dealRepository;
    }

    public Integer getTurnedCountByDeal(Long chatId) {
        Long dealPid = userRepository.getCurrentDealByChatId(chatId);
        return paymentTypeRepository.countByDealTypeAndIsOnAndFiatCurrency(
                dealRepository.getDealTypeByPid(dealPid), true, dealRepository.getFiatCurrencyByPid(dealPid));
    }

    public PaymentType getFirstTurned(Long dealPid) {
        DealType dealType = dealRepository.getDealTypeByPid(dealPid);
        FiatCurrency fiatCurrency = dealRepository.getFiatCurrencyByPid(dealPid);
        List<PaymentType> paymentTypeList = paymentTypeRepository.getByDealTypeAndIsOnAndFiatCurrency(
                dealType, true, fiatCurrency);
        if (CollectionUtils.isEmpty(paymentTypeList))
            throw new BaseException("Не найден ни один тип оплаты для " + dealType.name() + " " + fiatCurrency.name());
        return paymentTypeList.get(0);
    }

    public List<PaymentType> findAll() {
        return paymentTypeRepository.findAll();
    }

    public PaymentType getByPid(Long pid) {
        return paymentTypeRepository.getByPid(pid);
    }

    @Override
    public List<PaymentType> getByDealType(DealType dealType) {
        return paymentTypeRepository.getByDealType(dealType);
    }

    @Override
    public List<PaymentType> getByDealTypeAndFiatCurrency(DealType dealType, FiatCurrency fiatCurrency) {
        return paymentTypeRepository.getByDealTypeAndFiatCurrency(dealType, fiatCurrency);
    }

    @Override
    public long countByDealTypeAndFiatCurrency(DealType dealType, FiatCurrency fiatCurrency) {
        return paymentTypeRepository.countByDealTypeAndFiatCurrency(dealType, fiatCurrency);
    }

    @Override
    public List<PaymentType> getByDealTypeAndIsOnAndFiatCurrency(DealType dealType, Boolean isOn, FiatCurrency fiatCurrency) {
        return paymentTypeRepository.getByDealTypeAndIsOnAndFiatCurrency(dealType, isOn, fiatCurrency);
    }

    @Override
    public Integer countByDealTypeAndIsOnAndFiatCurrency(DealType dealType, Boolean isOn, FiatCurrency fiatCurrency) {
        return paymentTypeRepository.countByDealTypeAndIsOnAndFiatCurrency(dealType, isOn, fiatCurrency);
    }

    @Override
    public DealType getDealTypeByPid(Long pid) {
        return paymentTypeRepository.getDealTypeByPid(pid);
    }

    @Override
    public long countByNameLike(String name) {
        return paymentTypeRepository.countByNameLike(name);
    }

    @Override
    public void updateIsOnByPid(Boolean isOn, Long pid) {
        paymentTypeRepository.updateIsOnByPid(isOn, pid);
    }

    @Override
    public void updateMinSumByPid(BigDecimal minSum, Long pid) {
        paymentTypeRepository.updateMinSumByPid(minSum, pid);
    }

    @Override
    public void updateIsDynamicOnByPid(Boolean isDynamicOn, Long pid) {
        paymentTypeRepository.updateIsDynamicOnByPid(isDynamicOn, pid);
    }

    @Transactional
    public void remove(Long paymentTypePid) {
        dealRepository.updatePaymentTypeToNullByPaymentTypePid(paymentTypePid);
        paymentRequisiteRepository.deleteByPaymentTypePid(paymentTypePid);
        paymentTypeRepository.deleteById(paymentTypePid);
    }

    @Transactional
    public boolean isNameFree(String name) {
        return paymentTypeRepository.countByNameLike(name) == 0;
    }
}
