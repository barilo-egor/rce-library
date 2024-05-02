package tgb.btc.library.bean.bot;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import tgb.btc.library.bean.BasePersist;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "PAYMENT_REQUISITE")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentRequisite extends BasePersist {

    @ManyToOne
    private PaymentType paymentType;

    @Column(name = "NAME")
    private String name;

    @Column(name = "REQUISITE")
    private String requisite;

    @Column(name = "IS_ON")
    private Boolean isOn;

    public PaymentRequisite(Long pid) {
        super(pid);
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public String getRequisite() {
        return requisite;
    }

    public void setRequisite(String requisite) {
        this.requisite = requisite;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getOn() {
        return isOn;
    }

    public void setOn(Boolean on) {
        isOn = on;
    }
}
