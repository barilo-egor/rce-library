package tgb.btc.library.repository.bot;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tgb.btc.library.bean.bot.MerchantConfig;
import tgb.btc.library.repository.BaseRepository;

import java.util.List;

@Repository
public interface MerchantConfigRepository extends BaseRepository<MerchantConfig> {

    List<MerchantConfig> findAllByOrderByMerchantOrder();

    List<MerchantConfig> findAllByIsOnOrderByMerchantOrder(Boolean isOn);

    @Query("select max(merchantOrder) from MerchantConfig")
    Integer finMaxMerchantOrder();

    MerchantConfig findByMerchantOrder(Integer merchantOrder);

    List<MerchantConfig> findAllByIsOn(boolean isOn);
}
