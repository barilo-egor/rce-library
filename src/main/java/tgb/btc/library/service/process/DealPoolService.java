package tgb.btc.library.service.process;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import tgb.btc.api.web.INotificationsAPI;
import tgb.btc.api.web.INotifier;
import tgb.btc.library.bean.bot.Deal;
import tgb.btc.library.constants.enums.bot.CryptoCurrency;
import tgb.btc.library.constants.enums.bot.DealStatus;
import tgb.btc.library.interfaces.service.bean.bot.deal.IModifyDealService;
import tgb.btc.library.interfaces.service.bean.bot.deal.IReadDealService;
import tgb.btc.library.interfaces.service.process.IDealPoolService;

import java.util.List;
import java.util.Objects;

@Service
public class DealPoolService implements IDealPoolService {

    private final IModifyDealService modifyDealService;

    private final IReadDealService readDealService;

    private final INotifier notifier;

    private final INotificationsAPI notificationsAPI;

    @Autowired
    public DealPoolService(IModifyDealService modifyDealService, IReadDealService readDealService, INotifier notifier,
                           INotificationsAPI notificationsAPI) {
        this.modifyDealService = modifyDealService;
        this.readDealService = readDealService;
        this.notifier = notifier;
        this.notificationsAPI = notificationsAPI;
    }

    @Override
    public void addToPool(Long pid, Long initiatorChatId) {
        synchronized (this) {
            modifyDealService.updateDealStatusByPid(DealStatus.AWAITING_WITHDRAWAL, pid);
            notificationsAPI.poolChanged();
            if (Objects.nonNull(initiatorChatId)) {
                notifier.notifyPoolChanged(initiatorChatId);
            }
        }
    }

    @Override
    public List<Deal> getAllByDealStatusAndCryptoCurrency(CryptoCurrency cryptoCurrency) {
        synchronized (this) {
            return readDealService.getAllByDealStatusAndCryptoCurrency(DealStatus.AWAITING_WITHDRAWAL, cryptoCurrency);
        }
    }

    /**
     * Перевод сделок в подтвержденные с оповещениями на веб и бот.
     * @param cryptoCurrency крипто валюта
     */
    @Override
    public void completePool(CryptoCurrency cryptoCurrency, Long initiatorChatId) {
        synchronized (this) {
            List<Deal> deals = getAllByDealStatusAndCryptoCurrency(cryptoCurrency);
            for (Deal deal : deals) {
                modifyDealService.updateDealStatusByPid(DealStatus.CONFIRMED, deal.getPid());
            }
            notificationsAPI.poolChanged();
            if (Objects.nonNull(initiatorChatId)) {
                notifier.notifyPoolChanged(initiatorChatId);
            }
        }
    }

    @Override
    public void deleteFromPool(Long pid, Long initiatorChatId) {
        synchronized (this) {
            modifyDealService.updateDealStatusByPid(DealStatus.PAID, pid);
            notificationsAPI.poolChanged();
            if (Objects.nonNull(initiatorChatId)) {
                notifier.notifyPoolChanged(initiatorChatId);
            }
        }
    }

    @Override
    public void clearPool(CryptoCurrency cryptoCurrency, Long initiatorChatId) {
        synchronized (this) {
            List<Deal> deals = readDealService.getAllByDealStatusAndCryptoCurrency(DealStatus.AWAITING_WITHDRAWAL, cryptoCurrency);
            for (Deal deal : deals) {
                deal.setDealStatus(DealStatus.PAID);
                modifyDealService.save(deal);
            }
            notificationsAPI.poolChanged();
            if (Objects.nonNull(initiatorChatId)) {
                notifier.notifyPoolChanged(initiatorChatId);
            }
        }
    }

    @Scheduled(cron = "0 0/10 * * * ?")
    @Async
    @Override
    public void notifyDealsCount() {
        List<Deal> deals = readDealService.getAllByDealStatusAndCryptoCurrency(DealStatus.AWAITING_WITHDRAWAL, CryptoCurrency.BITCOIN);
        int size = deals.size();
        if (size > 0) {
            notifier.notifyAdmins("В пуле BTC на текущий момент находится " + deals.size() + " сделок.");
        }
    }
}
