package tgb.btc.library.bean.web.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tgb.btc.library.bean.BasePersist;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "API_CALCULATION")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiCalculation extends BasePersist {

    @OneToMany
    private List<ApiDeal> deals;

    @ManyToOne
    private ApiUser apiUser;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        if (!super.equals(o))
            return false;
        ApiCalculation that = (ApiCalculation) o;
        return Objects.equals(deals, that.deals) && Objects.equals(apiUser, that.apiUser);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), deals, apiUser);
    }

}
