package tgb.btc.library.service.bean.bot.deal;

import org.springframework.stereotype.Service;
import tgb.btc.library.bean.bot.PaymentTypeDiscount;
import tgb.btc.library.exception.BaseException;
import tgb.btc.library.interfaces.service.bean.bot.IPaymentTypeDiscountService;
import tgb.btc.library.repository.bot.PaymentTypeDiscountRepository;

import java.util.List;

@Service
public class PaymentTypeDiscountService implements IPaymentTypeDiscountService {

    private final PaymentTypeDiscountRepository repository;

    public PaymentTypeDiscountService(PaymentTypeDiscountRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<PaymentTypeDiscount> getByPaymentTypePid(Long paymentTypePid) {
        return repository.getAllByPaymentTypePidOrderByMaxAmount(paymentTypePid);
    }

    @Override
    public PaymentTypeDiscount getByPid(Long pid) {
        return repository.findById(pid).orElseThrow(() -> new BaseException("Запись не найдена."));
    }

    @Override
    public PaymentTypeDiscount save(PaymentTypeDiscount paymentTypeDiscount) {
        return repository.save(paymentTypeDiscount);
    }

    @Override
    public void deleteByPid(Long paymentTypeDiscountPid) {
        repository.deleteById(paymentTypeDiscountPid);
    }

    @Override
    public void delete(PaymentTypeDiscount paymentTypeDiscount) {
        repository.delete(paymentTypeDiscount);
    }
}
