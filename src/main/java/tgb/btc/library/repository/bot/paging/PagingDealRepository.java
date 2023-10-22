package tgb.btc.library.repository.bot.paging;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import tgb.btc.library.bean.bot.Deal;
import tgb.btc.library.constants.enums.bot.DealStatus;

@Repository
@Transactional
public interface PagingDealRepository extends PagingAndSortingRepository<Deal, Long> {

    void findAllByDealStatusNot(DealStatus dealStatus, Pageable pageable, Sort sort);

}
