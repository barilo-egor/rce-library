package tgb.btc.library.bean.bot;

import lombok.*;
import tgb.btc.library.bean.BasePersist;
import tgb.btc.library.constants.enums.bot.WithdrawalRequestStatus;

import javax.persistence.*;

@Entity
@Table(name = "WITHDRAWAL_REQUEST")
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class WithdrawalRequest extends BasePersist {
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;

    @Column(name = "STATUS")
    private WithdrawalRequestStatus status;

    @Column(name = "IS_ACTIVE")
    private Boolean isActive;

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    @Override
    public String toString() {
        return "WithdrawalRequest{" +
                "user=" + user +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", status=" + status +
                ", isActive=" + isActive +
                '}';
    }
}
