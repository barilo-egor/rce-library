package tgb.btc.library.service.bean.bot;

import org.springframework.data.domain.Example;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tgb.btc.library.bean.bot.MerchantConfig;
import tgb.btc.library.constants.enums.Merchant;
import tgb.btc.library.interfaces.service.bean.bot.IMerchantConfigService;
import tgb.btc.library.repository.bot.MerchantConfigRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class MerchantConfigService implements IMerchantConfigService {

    private final MerchantConfigRepository repository;

    public MerchantConfigService(MerchantConfigRepository repository) {
        this.repository = repository;
        Merchant[] merchants = Merchant.values();
        if (merchants.length > repository.count()) {
            for (Merchant merchant : merchants) {
                MerchantConfig merchantConfig = getMerchantConfig(merchant);
                if (Objects.isNull(merchantConfig)) {
                    create(merchant);
                }
            }
        }
    }

    @Override
    public MerchantConfig getMerchantConfig(Merchant merchant) {
        Optional<MerchantConfig> merchantConfigOptional =
                repository.findBy(Example.of(MerchantConfig.builder().merchant(merchant).build()), FluentQuery.FetchableFluentQuery::one);
        return merchantConfigOptional.orElse(null);
    }

    public Optional<MerchantConfig> getByMerchantOrder(Integer order) {
        return repository.findBy(Example.of(MerchantConfig.builder().merchantOrder(order).build()), FluentQuery.FetchableFluentQuery::one);
    }

    @Override
    public List<MerchantConfig> findAll() {
        return repository.findAll();
    }

    @Override
    public List<MerchantConfig> findAllSortedByMerchantOrder() {
        return repository.findAllByOrderByMerchantOrder();
    }

    @Override
    public List<MerchantConfig> findAllByIsOnOrderByMerchantOrder(Boolean isOn) {
        return repository.findAllByIsOnOrderByMerchantOrder(isOn);
    }

    private MerchantConfig create(Merchant merchant) {
        return repository.save(
                MerchantConfig.builder()
                        .isOn(false)
                        .merchant(merchant)
                        .isAutoWithdrawalOn(false)
                        .maxAmount(5000)
                        .delay(3)
                        .attemptsCount(5)
                        .merchantOrder(Long.valueOf(repository.count()).intValue() + 1)
                        .build()
        );
    }

    @Override
    @Transactional
    public void changeOrder(Merchant merchant, boolean isUp) {
        MerchantConfig config = getMerchantConfig(merchant);
        int currentOrder = config.getMerchantOrder();
        int maxOrder = repository.finMaxMerchantOrder();

        if ((isUp && currentOrder == 1) || (!isUp && currentOrder == maxOrder)) {
            return;
        }

        int newOrder = isUp ? currentOrder - 1 : currentOrder + 1;
        MerchantConfig other = getByMerchantOrder(newOrder)
                .orElseThrow(() -> new IllegalStateException("Config with order " + newOrder + " not found"));

        other.setMerchantOrder(-1);
        repository.saveAndFlush(other);

        config.setMerchantOrder(newOrder);
        repository.saveAndFlush(config);

        other.setMerchantOrder(currentOrder);
        repository.saveAndFlush(other);
    }

    @Override
    public List<MerchantConfig> findAllByIsOn(boolean isOn) {
        return repository.findBy(Example.of(MerchantConfig.builder().isOn(isOn).build()), FluentQuery.FetchableFluentQuery::all);
    }

    @Override
    public MerchantConfig save(MerchantConfig config) {
        return repository.save(config);
    }
}
