package tgb.btc.library.repository.web;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tgb.btc.library.bean.bot.DealPayment;
import tgb.btc.library.repository.BaseRepository;

import java.util.List;

@Repository
public interface DealPaymentRepository extends BaseRepository<DealPayment>  {

    /**
     * SELECT
     */

    @Query("from DealPayment where deal.pid=:dealPid")
    DealPayment getByDealPid(Long dealPid);

    @Query("from DealPayment order by dateTime desc")
    List<DealPayment> findAllSortedDescDateTime();
}
