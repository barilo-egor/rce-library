package tgb.btc.library.interfaces.service.bean.common.bot;

public interface IUserCommonService {

    boolean isDefaultStep(Long chatId);

    boolean isReferralBalanceEmpty(Long chatId);
}
