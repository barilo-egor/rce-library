package tgb.btc.library.service.bean.bot.paging;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import tgb.btc.library.bean.bot.Deal;
import tgb.btc.library.constants.enums.bot.DealStatus;
import tgb.btc.library.repository.bot.paging.PagingDealRepository;

import java.util.List;

@Service
public class PagingDealService {

    private PagingDealRepository pagingDealRepository;

    @Autowired
    public void setPagingDealRepository(PagingDealRepository pagingDealRepository) {
        this.pagingDealRepository = pagingDealRepository;
    }

    public List<Deal> findAllByDealStatusNot(DealStatus dealStatus, Pageable pageable) {
        return pagingDealRepository.findAllByDealStatusNot(dealStatus, pageable);
    }
}
