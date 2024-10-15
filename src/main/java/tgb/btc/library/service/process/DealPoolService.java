package tgb.btc.library.service.process;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import tgb.btc.api.web.INotifier;
import tgb.btc.library.bean.bot.Deal;
import tgb.btc.library.constants.enums.bot.CryptoCurrency;
import tgb.btc.library.constants.enums.bot.DealStatus;
import tgb.btc.library.interfaces.service.bean.bot.deal.IModifyDealService;
import tgb.btc.library.interfaces.service.bean.bot.deal.IReadDealService;
import tgb.btc.library.interfaces.service.process.IDealPoolService;

import java.util.List;

@Service
public class DealPoolService implements IDealPoolService {

    private final IModifyDealService modifyDealService;

    private final IReadDealService readDealService;

    private final INotifier notifier;

    @Autowired
    public DealPoolService(IModifyDealService modifyDealService, IReadDealService readDealService, INotifier notifier) {
        this.modifyDealService = modifyDealService;
        this.readDealService = readDealService;
        this.notifier = notifier;
    }

    @Override
    public void addToPool(Long pid) {
        synchronized (this) {
            modifyDealService.updateDealStatusByPid(DealStatus.AWAITING_WITHDRAWAL, pid);
        }
    }

    @Override
    public List<Deal> getAllByDealStatusAndCryptoCurrency(CryptoCurrency cryptoCurrency) {
        synchronized (this) {
            return readDealService.getAllByDealStatusAndCryptoCurrency(DealStatus.AWAITING_WITHDRAWAL, cryptoCurrency);
        }
    }

    /**
     * Перевод сделок в подтвержденные, в случае удачного вывода.
     * @param cryptoCurrency крипто валюта
     */
    @Override
    public void completePool(CryptoCurrency cryptoCurrency) {
        synchronized (this) {
            List<Deal> deals = getAllByDealStatusAndCryptoCurrency(cryptoCurrency);
            for (Deal deal : deals) {
                modifyDealService.updateDealStatusByPid(DealStatus.CONFIRMED, deal.getPid());
            }
        }
    }

    @Override
    public void deleteFromPool(Long pid) {
        synchronized (this) {
            modifyDealService.updateDealStatusByPid(DealStatus.PAID, pid);
        }
    }

    @Override
    public void clearPool(CryptoCurrency cryptoCurrency) {
        synchronized (this) {
            List<Deal> deals = readDealService.getAllByDealStatusAndCryptoCurrency(DealStatus.AWAITING_WITHDRAWAL, cryptoCurrency);
            for (Deal deal : deals) {
                deal.setDealStatus(DealStatus.PAID);
                modifyDealService.save(deal);
            }
        }
    }

    @Scheduled(cron = "0 0/10 * * * ?")
    @Async
    public void notifyDealsCount() {
        List<Deal> deals = readDealService.getAllByDealStatusAndCryptoCurrency(DealStatus.AWAITING_WITHDRAWAL, CryptoCurrency.BITCOIN);
        int size = deals.size();
        if (size > 0) {
            notifier.notifyAdmins("В пуле BTC на текущий момент находится " + deals.size() + " сделок.");
        }
    }
}
