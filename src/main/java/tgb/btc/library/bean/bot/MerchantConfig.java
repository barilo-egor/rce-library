package tgb.btc.library.bean.bot;

import jakarta.persistence.*;
import lombok.*;
import tgb.btc.library.bean.BasePersist;
import tgb.btc.library.constants.enums.Merchant;
import tgb.btc.library.constants.enums.bot.CryptoCurrency;
import tgb.btc.library.constants.enums.bot.DeliveryType;

import java.util.List;
import java.util.Optional;

@EqualsAndHashCode(callSuper = true)
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class MerchantConfig extends BasePersist {

    private Boolean isOn;

    @Enumerated(EnumType.STRING)
    @Column(unique = true, nullable = false)
    private Merchant merchant;

    private Boolean isAutoWithdrawalOn;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<MerchantSuccessStatus> successStatuses;

    private Integer maxAmount;

    @Column(unique = true, nullable = false)
    private Integer merchantOrder;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<AutoConfirmConfig> confirmConfigs;

    private Integer delay;

    private Integer attemptsCount;

    public Optional<AutoConfirmConfig> getAutoConfirmConfig(CryptoCurrency cryptoCurrency, DeliveryType deliveryType) {
        return confirmConfigs.stream()
                .filter(conf -> cryptoCurrency.equals(conf.getCryptoCurrency())
                        && deliveryType.equals(conf.getDeliveryType()))
                .findFirst();
    }


}
