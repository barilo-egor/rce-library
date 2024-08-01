package tgb.btc.library.bean.bot;

import lombok.*;
import tgb.btc.library.bean.BasePersist;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "PAYMENT_REQUISITE")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(callSuper = true)
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

    public Boolean getOn() {
        return isOn;
    }

    public void setOn(Boolean on) {
        isOn = on;
    }
}
