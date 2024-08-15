package tgb.btc.library.interfaces.service.bean.bot;

import org.springframework.data.domain.Sort;
import tgb.btc.library.bean.bot.Review;
import tgb.btc.library.interfaces.service.IBasePersistService;

import java.util.List;

public interface IReviewService extends IBasePersistService<Review> {

    List<Review> findAll();

    List<Review> findAllByIsPublished(Boolean isPublished);

    List<Review> findAllByIsPublished(Boolean isPublished, Integer page, Integer limit, Sort sort);

    List<Review> findAllByIsPublished(Boolean isPublished, Integer page, Integer limit);
}
