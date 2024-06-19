package tgb.btc.library.bean.web.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tgb.btc.library.bean.BasePersist;
import tgb.btc.library.bean.bot.Deal;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "API_CALCULATION")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiCalculation extends BasePersist {

    @OneToOne
    private Deal dealFrom;

    @OneToOne
    private Deal dealTo;

    @ManyToOne
    private ApiUser apiUser;

    @OneToMany
    private List<ApiCalculationData> apiCalculationData;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        if (!super.equals(o))
            return false;
        ApiCalculation that = (ApiCalculation) o;
        return Objects.equals(dealFrom, that.dealFrom) && Objects.equals(dealTo, that.dealTo)
                && Objects.equals(apiUser, that.apiUser) && Objects.equals(apiCalculationData,
                that.apiCalculationData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), dealFrom, dealTo, apiUser, apiCalculationData);
    }

}
