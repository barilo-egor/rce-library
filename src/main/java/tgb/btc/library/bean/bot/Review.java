package tgb.btc.library.bean.bot;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.*;
import tgb.btc.library.bean.BasePersist;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "REVIEW")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
public class Review extends BasePersist {

    @Column(name = "TEXT")
    private String text;

    @Column(name = "CHAT_ID")
    private Long chatId;

    @Column(name = "USERNAME")
    private String username;

    @Column(name = "AMOUNT")
    private Integer amount;

    @Column(name = "IS_ACCEPTED", columnDefinition = "BIT(1) DEFAULT 0")
    private Boolean isAccepted = false;

    @OneToOne
    @JsonIgnore
    private Deal deal;

}
