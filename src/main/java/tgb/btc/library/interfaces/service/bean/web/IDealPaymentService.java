package tgb.btc.library.interfaces.service.bean.web;

import tgb.btc.library.bean.bot.DealPayment;
import tgb.btc.library.interfaces.service.IBasePersistService;

import java.util.List;

public interface IDealPaymentService extends IBasePersistService<DealPayment> {

    /**
     * SELECT
     */

    List<DealPayment> findAllSortedDescDateTime();
}
