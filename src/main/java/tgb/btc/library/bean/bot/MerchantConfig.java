package tgb.btc.library.bean.bot;

import jakarta.persistence.*;
import lombok.*;
import tgb.btc.library.bean.BasePersist;
import tgb.btc.library.constants.enums.Merchant;

import java.util.List;

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
}
