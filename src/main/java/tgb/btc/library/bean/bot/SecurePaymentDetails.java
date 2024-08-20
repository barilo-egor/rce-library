package tgb.btc.library.bean.bot;


import lombok.*;
import tgb.btc.library.bean.BasePersist;

import javax.persistence.Entity;
import javax.persistence.Table;

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
}
