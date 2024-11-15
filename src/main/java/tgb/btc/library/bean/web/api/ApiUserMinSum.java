package tgb.btc.library.bean.web.api;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.*;
import tgb.btc.library.bean.BasePersist;
import tgb.btc.library.constants.enums.bot.CryptoCurrency;
import tgb.btc.library.constants.enums.bot.DealType;

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
