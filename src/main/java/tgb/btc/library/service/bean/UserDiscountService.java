package tgb.btc.library.service.bean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tgb.btc.library.bean.bot.UserDiscount;
import tgb.btc.library.repository.BaseRepository;
import tgb.btc.library.repository.bot.UserDiscountRepository;

@Service
public class UserDiscountService extends BasePersistService<UserDiscount> {

    private UserDiscountRepository userDiscountRepository;

    @Autowired
    public void setUserDiscountRepository(UserDiscountRepository userDiscountRepository) {
        this.userDiscountRepository = userDiscountRepository;
    }

    @Autowired
    public UserDiscountService(BaseRepository<UserDiscount> baseRepository) {
        super(baseRepository);
    }

    public boolean isExistByUserPid(Long userPid) {
        return userDiscountRepository.countByUser_Pid(userPid) > 0;
    }
}
