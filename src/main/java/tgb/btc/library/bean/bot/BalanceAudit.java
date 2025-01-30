package tgb.btc.library.bean.bot;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import lombok.*;
import tgb.btc.library.constants.enums.bot.BalanceAuditType;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Builder
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

    /**
     * Тип изменения
     */
    @Enumerated(EnumType.STRING)
    private BalanceAuditType type;
}
