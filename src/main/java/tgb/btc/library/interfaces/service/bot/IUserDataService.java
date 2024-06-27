package tgb.btc.library.interfaces.service.bot;

import tgb.btc.library.constants.enums.bot.CryptoCurrency;
import tgb.btc.library.constants.enums.bot.DealType;
import tgb.btc.library.constants.enums.bot.FiatCurrency;

public interface IUserDataService {

    Long getLongByUserPid(Long userPid);

    String getStringByUserChatId(Long chatId);

    DealType getDealTypeByChatId(Long chatId);

    FiatCurrency getFiatCurrencyByChatId(Long chatId);

    CryptoCurrency getCryptoCurrencyByChatId(Long chatId);

    Long countByUserPid(Long userPid);

    void updateLongByUserPid(Long userPid, Long longVariable);

    void updateStringByUserChatId(Long chatId, String stringVariable);

    void updateDealTypeByUserChatId(Long userChatId, DealType dealTypeVariable);

    void updateFiatCurrencyByUserChatId(Long chatId, FiatCurrency fiatCurrency);

    void deleteByUser_ChatId(Long userChatId);

    void updateCryptoCurrencyByChatId(Long userChatId, CryptoCurrency cryptoCurrency);
}
