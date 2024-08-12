package tgb.btc.library.bean.web.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.*;
import tgb.btc.library.bean.BasePersist;
import tgb.btc.library.constants.enums.bot.CryptoCurrency;
import tgb.btc.library.constants.enums.bot.DealType;
import tgb.btc.library.constants.enums.bot.FiatCurrency;
import tgb.btc.library.interfaces.JsonConvertable;

import javax.persistence.Entity;
import javax.persistence.Table;

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

    private DealType dealType;

    private FiatCurrency fiatCurrency;

    private CryptoCurrency cryptoCurrency;

    private Integer minSum;

    @Override
    public ObjectNode map() {
        return new ObjectMapper()
                .createObjectNode()
                .put("id", id)
                .put("name", name)
                .put("comment", comment)
                .put("dealType", dealType.getNominativeFirstLetterToUpper())
                .put("cryptoCurrency", cryptoCurrency.name())
                .put("minSum", minSum);
    }
}
