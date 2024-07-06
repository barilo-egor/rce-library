package tgb.btc.library.service.bean.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tgb.btc.library.bean.bot.PaymentReceipt;
import tgb.btc.library.interfaces.service.bean.bot.IPaymentReceiptService;
import tgb.btc.library.repository.BaseRepository;
import tgb.btc.library.repository.bot.PaymentReceiptRepository;
import tgb.btc.library.service.bean.BasePersistService;

import java.util.List;

@Service
@Transactional
public class PaymentReceiptService extends BasePersistService<PaymentReceipt> implements IPaymentReceiptService {

    private PaymentReceiptRepository paymentReceiptRepository;

    @Autowired
    public void setPaymentReceiptRepository(PaymentReceiptRepository paymentReceiptRepository) {
        this.paymentReceiptRepository = paymentReceiptRepository;
    }

    @Override
    public List<PaymentReceipt> getByDealsPids(Long userChatId) {
        return paymentReceiptRepository.getByDealsPids(userChatId);
    }

    @Override
    protected BaseRepository<PaymentReceipt> getBaseRepository() {
        return paymentReceiptRepository;
    }

}
