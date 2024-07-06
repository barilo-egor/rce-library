package tgb.btc.library.interfaces.service.bean.bot;

import tgb.btc.library.bean.bot.Review;
import tgb.btc.library.interfaces.service.IBasePersistService;

import java.util.List;

public interface IReviewService extends IBasePersistService<Review> {

    List<Review> findAll();

    List<Review> findAllByIsPublished(Boolean isPublished);
}
