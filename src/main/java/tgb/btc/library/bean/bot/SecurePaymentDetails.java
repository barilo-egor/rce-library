package tgb.btc.library.bean.bot;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import tgb.btc.library.bean.BasePersist;
import tgb.btc.library.constants.enums.bot.FiatCurrency;

@Entity
@Table(name = "SECURE_PAYMENT_DETAILS")
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class SecurePaymentDetails extends BasePersist {

    private String details;

    private Integer minDealCount;

    @Column(nullable = false)
    private FiatCurrency fiatCurrency;
}
