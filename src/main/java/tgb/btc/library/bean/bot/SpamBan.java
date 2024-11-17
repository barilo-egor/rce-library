package tgb.btc.library.bean.bot;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.*;
import tgb.btc.library.bean.BasePersist;

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
