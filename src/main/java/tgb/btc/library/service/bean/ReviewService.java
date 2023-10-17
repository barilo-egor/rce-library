package tgb.btc.library.service.bean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tgb.btc.library.bean.bot.Review;
import tgb.btc.library.repository.BaseRepository;
import tgb.btc.library.repository.bot.ReviewRepository;

import java.util.List;

@Service
public class ReviewService extends BasePersistService<Review> {

    private final ReviewRepository reviewRepository;

    @Autowired
    public ReviewService(BaseRepository<Review> baseRepository, ReviewRepository reviewRepository) {
        super(baseRepository);
        this.reviewRepository = reviewRepository;
    }

    public List<Review> findAll() {
        return reviewRepository.findAll();
    }

    public List<Review> findAllByIsPublished(Boolean isPublished) {
        return reviewRepository.findAllByIsPublished(isPublished);
    }
}
