package tgb.btc.library.bean.bot;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@SuperBuilder
public class BalanceAudit extends Audit {

    /**
     * Пользователь, которому изменили баланс
     */
    @ManyToOne
    private User target;

    /**
     * Пользователь изменивший баланс
     */
    @ManyToOne
    private User initiator;

    /**
     * Сумма изменения
     */
    private Integer amount;

    /**
     * Баланс после изменения
     */
    private Integer newBalance;
}
