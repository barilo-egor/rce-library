package tgb.btc.library.bean.web.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.*;
import org.apache.commons.lang.BooleanUtils;
import tgb.btc.library.bean.BasePersist;
import tgb.btc.library.bean.bot.GroupChat;
import tgb.btc.library.bean.web.WebUser;
import tgb.btc.library.constants.enums.bot.DealType;
import tgb.btc.library.constants.enums.bot.FiatCurrency;
import tgb.btc.library.interfaces.JsonConvertable;
import tgb.btc.library.util.web.JacksonUtil;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "API_USER")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiUser extends BasePersist implements JsonConvertable {

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

    @OneToOne(fetch = FetchType.LAZY)
    @Getter
    @Setter
    private ApiDeal lastPaidDeal;

    @OneToMany
    private List<WebUser> webUsers;

    @OneToOne
    @Getter
    @Setter
    private GroupChat groupChat;

    public List<WebUser> getWebUsers() {
        if (Objects.isNull(webUsers)) return new ArrayList<>();
        return webUsers;
    }

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
    public ObjectNode map() {
        ObjectNode result = JacksonUtil.getEmpty()
                .put("pid", getPid())
                .put("id", getId())
                .put("personalDiscount", getPersonalDiscount())
                .put("registrationDate", getRegistrationDate().format(DateTimeFormatter.ISO_DATE))
                .put("isBanned", BooleanUtils.isTrue(getIsBanned()))
                .put("token", getToken())
                .put("buyRequisite", getBuyRequisite())
                .put("sellRequisite", getSellRequisite());
        if (Objects.nonNull(getFiatCurrency())) {
            ObjectNode fiatCurrencyNode = getFiatCurrency().mapFunction().apply(getFiatCurrency());
            result.set("fiatCurrency", fiatCurrencyNode);
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
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ApiUser apiUser = (ApiUser) o;
        return Objects.equals(id, apiUser.id) && Objects.equals(personalDiscount, apiUser.personalDiscount)
                && Objects.equals(registrationDate, apiUser.registrationDate)
                && Objects.equals(isBanned, apiUser.isBanned)
                && Objects.equals(token, apiUser.token)
                && Objects.equals(buyRequisite, apiUser.buyRequisite)
                && Objects.equals(sellRequisite, apiUser.sellRequisite)
                && fiatCurrency == apiUser.fiatCurrency
                && Objects.equals(usdApiUserCourseList, apiUser.usdApiUserCourseList)
                && Objects.equals(apiUserMinSum, apiUser.apiUserMinSum)
                && Objects.equals(lastPaidDeal, apiUser.lastPaidDeal)
                && Objects.equals(webUsers, apiUser.webUsers) && Objects.equals(groupChat, apiUser.groupChat);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, personalDiscount, registrationDate, isBanned, token, buyRequisite,
                sellRequisite, fiatCurrency, usdApiUserCourseList, apiUserMinSum, lastPaidDeal, webUsers, groupChat);
    }
}
