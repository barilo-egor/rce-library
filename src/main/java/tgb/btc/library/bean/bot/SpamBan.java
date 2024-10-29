package tgb.btc.library.bean.bot;

import lombok.*;
import tgb.btc.library.bean.BasePersist;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "SPAM_BAN")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Builder
@Getter
@Setter
public class SpamBan extends BasePersist {
    @OneToOne
    private User user;

    @Column(name = "LOCAL_DATE_TIMEN")
    private LocalDateTime localDateTime;
}
