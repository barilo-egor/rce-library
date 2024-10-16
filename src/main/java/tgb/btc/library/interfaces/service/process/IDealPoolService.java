package tgb.btc.library.interfaces.service.process;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import tgb.btc.library.bean.bot.Deal;
import tgb.btc.library.constants.enums.bot.CryptoCurrency;

import java.util.List;

public interface IDealPoolService {
    void addToPool(Long pid);

    List<Deal> getAllByDealStatusAndCryptoCurrency(CryptoCurrency cryptoCurrency);

    void completePool(CryptoCurrency cryptoCurrency, Long initiatorChatId);

    void deleteFromPool(Long pid, Long initiatorChatId);

    void clearPool(CryptoCurrency cryptoCurrency, Long initiatorChatId);

    @Scheduled(cron = "0 0/10 * * * ?")
    @Async
    void notifyDealsCount();
}
