package tgb.btc.library.repository.bot;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import tgb.btc.library.bean.bot.Review;
import tgb.btc.library.repository.BaseRepository;

import java.util.List;

@Repository
@Transactional
public interface ReviewRepository extends BaseRepository<Review>, PagingAndSortingRepository<Review, Long> {
    List<Review> findAllByIsAccepted(Boolean isAccepted);

    @Query("from Review where pid in (:pids)")
    List<Review> findAllByPids(List<Long> pids);

    @Query("from Review where pid > :pid and isAccepted = :isAccepted order by pid asc")
    List<Review> findAllByPidAndIsAcceptedOrderByPidAsc(Long pid, boolean isAccepted, Pageable pageable);

    @Query("select count(pid) from Review where deal.pid = :dealPid")
    long countByDealPid(Long dealPid);
}
