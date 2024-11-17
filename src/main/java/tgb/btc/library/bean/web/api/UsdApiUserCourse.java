package tgb.btc.library.bean.web.api;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import tgb.btc.library.bean.BasePersist;
import tgb.btc.library.constants.enums.bot.FiatCurrency;

import java.math.BigDecimal;

@Entity
@Table(name = "USD_API_USER_COURSE")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsdApiUserCourse extends BasePersist {

    private FiatCurrency fiatCurrency;

    private BigDecimal course;

    public FiatCurrency getFiatCurrency() {
        return fiatCurrency;
    }

    public void setFiatCurrency(FiatCurrency fiatCurrency) {
        this.fiatCurrency = fiatCurrency;
    }

    public BigDecimal getCourse() {
        return course;
    }

    public void setCourse(BigDecimal course) {
        this.course = course;
    }
}
