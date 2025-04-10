package tgb.btc.library.bean.bot;

import jakarta.persistence.*;
import lombok.*;
import tgb.btc.library.bean.BasePersist;
import tgb.btc.library.constants.enums.bot.DealType;
import tgb.btc.library.constants.enums.bot.FiatCurrency;
import tgb.btc.library.constants.enums.web.merchant.alfateam.PaymentOption;
import tgb.btc.library.constants.enums.web.merchant.dashpay.OrderMethod;
import tgb.btc.library.constants.enums.web.merchant.evopay.EvoPayPaymentMethod;
import tgb.btc.library.constants.enums.web.merchant.honeymoney.HoneyMoneyMethod;
import tgb.btc.library.constants.enums.web.merchant.nicepay.NicePayMethod;
import tgb.btc.library.constants.enums.web.merchant.onlypays.OnlyPaysPaymentType;
import tgb.btc.library.constants.enums.web.merchant.payfinity.PayFinityOrderType;
import tgb.btc.library.constants.enums.web.merchant.paypoints.PayPointsMethod;

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

    @Column(length = 500)
    private String requisiteAdditionalText;

    private String payscrowPaymentMethodId;

    @Enumerated(value = EnumType.STRING)
    private OrderMethod dashPayOrderMethod;

    @Enumerated(value = EnumType.STRING)
    private PaymentOption alfaTeamPaymentOption;

    @Enumerated(value = EnumType.STRING)
    private PaymentOption alfaTeamTJSPaymentOption;

    @Enumerated(value = EnumType.STRING)
    private PaymentOption alfaTeamVTBPaymentOption;

    @Enumerated(value = EnumType.STRING)
    private PaymentOption alfaTeamAlfaPaymentOption;

    @Enumerated(value = EnumType.STRING)
    private PaymentOption alfaTeamSberPaymentOption;

    @Enumerated(value = EnumType.STRING)
    private PayPointsMethod payPointsMethod;

    @Enumerated(value = EnumType.STRING)
    private OnlyPaysPaymentType onlyPaysPaymentType;

    @Enumerated(value = EnumType.STRING)
    private EvoPayPaymentMethod evoPayPaymentMethod;

    @Enumerated(value = EnumType.STRING)
    private NicePayMethod nicePayMethod;

    @Enumerated(value = EnumType.STRING)
    private PayFinityOrderType payFinityOrderType;

    @Enumerated(value = EnumType.STRING)
    private HoneyMoneyMethod honeyMoneyMethod;

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
