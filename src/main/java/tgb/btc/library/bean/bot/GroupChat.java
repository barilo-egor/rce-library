package tgb.btc.library.bean.bot;

import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import tgb.btc.library.bean.BasePersist;
import tgb.btc.library.constants.enums.MemberStatus;
import tgb.btc.library.constants.enums.bot.GroupChatType;
import tgb.btc.library.interfaces.JsonConvertable;
import tgb.btc.library.util.web.JacksonUtil;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class GroupChat extends BasePersist implements JsonConvertable {

    @Column(unique = true, nullable = false)
    private Long chatId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @ColumnDefault("'MEMBER'")
    private MemberStatus memberStatus;

    @Column(nullable = false)
    private LocalDateTime registerDateTime;

    private String title;

    @Enumerated(EnumType.STRING)
    @ColumnDefault("'DEFAULT'")
    private GroupChatType type;

    @Column(nullable = false)
    @ColumnDefault("true")
    private Boolean isSendMessageEnabled;

    @Override
    public ObjectNode map() {
        return JacksonUtil.getEmpty()
                .put("title", this.getTitle())
                .put("pid", this.getPid());
    }

    public static GroupChat empty() {
        return GroupChat.builder()
                .title("Отсутствует")
                .build();
    }

}
