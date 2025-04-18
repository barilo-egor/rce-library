package tgb.btc.library.service.bean.bot;

import org.springframework.stereotype.Service;
import tgb.btc.library.bean.bot.AutoConfirmConfig;
import tgb.btc.library.constants.enums.bot.CryptoCurrency;
import tgb.btc.library.constants.enums.bot.DeliveryType;
import tgb.btc.library.constants.enums.web.merchant.AutoConfirmType;
import tgb.btc.library.interfaces.service.bean.bot.IAutoConfirmConfigService;
import tgb.btc.library.repository.bot.AutoConfirmConfigRepository;

@Service
public class AutoConfirmConfigService implements IAutoConfirmConfigService {

    private final AutoConfirmConfigRepository repository;

    public AutoConfirmConfigService(AutoConfirmConfigRepository repository) {
        this.repository = repository;
    }

    @Override
    public void delete(AutoConfirmConfig autoConfirmConfig) {
        repository.delete(autoConfirmConfig);
    }

    @Override
    public AutoConfirmConfig create(AutoConfirmType autoConfirmType, CryptoCurrency cryptoCurrency,
                                    DeliveryType deliveryType) {
        AutoConfirmConfig autoConfirmConfig = new AutoConfirmConfig();
        autoConfirmConfig.setAutoConfirmType(autoConfirmType);
        autoConfirmConfig.setCryptoCurrency(cryptoCurrency);
        autoConfirmConfig.setDeliveryType(deliveryType);
        return save(autoConfirmConfig);
    }

    @Override
    public AutoConfirmConfig save(AutoConfirmConfig autoConfirmConfig) {
        return repository.save(autoConfirmConfig);
    }
}
