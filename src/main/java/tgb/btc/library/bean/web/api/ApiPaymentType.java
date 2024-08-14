package tgb.btc.library.bean.web.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import tgb.btc.library.bean.BasePersist;
import tgb.btc.library.constants.enums.bot.CryptoCurrency;
import tgb.btc.library.constants.enums.bot.DealType;
import tgb.btc.library.constants.enums.bot.FiatCurrency;
import tgb.btc.library.interfaces.JsonConvertable;

import javax.persistence.*;
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
    private FiatCurrency fiatCurrency;

    @Enumerated(EnumType.STRING)
    private CryptoCurrency cryptoCurrency;

    @Column(nullable = false, precision = 10, scale = 8)
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
                .put("fiatCurrency", DealType.BUY.equals(dealType) ? fiatCurrency.name() : null)
                .put("cryptoCurrency", DealType.SELL.equals(dealType) ? cryptoCurrency.name() : null)
                .put("minSum", minSum.stripTrailingZeros().toPlainString());
    }
}
