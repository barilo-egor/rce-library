package tgb.btc.library.interfaces.service.process;

import tgb.btc.library.bean.bot.Deal;
import tgb.btc.library.constants.enums.bot.CryptoCurrency;

import java.util.List;

public interface IDealPoolService {
    void addToPool(Long pid);

    List<Deal> getAllByDealStatusAndCryptoCurrency(CryptoCurrency cryptoCurrency);

    void completePool(List<Deal> deals);

    void deleteFromPool(Long pid);

    void clearPool(CryptoCurrency cryptoCurrency);
}
