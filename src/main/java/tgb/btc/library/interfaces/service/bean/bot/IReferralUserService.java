package tgb.btc.library.interfaces.service.bean.bot;

import tgb.btc.library.bean.bot.ReferralUser;
import tgb.btc.library.interfaces.service.IBasePersistService;

public interface IReferralUserService extends IBasePersistService<ReferralUser> {

    ReferralUser save(ReferralUser referralUser);
}
