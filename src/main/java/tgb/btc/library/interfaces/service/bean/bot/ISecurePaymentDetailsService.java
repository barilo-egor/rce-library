package tgb.btc.library.interfaces.service.bean.bot;

import tgb.btc.library.bean.bot.SecurePaymentDetails;
import tgb.btc.library.constants.enums.bot.FiatCurrency;
import tgb.btc.library.interfaces.service.IBasePersistService;

public interface ISecurePaymentDetailsService extends IBasePersistService<SecurePaymentDetails> {
    SecurePaymentDetails update(Long pid, String details, FiatCurrency fiatCurrency);

    boolean hasAccessToPaymentTypes(Long chatId, FiatCurrency fiatCurrency);

    SecurePaymentDetails getByChatIdAndFiatCurrency(Long chatId, FiatCurrency fiatCurrency);

    long count(FiatCurrency fiatCurrency);
}
