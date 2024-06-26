package tgb.btc.library.repository.bot.deal;

import org.springframework.stereotype.Repository;
import tgb.btc.library.repository.bot.deal.read.*;

@Repository
public interface ReadDealRepository extends DateDealRepository, DealCountRepository, DealPropertyRepository,
        DealUserRepository, ReportDealRepository {
}
