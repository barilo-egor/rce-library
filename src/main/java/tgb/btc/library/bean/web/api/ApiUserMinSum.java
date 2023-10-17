package tgb.btc.library.bean.web.api;

import lombok.*;
import tgb.btc.library.bean.BasePersist;
import tgb.btc.library.constants.enums.bot.CryptoCurrency;
import tgb.btc.library.constants.enums.bot.DealType;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "API_USER_MIN_SUM")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiUserMinSum extends BasePersist {

    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    private DealType dealType;

    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    private CryptoCurrency cryptoCurrency;

    @Getter
    @Setter
    private BigDecimal amount;
}
