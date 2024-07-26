package tgb.btc.library.interfaces.service.bean.web;

import org.springframework.transaction.annotation.Transactional;
import tgb.btc.library.bean.web.WebUser;
import tgb.btc.library.bean.web.api.ApiDeal;
import tgb.btc.library.bean.web.api.ApiUser;
import tgb.btc.library.constants.enums.bot.CryptoCurrency;
import tgb.btc.library.constants.enums.bot.FiatCurrency;
import tgb.btc.library.interfaces.service.IBasePersistService;

import java.util.List;

public interface IApiUserService extends IBasePersistService<ApiUser> {

    boolean isExistsById(String id);

    @Transactional
    void delete(String deleteUserId, String newUserId);

    @Transactional
    String generateToken(String username);

    long countByToken(String token);

    ApiUser getByToken(String token);

    long countById(String id);

    Boolean isBanned(Long pid);

    Long getPidByToken(String token);

    ApiUser getById(String id);

    Long getLastPaidDealPidByUserPid(Long pid);

    ApiDeal getLastPaidDeal(Long userPid);

    Long getPidByUsername(String username);

    FiatCurrency getFiatCurrencyByUsername(String username);

    ApiUser getByUsername(String username);

    List<WebUser> getWebUsers(Long pid);

    ApiUser getByGroupChatPid(Long groupChatPid);

    ApiUser getByGroupChatId(Long groupChatId);

    /**
     * DELETE
     */
    void deleteById(String id);

    /**
     * UPDATE
     */
    void updateLastPidDeal(Long userPid, ApiDeal lastPaidDeal);

    CryptoCurrency findMostFrequentCryptoCurrency(Long apiUserPid);
}
