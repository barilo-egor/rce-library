package tgb.btc.library.service.bean.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tgb.btc.library.bean.bot.Deal;
import tgb.btc.library.bean.bot.PaymentReceipt;
import tgb.btc.library.interfaces.service.bot.IPaymentReceiptService;
import tgb.btc.library.repository.BaseRepository;
import tgb.btc.library.repository.bot.PaymentReceiptRepository;
import tgb.btc.library.service.bean.BasePersistService;

import java.util.List;

@Service
public class PaymentReceiptService extends BasePersistService<Deal> implements IPaymentReceiptService {

    private PaymentReceiptRepository paymentReceiptRepository;

    @Autowired
    public void setPaymentReceiptRepository(PaymentReceiptRepository paymentReceiptRepository) {
        this.paymentReceiptRepository = paymentReceiptRepository;
    }

    @Autowired
    public PaymentReceiptService(BaseRepository<Deal> baseRepository) {
        super(baseRepository);
    }

    @Override
    public List<PaymentReceipt> getByDealsPids(Long userChatId) {
        return paymentReceiptRepository.getByDealsPids(userChatId);
    }
}