package tgb.btc.library.service.bean.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tgb.btc.library.bean.bot.SecurePaymentDetails;
import tgb.btc.library.interfaces.service.bean.bot.ISecurePaymentDetailsService;
import tgb.btc.library.repository.BaseRepository;
import tgb.btc.library.repository.bot.SecurePaymentDetailsRepository;
import tgb.btc.library.service.bean.BasePersistService;

@Service
public class SecurePaymentDetailsService extends BasePersistService<SecurePaymentDetails> implements ISecurePaymentDetailsService {

    private final SecurePaymentDetailsRepository repository;

    @Autowired
    public SecurePaymentDetailsService(SecurePaymentDetailsRepository repository) {
        this.repository = repository;
    }

    @Override
    protected BaseRepository<SecurePaymentDetails> getBaseRepository() {
        return repository;
    }
}
