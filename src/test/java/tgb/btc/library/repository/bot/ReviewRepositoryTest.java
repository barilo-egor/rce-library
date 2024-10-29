package tgb.btc.library.repository.bot;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import tgb.btc.library.bean.bot.Review;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ActiveProfiles("test")
class ReviewRepositoryTest {

    @Autowired
    private ReviewRepository reviewRepository;

    @Test
    void findAllByIsPublished() {
        assertEquals(new ArrayList<>(), reviewRepository.findAllByIsPublished(true));
        assertEquals(new ArrayList<>(), reviewRepository.findAllByIsPublished(false));
        Set<Review> publishedExpected = new HashSet<>();
        Set<Review> notPublishedExpected = new HashSet<>();
        for (int i = 0; i < 10; i++) {
            Review review = new Review();
            if (i % 2 == 0) {
                review.setPublished(true);
            } else {
                review.setPublished(false);
            }
            reviewRepository.save(review);
            if (i % 2 == 0) {
                publishedExpected.add(review);
            } else {
                notPublishedExpected.add(review);
            }
        }
        for (int i = 0; i < 3; i++) {
            reviewRepository.save(Review.builder().build());
        }
        assertEquals(publishedExpected, new HashSet<>(reviewRepository.findAllByIsPublished(true)));
        assertEquals(notPublishedExpected, new HashSet<>(reviewRepository.findAllByIsPublished(false)));
    }
}