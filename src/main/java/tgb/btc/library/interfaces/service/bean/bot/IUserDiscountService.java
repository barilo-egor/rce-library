package tgb.btc.library.interfaces.service.bean.bot;

import tgb.btc.library.bean.bot.UserDiscount;
import tgb.btc.library.interfaces.service.IBasePersistService;

import java.math.BigDecimal;

public interface IUserDiscountService extends IBasePersistService<UserDiscount> {

    /** SELECT **/

    Boolean getRankDiscountByUserChatId(Long chatId);

    BigDecimal getPersonalBuyByChatId(Long chatId);

    BigDecimal getPersonalSellByChatId(Long chatId);

    Long countByUser_Pid(Long userPid);

    UserDiscount getByUserChatId(Long chatId);

    boolean isExistByUserPid(Long userPid);

    /** UPDATE **/

    UserDiscount save(UserDiscount userDiscount);

    void updateIsRankDiscountOnByPid(Boolean isRankDiscountOn, Long pid);

    void updatePersonalBuyByUserPid(BigDecimal personalBuy, Long userPid);

    void updatePersonalSellByUserPid(BigDecimal personalSell, Long userPid);

    void deleteByUser_ChatId(Long userChatId);
}
