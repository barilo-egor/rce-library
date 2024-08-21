package tgb.btc.library.interfaces.service.bean.bot;

import tgb.btc.library.bean.bot.SecurePaymentDetails;
import tgb.btc.library.interfaces.service.IBasePersistService;

public interface ISecurePaymentDetailsService extends IBasePersistService<SecurePaymentDetails> {
    SecurePaymentDetails update(Long pid, String details);

    boolean hasAccessToPaymentTypes(Long chatId);

    SecurePaymentDetails getByChatId(Long chatId);
}
