package tgb.btc.library.service.process;

import jakarta.annotation.PostConstruct;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tgb.btc.library.constants.enums.bot.DealStatus;
import tgb.btc.library.interfaces.service.bean.bot.deal.IReadDealService;
import tgb.btc.library.service.properties.ModulesPropertiesReader;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class VerifiedUserCache {

    private static final Map<Long, Boolean> VERIFIED_USERS = new ConcurrentHashMap<>();

    private IReadDealService readDealService;

    private ModulesPropertiesReader modulesPropertiesReader;

    private Long count = -1L;

    @Autowired
    public void setModulesPropertiesReader(ModulesPropertiesReader modulesPropertiesReader) {
        this.modulesPropertiesReader = modulesPropertiesReader;
    }

    @PostConstruct
    public void init() {
        Long propertyCount = modulesPropertiesReader.getLong("anti.spam.ignore.count.deals", null);
        if (Objects.nonNull(propertyCount)) {
            count = propertyCount;
        }
    }

    @Autowired
    public void setReadDealService(IReadDealService readDealService) {
        this.readDealService = readDealService;
    }

    private void add(Long chatId) {
        VERIFIED_USERS.put(chatId, Boolean.TRUE);
    }

    public boolean check(Long chatId) {
        Boolean result = VERIFIED_USERS.get(chatId);
        if (Objects.nonNull(result)) return result;
        if (count < 0) {
            return true;
        }
        if (BooleanUtils.isTrue(readDealService.dealsByUserChatIdIsExist(chatId, DealStatus.CONFIRMED, count))) {
            add(chatId);
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

}
