package tgb.btc.library.interfaces.service.bean.bot;

import tgb.btc.library.bean.bot.AutoConfirmConfig;
import tgb.btc.library.constants.enums.bot.CryptoCurrency;
import tgb.btc.library.constants.enums.bot.DeliveryType;
import tgb.btc.library.constants.enums.web.merchant.AutoConfirmType;

public interface IAutoConfirmConfigService {
    void delete(AutoConfirmConfig autoConfirmConfig);

    AutoConfirmConfig create(AutoConfirmType autoConfirmType, CryptoCurrency cryptoCurrency,
                             DeliveryType deliveryType);

    AutoConfirmConfig save(AutoConfirmConfig autoConfirmConfig);
}
