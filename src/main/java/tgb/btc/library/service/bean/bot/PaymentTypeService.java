package tgb.btc.library.service.bean.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tgb.btc.library.bean.bot.PaymentType;
import tgb.btc.library.constants.enums.bot.DealType;
import tgb.btc.library.constants.enums.bot.FiatCurrency;
import tgb.btc.library.exception.BaseException;
import tgb.btc.library.repository.bot.DealRepository;
import tgb.btc.library.repository.bot.PaymentRequisiteRepository;
import tgb.btc.library.repository.bot.PaymentTypeRepository;
import tgb.btc.library.repository.bot.UserRepository;

import java.util.List;

@Service
public class PaymentTypeService {

    private PaymentTypeRepository paymentTypeRepository;

    private DealRepository dealRepository;

    private UserRepository userRepository;

    private PaymentRequisiteRepository paymentRequisiteRepository;

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
