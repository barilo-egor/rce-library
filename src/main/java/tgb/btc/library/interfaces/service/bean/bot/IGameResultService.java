package tgb.btc.library.interfaces.service.bean.bot;

import tgb.btc.library.bean.GameResult;
import tgb.btc.library.bean.bot.User;
import tgb.btc.library.constants.enums.bot.Game;
import tgb.btc.library.constants.enums.bot.GameResultType;
import tgb.btc.library.interfaces.service.IBasePersistService;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface IGameResultService extends IBasePersistService<GameResult> {

    GameResult save(LocalDateTime dateTime, User user, Game game, GameResultType gameResultType, BigDecimal sum);

    GameResult save(GameResult gameResult);
}
