package tgb.btc.library.repository.web;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tgb.btc.library.bean.bot.DealPayment;

@Repository
public interface DealPaymentRepository {

    /**
     * SELECT
     */

    @Query("from DealPayment where deal.pid=:dealPid")
    DealPayment getByDealPid(Long dealPid);
}
