package tgb.btc.library.service.bean.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tgb.btc.library.bean.GameResult;
import tgb.btc.library.bean.bot.Deal;
import tgb.btc.library.bean.bot.User;
import tgb.btc.library.constants.enums.bot.Game;
import tgb.btc.library.constants.enums.bot.GameResultType;
import tgb.btc.library.interfaces.service.bean.bot.IGameResultService;
import tgb.btc.library.repository.BaseRepository;
import tgb.btc.library.repository.bot.GameResultRepository;
import tgb.btc.library.service.bean.BasePersistService;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class GameResultService extends BasePersistService<GameResult> implements IGameResultService {

    private GameResultRepository gameResultRepository;

    @Autowired
    public void setGameResultRepository(GameResultRepository gameResultRepository) {
        this.gameResultRepository = gameResultRepository;
    }

    @Autowired
    public GameResultService(BaseRepository<GameResult> baseRepository) {
        super(baseRepository);
    }

    @Override
    public GameResult save(LocalDateTime dateTime, User user, Game game, GameResultType gameResultType, BigDecimal sum) {
        GameResult gameResult = new GameResult();
        gameResult.setDateTime(dateTime);
        gameResult.setUser(user);
        gameResult.setGame(game);
        gameResult.setGameResultType(gameResultType);
        gameResult.setSum(sum);
        return gameResultRepository.save(gameResult);
    }

    @Override
    public GameResult save(GameResult gameResult) {
        return gameResultRepository.save(gameResult);
    }
}
