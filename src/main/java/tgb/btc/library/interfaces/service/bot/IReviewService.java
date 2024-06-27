package tgb.btc.library.interfaces.service.bot;

import tgb.btc.library.bean.bot.Review;

import java.util.List;

public interface IReviewService {

    List<Review> findAll();

    List<Review> findAllByIsPublished(Boolean isPublished);
}
