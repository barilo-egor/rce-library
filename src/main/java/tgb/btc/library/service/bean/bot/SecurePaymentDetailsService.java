package tgb.btc.library.service.bean.bot;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tgb.btc.library.bean.bot.SecurePaymentDetails;
import tgb.btc.library.exception.BaseException;
import tgb.btc.library.interfaces.service.bean.bot.ISecurePaymentDetailsService;
import tgb.btc.library.interfaces.service.bean.bot.deal.read.IDealCountService;
import tgb.btc.library.repository.BaseRepository;
import tgb.btc.library.repository.bot.SecurePaymentDetailsRepository;
import tgb.btc.library.service.bean.BasePersistService;

import java.util.List;

@Service
@Slf4j
@Transactional
public class SecurePaymentDetailsService extends BasePersistService<SecurePaymentDetails> implements ISecurePaymentDetailsService {

    private final SecurePaymentDetailsRepository repository;

    private final IDealCountService dealCountService;

    @Autowired
    public SecurePaymentDetailsService(SecurePaymentDetailsRepository repository, IDealCountService dealCountService) {
        this.repository = repository;
        this.dealCountService = dealCountService;
    }

    @Override
    protected BaseRepository<SecurePaymentDetails> getBaseRepository() {
        return repository;
    }

    @Override
    public SecurePaymentDetails update(Long pid, String details) {
        SecurePaymentDetails securePaymentDetails = repository.getById(pid);
        if (StringUtils.isNotEmpty(details)) {
            securePaymentDetails.setDetails(details);
        }
        return save(securePaymentDetails);
    }

    @Override
    public boolean hasAccessToPaymentTypes(Long chatId) {
        int dealsCount = dealCountService.getCountConfirmedByUserChatId(chatId).intValue();
        List<SecurePaymentDetails> detailsList = repository.findAll(
                Example.of(SecurePaymentDetails.builder().minDealCount(dealsCount).build())
        );
        return CollectionUtils.isEmpty(detailsList);
    }

    @Override
    public SecurePaymentDetails getByChatId(Long chatId) {
        int dealsCount = dealCountService.getCountConfirmedByUserChatId(chatId).intValue();
        List<SecurePaymentDetails> detailsList = repository.findAll(
                Example.of(SecurePaymentDetails.builder().minDealCount(dealsCount).build())
        );
        if (CollectionUtils.isEmpty(detailsList)) {
            log.error("Защитный реквизит для chatId={} не найден. Количество сделок = {}", chatId, dealsCount);
            throw new BaseException("Защитный реквизит не найден.");
        }
        return detailsList.get(0);
    }
}
