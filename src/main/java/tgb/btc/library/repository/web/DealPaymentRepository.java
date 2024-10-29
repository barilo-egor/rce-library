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

    @Query(value = "SELECT * FROM deal_payment ORDER BY date_time DESC LIMIT 50", nativeQuery = true)
    List<DealPayment> findAllSortedDescDateTime();
}
