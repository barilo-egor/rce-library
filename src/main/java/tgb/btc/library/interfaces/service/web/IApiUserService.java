package tgb.btc.library.interfaces.service.web;

import tgb.btc.library.bean.web.WebUser;
import tgb.btc.library.bean.web.api.ApiDeal;
import tgb.btc.library.bean.web.api.ApiUser;

public interface IApiUserService {

    long countByToken(String token);

    ApiUser getByToken(String token);

    long countById(String id);

    Boolean isBanned(Long pid);

    Long getPidByToken(String token);

    ApiUser getById(String id);

    Long getLastPaidDealPidByUserPid(Long pid);

    ApiDeal getLastPaidDeal(Long userPid);

    Long getPidByUsername(String username);

    ApiUser getByUsername(String username);

    WebUser getWebUser(Long pid);

    /**
     * DELETE
     */
    void deleteById(String id);

    /**
     * UPDATE
     */
    void updateLastPidDeal(Long userPid, ApiDeal lastPaidDeal);

    void updateWebUser(Long pid, WebUser webUser);
}
