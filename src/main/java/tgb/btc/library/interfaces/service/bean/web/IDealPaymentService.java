package tgb.btc.library.interfaces.service.bean.web;

import tgb.btc.library.bean.bot.DealPayment;

import java.util.List;

public interface IDealPaymentService {

    /**
     * SELECT
     */

    DealPayment getByDealPid(Long dealPid);

    List<DealPayment> findAllSortedDescDateTime();
}
