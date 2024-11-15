package tgb.btc.library.bean.bot;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;
import tgb.btc.library.bean.BasePersist;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "LOTTERY_WIN")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Builder
public class LotteryWin extends BasePersist {

    @ManyToOne
    private User user;

    @Column(name = "WON_DATE_TIME")
    private LocalDateTime wonDateTime;

}
