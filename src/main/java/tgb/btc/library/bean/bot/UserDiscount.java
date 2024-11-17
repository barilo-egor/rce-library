package tgb.btc.library.bean.bot;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.*;
import tgb.btc.library.bean.BasePersist;

import java.math.BigDecimal;

@Entity
@Table(name = "USER_DISCOUNT")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
public class UserDiscount extends BasePersist {

    @OneToOne
    private User user;

    @Column(name = "IS_RANK_DISCOUNT_ON")
    private Boolean isRankDiscountOn;

    @Column(name = "PERSONAL_BUY")
    private BigDecimal personalBuy;

    @Column(name = "PERSONAL_SELL")
    private BigDecimal personalSell;

    public Boolean getRankDiscountOn() {
        return isRankDiscountOn;
    }

    public void setRankDiscountOn(Boolean rankDiscountOn) {
        isRankDiscountOn = rankDiscountOn;
    }

}
