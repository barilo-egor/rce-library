package tgb.btc.library.bean.web.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.*;
import org.apache.commons.lang.BooleanUtils;
import tgb.btc.library.bean.BasePersist;
import tgb.btc.library.bean.bot.GroupChat;
import tgb.btc.library.bean.web.WebUser;
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
@EqualsAndHashCode(callSuper = true)
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

    @OneToMany(fetch = FetchType.EAGER)
    private List<WebUser> webUsers;

    @OneToOne
    @Getter
    @Setter
    private GroupChat groupChat;

    @Getter
    @Setter
    @ManyToMany
    private List<ApiPaymentType> paymentTypes;

    public List<WebUser> getWebUsers() {
        if (Objects.isNull(webUsers)) return new ArrayList<>();
        return webUsers;
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
                .put("token", getToken());
        if (Objects.nonNull(getFiatCurrency())) {
            ObjectNode fiatCurrencyNode = getFiatCurrency().mapFunction().apply(getFiatCurrency());
            result.set("fiatCurrency", fiatCurrencyNode);
        }
        ObjectNode groupChatNode;
        if (Objects.nonNull(getGroupChat())) {
            groupChatNode = getGroupChat().map();
        } else {
            groupChatNode = GroupChat.empty().map();
        }
        result.set("groupChat", groupChatNode);
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
}
