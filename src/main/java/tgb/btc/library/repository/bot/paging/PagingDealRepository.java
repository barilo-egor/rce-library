package tgb.btc.library.repository.bot.paging;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import tgb.btc.library.bean.bot.Deal;

@Repository
@Transactional
public interface PagingDealRepository extends PagingAndSortingRepository<Deal, Long> {
}
