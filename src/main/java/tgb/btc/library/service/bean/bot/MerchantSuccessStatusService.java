package tgb.btc.library.service.bean.bot;

import org.springframework.stereotype.Service;
import tgb.btc.library.bean.bot.MerchantSuccessStatus;
import tgb.btc.library.interfaces.service.bean.bot.IMerchantSuccessStatusService;
import tgb.btc.library.repository.bot.MerchantSuccessStatusRepository;

@Service
public class MerchantSuccessStatusService implements IMerchantSuccessStatusService {

    private final MerchantSuccessStatusRepository repository;

    public MerchantSuccessStatusService(MerchantSuccessStatusRepository repository) {
        this.repository = repository;
    }

    @Override
    public void delete(MerchantSuccessStatus merchantSuccessStatus) {
        repository.delete(merchantSuccessStatus);
    }

    @Override
    public MerchantSuccessStatus create(String status) {
        MerchantSuccessStatus merchantSuccessStatus = new MerchantSuccessStatus();
        merchantSuccessStatus.setStatus(status);
        return save(merchantSuccessStatus);
    }

    @Override
    public MerchantSuccessStatus save(MerchantSuccessStatus merchantSuccessStatus) {
        return repository.save(merchantSuccessStatus);
    }
}
