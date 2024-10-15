package tgb.btc.library.interfaces.service.process;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import tgb.btc.library.bean.bot.Deal;
import tgb.btc.library.constants.enums.bot.CryptoCurrency;

import java.util.List;

public interface IDealPoolService {
    void addToPool(Long pid);

    List<Deal> getAllByDealStatusAndCryptoCurrency(CryptoCurrency cryptoCurrency);

    void completePool(CryptoCurrency cryptoCurrency);

    void deleteFromPool(Long pid);

    void clearPool(CryptoCurrency cryptoCurrency);

    @Scheduled(cron = "0 0/10 * * * ?")
    @Async
    void notifyDealsCount();
}
