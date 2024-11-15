package tgb.btc.library.bean.bot;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;
import tgb.btc.library.bean.BasePersist;

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
