package tgb.btc.library.service.process;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

    @Autowired
    public DealPoolService(IModifyDealService modifyDealService, IReadDealService readDealService) {
        this.modifyDealService = modifyDealService;
        this.readDealService = readDealService;
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
     * @param deals сделки
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
}
