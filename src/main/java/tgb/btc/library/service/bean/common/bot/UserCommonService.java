package tgb.btc.library.service.bean.common.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tgb.btc.library.interfaces.service.bean.bot.user.IReadUserService;
import tgb.btc.library.interfaces.service.bean.common.bot.IUserCommonService;

import java.util.Objects;

@Service
@Transactional
public class UserCommonService implements IUserCommonService {

    private IReadUserService readUserService;

    @Autowired
    public void setReadUserService(IReadUserService readUserService) {
        this.readUserService = readUserService;
    }

    @Override
    public boolean isReferralBalanceEmpty(Long chatId) {
        Integer referralBalance = readUserService.getReferralBalanceByChatId(chatId);
        return Objects.nonNull(referralBalance) && referralBalance == 0;
    }
}
