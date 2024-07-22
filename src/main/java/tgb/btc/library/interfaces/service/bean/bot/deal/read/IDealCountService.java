package tgb.btc.library.interfaces.service.bean.bot.deal.read;

import tgb.btc.library.constants.enums.bot.CryptoCurrency;
import tgb.btc.library.constants.enums.bot.DealStatus;
import tgb.btc.library.constants.enums.bot.DealType;

import java.util.List;

public interface IDealCountService {

    Integer getCountDealByChatIdAndNotInDealStatus(Long chatId, List<DealStatus> dealStatus);

    Long getCountConfirmedByUserChatId(Long chatId);

    Long getConfirmedDealsCountByUserChatIdAndDealTypeAndCryptoCurrency(Long chatId, DealType dealType, CryptoCurrency cryptoCurrency);

    Long getConfirmedDealsCountByUserChatId(Long chatId);
}
