package tgb.btc.library.interfaces.service.bean.bot;

import tgb.btc.library.bean.bot.PaymentReceipt;
import tgb.btc.library.interfaces.service.IBasePersistService;

import java.util.List;

public interface IPaymentReceiptService extends IBasePersistService<PaymentReceipt> {

    List<PaymentReceipt> getByDealsPids(Long userChatId);
}
