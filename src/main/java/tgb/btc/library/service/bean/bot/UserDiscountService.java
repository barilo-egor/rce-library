package tgb.btc.library.service.bean.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tgb.btc.library.bean.bot.UserDiscount;
import tgb.btc.library.interfaces.service.bean.bot.IUserDiscountService;
import tgb.btc.library.repository.BaseRepository;
import tgb.btc.library.repository.bot.UserDiscountRepository;
import tgb.btc.library.service.bean.BasePersistService;

import java.math.BigDecimal;

@Service
public class UserDiscountService extends BasePersistService<UserDiscount> implements IUserDiscountService {

    private UserDiscountRepository userDiscountRepository;

    @Autowired
    public void setUserDiscountRepository(UserDiscountRepository userDiscountRepository) {
        this.userDiscountRepository = userDiscountRepository;
    }

    @Override
    public Boolean getRankDiscountByUserChatId(Long chatId) {
        return userDiscountRepository.getRankDiscountByUserChatId(chatId);
    }

    @Override
    public BigDecimal getPersonalBuyByChatId(Long chatId) {
        return userDiscountRepository.getPersonalBuyByChatId(chatId);
    }

    @Override
    public BigDecimal getPersonalSellByChatId(Long chatId) {
        return userDiscountRepository.getPersonalSellByChatId(chatId);
    }

    @Override
    public Long countByUser_Pid(Long userPid) {
        return userDiscountRepository.countByUser_Pid(userPid);
    }

    @Override
    public UserDiscount getByUserChatId(Long chatId) {
        return userDiscountRepository.getByUserChatId(chatId);
    }

    public boolean isExistByUserPid(Long userPid) {
        return userDiscountRepository.countByUser_Pid(userPid) > 0;
    }

    @Override
    public void updateIsRankDiscountOnByPid(Boolean isRankDiscountOn, Long pid) {
        userDiscountRepository.updateIsRankDiscountOnByPid(isRankDiscountOn, pid);
    }

    @Override
    public void updatePersonalBuyByUserPid(BigDecimal personalBuy, Long userPid) {
        userDiscountRepository.updatePersonalBuyByUserPid(personalBuy, userPid);
    }

    @Override
    public void updatePersonalSellByUserPid(BigDecimal personalSell, Long userPid) {
        userDiscountRepository.updatePersonalSellByUserPid(personalSell, userPid);
    }

    @Override
    public void deleteByUser_ChatId(Long userChatId) {
        userDiscountRepository.deleteByUser_ChatId(userChatId);
    }

    @Override
    protected BaseRepository<UserDiscount> getBaseRepository() {
        return userDiscountRepository;
    }

}
