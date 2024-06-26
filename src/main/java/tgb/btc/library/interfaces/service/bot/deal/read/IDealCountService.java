package tgb.btc.library.interfaces.service.bot.deal.read;

import tgb.btc.library.constants.enums.bot.CryptoCurrency;
import tgb.btc.library.constants.enums.bot.DealStatus;
import tgb.btc.library.constants.enums.bot.DealType;

import java.util.List;

public interface IDealCountService {

    Integer getCountFinishedDeal(Long chatId, List<DealStatus> dealStatus);

    Long getCountPassedByUserChatId(Long chatId);

    Long getPassedDealsCountByUserChatIdAndDealTypeAndCryptoCurrency(Long chatId, DealType dealType, CryptoCurrency cryptoCurrency);

    Long getPassedDealsCountByUserChatId(Long chatId, DealType dealType);

    Long getPassedDealsCountByUserChatId(Long chatId);
}
