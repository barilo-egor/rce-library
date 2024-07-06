package tgb.btc.library.service.bean.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tgb.btc.library.bean.bot.DealPayment;
import tgb.btc.library.interfaces.service.bean.web.IDealPaymentService;
import tgb.btc.library.repository.BaseRepository;
import tgb.btc.library.repository.web.DealPaymentRepository;
import tgb.btc.library.service.bean.BasePersistService;

import java.util.List;

@Service
public class DealPaymentService extends BasePersistService<DealPayment> implements IDealPaymentService {

    private DealPaymentRepository dealPaymentRepository;

    @Autowired
    public void setDealPaymentRepository(DealPaymentRepository dealPaymentRepository) {
        this.dealPaymentRepository = dealPaymentRepository;
    }

    @Override
    public DealPayment getByDealPid(Long dealPid) {
        return dealPaymentRepository.getByDealPid(dealPid);
    }

    @Override
    public List<DealPayment> findAllSortedDescDateTime() {
        return dealPaymentRepository.findAllSortedDescDateTime();
    }

    @Override
    protected BaseRepository<DealPayment> getBaseRepository() {
        return dealPaymentRepository;
    }

}
