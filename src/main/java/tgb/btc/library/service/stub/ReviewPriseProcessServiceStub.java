package tgb.btc.library.service.stub;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import tgb.btc.api.library.IReviewPriseProcessService;

/**
 * Заглушка для тестов. Реализация в rce
 */
@ConditionalOnMissingBean
public class ReviewPriseProcessServiceStub implements IReviewPriseProcessService {

    @Override
    public void processReviewPrise(Long aLong) {
        // stub
    }
}
