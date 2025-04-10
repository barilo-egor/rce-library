package tgb.btc.library.bean.web.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import tgb.btc.library.bean.BasePersist;
import tgb.btc.library.constants.enums.bot.DealType;
import tgb.btc.library.constants.enums.bot.FiatCurrency;
import tgb.btc.library.interfaces.JsonConvertable;

import java.math.BigDecimal;

@Entity
@Table(name = "API_PAYMENT_TYPE")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
@EqualsAndHashCode(callSuper = true)
public class ApiPaymentType extends BasePersist implements JsonConvertable {

    private String id;

    private String name;

    private String comment;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DealType dealType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FiatCurrency fiatCurrency;

    @Column(nullable = false, precision = 15, scale = 8)
    @ColumnDefault("0")
    private BigDecimal minSum;

    @Override
    public ObjectNode map() {
        return new ObjectMapper()
                .createObjectNode()
                .put("pid", getPid())
                .put("id", id)
                .put("name", name)
                .put("comment", comment)
                .put("dealType", dealType.getNominative())
                .put("fiatCurrency", fiatCurrency.name())
                .put("minSum", minSum.stripTrailingZeros().toPlainString());
    }
}
