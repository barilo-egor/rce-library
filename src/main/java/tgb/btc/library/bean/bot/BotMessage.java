package tgb.btc.library.bean.bot;

import jakarta.persistence.*;
import lombok.*;
import tgb.btc.library.bean.BasePersist;
import tgb.btc.library.constants.enums.bot.BotMessageType;
import tgb.btc.library.constants.enums.bot.MessageType;


@Setter
@Getter
@Entity
@Table(name = "BOT_MESSAGE")
@Builder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class BotMessage extends BasePersist {
    @Column(name = "TYPE")
    @Enumerated(value = EnumType.STRING)
    private BotMessageType type;
    @Column(name = "TEXT", length = 2000)
    private String text;
    @Column(name = "IMAGE", length = 500)
    private String image;
    @Column(name = "ANIMATION", length = 500)
    private String animation;
    @Column(name = "MESSAGE_TYPE", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private MessageType messageType = MessageType.TEXT;

    @Override
    public String toString() {
        return "BotMessage{" +
                "type=" + type +
                ", text='" + text + '\'' +
                ", image='" + image + '\'' +
                ", animation='" + animation + '\'' +
                ", messageType=" + messageType +
                '}';
    }
}
