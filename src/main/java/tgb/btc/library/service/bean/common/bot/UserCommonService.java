package tgb.btc.library.service.bean.common.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tgb.btc.library.bean.bot.User;
import tgb.btc.library.interfaces.service.bean.bot.user.IReadUserService;
import tgb.btc.library.interfaces.service.bean.common.bot.IUserCommonService;

@Service
public class UserCommonService implements IUserCommonService {

    private IReadUserService readUserService;

    @Autowired
    public void setReadUserService(IReadUserService readUserService) {
        this.readUserService = readUserService;
    }

    @Override
    public boolean isDefaultStep(Long chatId) {
        return User.DEFAULT_STEP == readUserService.getStepByChatId(chatId);
    }

}
