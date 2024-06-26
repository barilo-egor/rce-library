package tgb.btc.library.interfaces.service.bot;

import tgb.btc.library.bean.bot.PaymentReceipt;

import java.util.List;

public interface IPaymentReceiptService {

    List<PaymentReceipt> getByDealsPids(Long userChatId);
}
