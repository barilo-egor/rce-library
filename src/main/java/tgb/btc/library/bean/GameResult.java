package tgb.btc.library.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tgb.btc.library.bean.bot.User;
import tgb.btc.library.constants.enums.bot.Game;
import tgb.btc.library.constants.enums.bot.GameResultType;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "GAME_RESULT")
@AllArgsConstructor
@NoArgsConstructor
public class GameResult extends BasePersist {

    @Getter
    @Setter
    private LocalDateTime dateTime;

    @Getter
    @Setter
    @ManyToOne
    private User user;

    @Getter
    @Setter
    @Enumerated(value = EnumType.STRING)
    private Game game;

    @Getter
    @Setter
    @Enumerated(value = EnumType.STRING)
    private GameResultType gameResultType;

    @Getter
    @Setter
    private BigDecimal sum;
}
