package tgb.btc.library.repository.bot;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import tgb.btc.library.bean.bot.Review;
import tgb.btc.library.repository.BaseRepository;
import java.util.List;

@Repository
@Transactional
public interface ReviewRepository extends BaseRepository<Review> {
    List<Review> findAllByIsPublished(Boolean isPublished);
}
