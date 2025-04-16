package tgb.btc.library.bean.bot;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import tgb.btc.library.bean.BasePersist;
import tgb.btc.library.constants.enums.bot.UserRole;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "\"USER\"")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class User extends BasePersist {

    @Column(name = "CHAT_ID", nullable = false, unique = true)
    private Long chatId;

    @Column(name = "USERNAME")
    private String username;

    @Column(name = "REGISTRATION_DATE", nullable = false)
    private LocalDateTime registrationDate;

    @Column(name = "LOTTERY_COUNT")
    private Integer lotteryCount;

    @Column(name = "FROM_CHAT_ID")
    private Long fromChatId;

    @Column(name = "REFERRAL_BALANCE", nullable = false)
    private Integer referralBalance = 0;

    @Column(name = "CHARGES")
    private Integer charges;

    @Column(name = "IS_ACTIVE", nullable = false)
    private Boolean isActive;

    @Column(name = "IS_BANNED", nullable = false)
    private Boolean isBanned;

    @Column(name = "CURRENT_DEAL")
    private Long currentDeal;

    @Column(name = "REFERRAL_PERCENT")
    private BigDecimal referralPercent;

    @OneToMany(fetch = FetchType.EAGER)
    private List<ReferralUser> referralUsers;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "USER_ROLE")
    @ColumnDefault("'USER'")
    @Builder.Default
    private UserRole userRole = UserRole.USER;

    @Column(name = "IS_NOTIFICATIONS_ON")
    private Boolean isNotificationsOn;

    public User(Long pid) {
        this.setPid(pid);
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Boolean getBanned() {
        return isBanned;
    }

    public void setBanned(Boolean banned) {
        isBanned = banned;
    }

    public Boolean getNotificationsOn() {
        return isNotificationsOn;
    }

    public void setNotificationsOn(Boolean notificationsOn) {
        isNotificationsOn = notificationsOn;
    }

    public static User.UserBuilder getDefaultUser() {
        return User.builder()
                .isActive(true)
                .isBanned(false)
                .registrationDate(LocalDateTime.now())
                .referralBalance(0);
    }
}
