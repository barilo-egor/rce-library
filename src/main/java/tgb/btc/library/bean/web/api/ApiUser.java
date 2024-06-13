package tgb.btc.library.bean.web.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.*;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import tgb.btc.library.bean.BasePersist;
import tgb.btc.library.bean.web.WebUser;
import tgb.btc.library.constants.enums.bot.DealType;
import tgb.btc.library.constants.enums.bot.FiatCurrency;
import tgb.btc.library.interfaces.ObjectNodeConvertable;
import tgb.btc.library.util.web.JacksonUtil;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

@Entity
@Table(name = "API_USER")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiUser extends BasePersist implements ObjectNodeConvertable<ApiUser> {

    @Getter
    @Setter
    @Column(unique = true)
    private String id;

    @Getter
    @Setter
    private BigDecimal personalDiscount;

    @Getter
    @Setter
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private LocalDate registrationDate;

    @Getter
    @Setter
    private Boolean isBanned;

    @Getter
    @Setter
    @Column(unique = true)
    private String token;

    @Getter
    @Setter
    private String buyRequisite;

    @Getter
    @Setter
    private String sellRequisite;

    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FiatCurrency fiatCurrency;

    @OneToMany
    @Setter
    private List<UsdApiUserCourse> usdApiUserCourseList;

    @OneToMany
    @Getter
    @Setter
    private List<ApiUserMinSum> apiUserMinSum;

    @Column(name = "LAST_PAID_DEAL")
    @OneToOne(fetch = FetchType.LAZY)
    @Getter
    @Setter
    private ApiDeal lastPaidDeal;

    @OneToOne
    @Getter
    @Setter
    private WebUser webUser;

    public String getRequisite(DealType dealType) {
        if (DealType.isBuy(dealType)) return buyRequisite;
        else return sellRequisite;
    }

    public List<UsdApiUserCourse> getUsdApiUserCourseList() {
        if (Objects.nonNull(usdApiUserCourseList)) {
            return usdApiUserCourseList;
        } else return new ArrayList<>();
    }


    public UsdApiUserCourse getCourse(FiatCurrency fiatCurrency) {
        return usdApiUserCourseList.stream()
                .filter(course -> fiatCurrency.equals(course.getFiatCurrency()))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Function<ApiUser, ObjectNode> mapFunction() {
        return apiUser -> {
            ObjectNode result = JacksonUtil.getEmpty()
                    .put("pid", apiUser.getPid())
                    .put("id", apiUser.getId())
                    .put("personalDiscount", apiUser.getPersonalDiscount())
                    .put("registrationDate", apiUser.getRegistrationDate().format(DateTimeFormatter.ISO_DATE))
                    .put("isBanned", BooleanUtils.isTrue(apiUser.getIsBanned()))
                    .put("token", apiUser.getToken())
                    .put("buyRequisite", apiUser.getBuyRequisite())
                    .put("sellRequisite", apiUser.getSellRequisite())
                    .put("webUser", Objects.nonNull(apiUser.getWebUser()) ? apiUser.getWebUser().getUsername() :
                            StringUtils.EMPTY);
            if (Objects.nonNull(apiUser.getFiatCurrency())) {
                ObjectNode fiatCurrency = apiUser.getFiatCurrency().mapFunction().apply(apiUser.getFiatCurrency());
                result.set("fiatCurrency", fiatCurrency);
            }
            usdApiUserCourseList.stream()
                    .filter(course -> FiatCurrency.BYN.equals(course.getFiatCurrency()))
                    .findFirst()
                    .ifPresent(course -> result.put("usdCourseBYN", course.getCourse()));
            usdApiUserCourseList.stream()
                    .filter(course -> FiatCurrency.RUB.equals(course.getFiatCurrency()))
                    .findFirst()
                    .ifPresent(course -> result.put("usdCourseRUB", course.getCourse()));
            return result;
        };
    }

}
