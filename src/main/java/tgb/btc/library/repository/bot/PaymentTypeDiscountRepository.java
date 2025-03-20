package tgb.btc.library.repository.bot;

import org.springframework.stereotype.Repository;
import tgb.btc.library.bean.bot.PaymentTypeDiscount;
import tgb.btc.library.repository.BaseRepository;

import java.util.List;

@Repository
public interface PaymentTypeDiscountRepository extends BaseRepository<PaymentTypeDiscount> {

    List<PaymentTypeDiscount> getAllByPaymentTypePid(Long paymentTypePid);
}
