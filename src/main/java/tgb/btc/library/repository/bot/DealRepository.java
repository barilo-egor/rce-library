package tgb.btc.library.repository.bot;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import tgb.btc.library.bean.bot.Deal;
import tgb.btc.library.repository.BaseRepository;
import tgb.btc.library.repository.bot.deal.ModifyDealRepository;
import tgb.btc.library.repository.bot.deal.ReadDealRepository;

@Repository
@Transactional
public interface DealRepository extends ReadDealRepository, ModifyDealRepository, BaseRepository<Deal> {

}
