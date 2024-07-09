package tgb.btc.library.bean.bot;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import tgb.btc.library.bean.BasePersist;
import tgb.btc.library.constants.enums.MemberStatus;
import tgb.btc.library.constants.enums.bot.GroupChatType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GroupChat extends BasePersist {

    @Getter
    @Setter
    @Column(unique = true, nullable = false)
    private Long chatId;

    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @ColumnDefault("'MEMBER'")
    private MemberStatus memberStatus;

    @Getter
    @Setter
    @Column(nullable = false)
    private LocalDateTime registerDateTime;

    @Getter
    @Setter
    private String title;

    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    @ColumnDefault("'DEFAULT'")
    private GroupChatType type;

    @Getter
    @Setter
    @Column(nullable = false)
    @ColumnDefault("true")
    private Boolean isSendMessageEnabled;
}
