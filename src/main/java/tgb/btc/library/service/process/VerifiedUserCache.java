package tgb.btc.library.service.process;

import org.apache.commons.lang.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tgb.btc.library.constants.enums.bot.DealStatus;
import tgb.btc.library.constants.enums.properties.PropertiesPath;
import tgb.btc.library.repository.bot.DealRepository;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class VerifiedUserCache {

    private static final Map<Long, Boolean> VERIFIED_USERS = new ConcurrentHashMap<>();

    private DealRepository dealRepository;

    @Autowired
    public void setDealRepository(DealRepository dealRepository) {
        this.dealRepository = dealRepository;
    }

    private void add(Long chatId) {
        VERIFIED_USERS.put(chatId, Boolean.TRUE);
    }

    public boolean check(Long chatId) {
        Boolean result = VERIFIED_USERS.get(chatId);
        if (Objects.nonNull(result)) return result;
        Long countDeals = Long.valueOf(PropertiesPath.MODULES_PROPERTIES.getString("anti.spam.ignore.count.deals"));
        if (BooleanUtils.isTrue(dealRepository.dealsByUserChatIdIsExist(chatId, DealStatus.CONFIRMED,countDeals))) {
            add(chatId);
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

}
