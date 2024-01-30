package tgb.btc.library.service.bean.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tgb.btc.library.bean.GameResult;
import tgb.btc.library.bean.bot.User;
import tgb.btc.library.constants.enums.bot.Game;
import tgb.btc.library.constants.enums.bot.GameResultType;
import tgb.btc.library.repository.bot.GameResultRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class GameResultService {

    private GameResultRepository gameResultRepository;

    @Autowired
    public GameResultService(GameResultRepository gameResultRepository) {
        this.gameResultRepository = gameResultRepository;
    }

    public GameResult save(LocalDateTime dateTime, User user, Game game, GameResultType gameResultType, BigDecimal sum) {
        GameResult gameResult = new GameResult();
        gameResult.setDateTime(dateTime);
        gameResult.setUser(user);
        gameResult.setGame(game);
        gameResult.setGameResultType(gameResultType);
        gameResult.setSum(sum);
        return gameResultRepository.save(gameResult);
    }

    public GameResult save(GameResult gameResult) {
        return gameResultRepository.save(gameResult);
    }
}
