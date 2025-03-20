package tgb.btc.library.interfaces.service.bean.bot;

import tgb.btc.library.bean.bot.PaymentTypeDiscount;

import java.util.List;

public interface IPaymentTypeDiscountService {
    List<PaymentTypeDiscount> getByPaymentTypePid(Long paymentTypePid);
}
