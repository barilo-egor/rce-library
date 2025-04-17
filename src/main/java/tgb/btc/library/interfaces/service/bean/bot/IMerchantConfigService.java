package tgb.btc.library.interfaces.service.bean.bot;

import tgb.btc.library.bean.bot.MerchantConfig;
import tgb.btc.library.constants.enums.Merchant;

import java.util.List;

public interface IMerchantConfigService {
    MerchantConfig getMerchantConfig(Merchant merchant);

    List<MerchantConfig> findAll();

    List<MerchantConfig> findAllSortedByMerchantOrder();

    void changeOrder(Merchant merchant, boolean isUp);

    List<MerchantConfig> findAllByIsOn(boolean isOn);

    MerchantConfig save(MerchantConfig config);
}
