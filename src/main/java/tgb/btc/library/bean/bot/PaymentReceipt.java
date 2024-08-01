package tgb.btc.library.bean.bot;

import lombok.*;
import tgb.btc.library.bean.BasePersist;
import tgb.btc.library.constants.enums.bot.ReceiptFormat;

import javax.persistence.*;

@Setter
@Getter
@Entity
@Table(name = "PAYMENT_RECEIPT")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PaymentReceipt extends BasePersist {

    @Column(name = "RECEIPT", length = 1000)
    private String receipt;

    @Column(name = "RECEIPT_FORMAT")
    @Enumerated(EnumType.STRING)
    private ReceiptFormat receiptFormat;

    @ManyToOne
    private Deal deal;

    @Override
    public String toString() {
        return "PaymentReceipt{" +
                "receipt='" + receipt + '\'' +
                ", receiptFormat=" + receiptFormat +
                '}';
    }
}
