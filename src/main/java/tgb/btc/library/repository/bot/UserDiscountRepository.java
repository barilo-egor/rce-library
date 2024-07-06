package tgb.btc.library.repository.bot;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import tgb.btc.library.bean.bot.UserDiscount;
import tgb.btc.library.repository.BaseRepository;

import java.math.BigDecimal;

@Repository
@Transactional
public interface UserDiscountRepository extends BaseRepository<UserDiscount> {

    /** SELECT **/
    @Query("select isRankDiscountOn from UserDiscount where user.chatId=:chatId")
    Boolean getRankDiscountByUserChatId(Long chatId);

    @Query("select personalBuy from UserDiscount where user.chatId=:chatId")
    BigDecimal getPersonalBuyByChatId(Long chatId);

    @Query("select personalSell from UserDiscount where user.chatId=:chatId")
    BigDecimal getPersonalSellByChatId(Long chatId);

    Long countByUser_Pid(Long userPid);

    @Query("from UserDiscount where user.chatId=:chatId")
    UserDiscount getByUserChatId(Long chatId);


    /** UPDATE **/
    @Query("update UserDiscount set isRankDiscountOn=:isRankDiscountOn where user.pid=:pid")
    @Modifying
    void updateIsRankDiscountOnByPid(Boolean isRankDiscountOn, Long pid);

    @Query("update UserDiscount set personalBuy=:personalBuy where user.pid=:userPid")
    @Modifying
    void updatePersonalBuyByUserPid(BigDecimal personalBuy, Long userPid);

    @Query("update UserDiscount set personalSell=:personalSell where user.pid=:userPid")
    @Modifying
    void updatePersonalSellByUserPid(BigDecimal personalSell, Long userPid);

    @Modifying
    void deleteByUser_ChatId(Long userChatId);
}
