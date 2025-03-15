package tgb.btc.library.bean.bot;

import jakarta.persistence.*;
import lombok.*;
import tgb.btc.library.bean.BasePersist;
import tgb.btc.library.constants.enums.bot.DealType;
import tgb.btc.library.constants.enums.bot.FiatCurrency;
import tgb.btc.library.constants.enums.web.merchant.alfateam.PaymentOption;
import tgb.btc.library.constants.enums.web.merchant.dashpay.OrderMethod;

import java.math.BigDecimal;

@Entity
@Table(name = "PAYMENT_TYPE", uniqueConstraints = @UniqueConstraint(columnNames = {"NAME", "DEAL_TYPE"}))
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
@Data
public class PaymentType extends BasePersist {

    @Column(name = "NAME")
    private String name;

    @Column(name = "IS_ON")
    private Boolean isOn;

    @Column(name = "MIN_SUM")
    private BigDecimal minSum;

    @Column(name = "DEAL_TYPE")
    @Enumerated(value = EnumType.STRING)
    private DealType dealType;

    @Column(name = "IS_DYNAMIC_ON")
    private Boolean isDynamicOn;

    @Column(name = "FIAT_CURRENCY")
    @Enumerated(value = EnumType.STRING)
    private FiatCurrency fiatCurrency;

    private String payscrowPaymentMethodId;

    @Enumerated(value = EnumType.STRING)
    private OrderMethod dashPayOrderMethod;

    @Enumerated(value = EnumType.STRING)
    private PaymentOption alfaTeamPaymentOption;

    public Boolean getOn() {
        return isOn;
    }

    public void setOn(Boolean on) {
        isOn = on;
    }

    public Boolean getDynamicOn() {
        return isDynamicOn;
    }

    public void setDynamicOn(Boolean dynamicOn) {
        isDynamicOn = dynamicOn;
    }
}
