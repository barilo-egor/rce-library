package tgb.btc.library.bean.web.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tgb.btc.library.bean.BasePersist;
import tgb.btc.library.constants.enums.bot.CryptoCurrency;
import tgb.btc.library.constants.enums.bot.DealType;
import tgb.btc.library.constants.enums.bot.FiatCurrency;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "API_CALCULATION_DATA")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiCalculationData extends BasePersist {

    @Enumerated(EnumType.STRING)
    private DealType dealType;

    @Enumerated(EnumType.STRING)
    private FiatCurrency fiatCurrency;

    @Enumerated(EnumType.STRING)
    private CryptoCurrency cryptoCurrency;

    private BigDecimal fiatAmount;

    private BigDecimal cryptoAmount;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        if (!super.equals(o))
            return false;
        ApiCalculationData that = (ApiCalculationData) o;
        return dealType == that.dealType && fiatCurrency == that.fiatCurrency && cryptoCurrency == that.cryptoCurrency
                && Objects.equals(fiatAmount, that.fiatAmount) && Objects.equals(cryptoAmount,
                that.cryptoAmount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), dealType, fiatCurrency, cryptoCurrency, fiatAmount, cryptoAmount);
    }

}
