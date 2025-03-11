package tgb.btc.library.service.bean.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tgb.btc.library.bean.bot.Review;
import tgb.btc.library.interfaces.service.bean.bot.IReviewService;
import tgb.btc.library.repository.BaseRepository;
import tgb.btc.library.repository.bot.ReviewRepository;
import tgb.btc.library.service.bean.BasePersistService;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ReviewService extends BasePersistService<Review> implements IReviewService {

    private ReviewRepository reviewRepository;

    @Autowired
    public void setReviewRepository(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Override
    protected BaseRepository<Review> getBaseRepository() {
        return reviewRepository;
    }

    public List<Review> findAll() {
        return reviewRepository.findAll();
    }

    @Override
    public List<Review> findAllByIsAccepted(Boolean isAccepted) {
        return reviewRepository.findAllByIsAccepted(isAccepted);
    }

    @Override
    public List<Review> findAllByIsAccepted(Boolean isAccepted, Integer page, Integer limit, Sort sort) {
        return reviewRepository.findAll(Example.of(Review.builder().isAccepted(isAccepted).build()),
                PageRequest.of(page, limit, sort)).toList();
    }

    @Override
    public List<Review> findAllByIsAccepted(Boolean isAccepted, Integer page, Integer limit) {
        return reviewRepository.findAll(Example.of(Review.builder().isAccepted(isAccepted).build()),
                PageRequest.of(page, limit)).toList();
    }

    @Override
    public List<Review> findAllByPids(List<Long> pids) {
        if (CollectionUtils.isEmpty(pids)) {
            return new ArrayList<>();
        }
        return reviewRepository.findAllByPids(pids);
    }

    @Override
    public List<Review> findMoreThanPid(Long pid, int limit) {
        return reviewRepository.findAllByPidAndIsAcceptedOrderByPidAsc(pid, false, PageRequest.of(0, limit));
    }

    @Override
    public long countByDealPid(Long dealPid) {
        return reviewRepository.countByDealPid(dealPid);
    }

    @Override
    public Review findFirstByIsAcceptedOrderByPidAsc(Boolean isAccepted) {
        return reviewRepository.findFirstByIsAcceptedOrderByPidAsc(isAccepted);
    }
}
