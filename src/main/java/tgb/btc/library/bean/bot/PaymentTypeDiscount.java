package tgb.btc.library.bean.bot;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import tgb.btc.library.bean.BasePersist;

@Entity
@Table(name = "PAYMENT_TYPE_DISCOUNT")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
/**
 * Скидка, применяемая к сделкам совершенным по определенному типу оплаты.
 */
public class PaymentTypeDiscount extends BasePersist {

    /**
     * Тип оплаты, к которому относится скидка
     */
    @ManyToOne
    private PaymentType paymentType;

    /**
     * Сумма до которой применяется скидка
     */
    private Integer maxAmount;

    /**
     * Процент скидки
     */
    private Double percent;
}
