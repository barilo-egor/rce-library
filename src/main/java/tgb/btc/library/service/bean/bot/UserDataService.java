package tgb.btc.library.service.bean.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tgb.btc.library.bean.bot.UserData;
import tgb.btc.library.constants.enums.bot.CryptoCurrency;
import tgb.btc.library.constants.enums.bot.DealType;
import tgb.btc.library.constants.enums.bot.FiatCurrency;
import tgb.btc.library.interfaces.service.bean.bot.IUserDataService;
import tgb.btc.library.repository.BaseRepository;
import tgb.btc.library.repository.bot.UserDataRepository;
import tgb.btc.library.service.bean.BasePersistService;

@Service
public class UserDataService extends BasePersistService<UserData> implements IUserDataService {

    private UserDataRepository userDataRepository;

    @Autowired
    public void setUserDataRepository(UserDataRepository userDataRepository) {
        this.userDataRepository = userDataRepository;
    }

    @Autowired
    public UserDataService(BaseRepository<UserData> baseRepository) {
        super(baseRepository);
    }

    @Override
    public UserData save(UserData userData) {
        return userDataRepository.save(userData);
    }

    @Override
    public Long getLongByUserPid(Long userPid) {
        return userDataRepository.getLongByUserPid(userPid);
    }

    @Override
    public String getStringByUserChatId(Long chatId) {
        return userDataRepository.getStringByUserChatId(chatId);
    }

    @Override
    public DealType getDealTypeByChatId(Long chatId) {
        return userDataRepository.getDealTypeByChatId(chatId);
    }

    @Override
    public FiatCurrency getFiatCurrencyByChatId(Long chatId) {
        return userDataRepository.getFiatCurrencyByChatId(chatId);
    }

    @Override
    public CryptoCurrency getCryptoCurrencyByChatId(Long chatId) {
        return userDataRepository.getCryptoCurrencyByChatId(chatId);
    }

    @Override
    public Long countByUserPid(Long userPid) {
        return userDataRepository.countByUserPid(userPid);
    }

    @Override
    public void updateLongByUserPid(Long userPid, Long longVariable) {
        userDataRepository.updateLongByUserPid(userPid, longVariable);
    }

    @Override
    public void updateStringByUserChatId(Long chatId, String stringVariable) {
        userDataRepository.updateStringByUserChatId(chatId, stringVariable);
    }

    @Override
    public void updateDealTypeByUserChatId(Long userChatId, DealType dealTypeVariable) {
        userDataRepository.updateDealTypeByUserChatId(userChatId, dealTypeVariable);
    }

    @Override
    public void updateFiatCurrencyByUserChatId(Long chatId, FiatCurrency fiatCurrency) {
        userDataRepository.updateFiatCurrencyByUserChatId(chatId, fiatCurrency);
    }

    @Override
    public void deleteByUser_ChatId(Long userChatId) {
        userDataRepository.deleteByUser_ChatId(userChatId);
    }

    @Override
    public void updateCryptoCurrencyByChatId(Long userChatId, CryptoCurrency cryptoCurrency) {
        userDataRepository.updateCryptoCurrencyByChatId(userChatId, cryptoCurrency);
    }
}
