package tgb.btc.library.interfaces.service.bean.bot;

import org.springframework.data.domain.Sort;
import tgb.btc.library.bean.bot.Review;
import tgb.btc.library.interfaces.service.IBasePersistService;

import java.util.List;

public interface IReviewService extends IBasePersistService<Review> {

    List<Review> findAll();

    List<Review> findAllByIsAccepted(Boolean isAccepted);

    List<Review> findAllByIsAccepted(Boolean isAccepted, Integer page, Integer limit, Sort sort);

    List<Review> findAllByIsAccepted(Boolean isAccepted, Integer page, Integer limit);

    List<Review> findAllByPids(List<Long> pids);

    List<Review> findMoreThanPid(Long pid, int limit);

    long countByDealPid(Long dealPid);

    Review findFirstByIsAcceptedOrderByPidAsc(Boolean isAccepted);
}
