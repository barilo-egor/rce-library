package tgb.btc.library.service.process;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import tgb.btc.library.constants.enums.SlotValue;
import tgb.btc.library.constants.enums.properties.PropertiesPath;
import tgb.btc.library.vo.slotReel.ScrollResult;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DiceService {

    @Cacheable("diceSelectWinNumberMessage")
    public String selectBetMessage(){
        return PropertiesPath.DICE_MESSAGE.getString("start")+ System.lineSeparator()+ System.lineSeparator() +
                PropertiesPath.DICE_MESSAGE.getString("start.rules")+ System.lineSeparator()+ System.lineSeparator() +
                "*" + PropertiesPath.DICE_MESSAGE.getString("referral.balance") + "* %s₽"+ System.lineSeparator()+ System.lineSeparator() +
                PropertiesPath.DICE_MESSAGE.getString("start.triple.sum");
    }

    @Cacheable("diceWinMessage")
    public String winMessage(){
        return "*" + PropertiesPath.DICE_MESSAGE.getString("win") + "* %s" + System.lineSeparator() + System.lineSeparator() +
                "*Вы утроили: %s*₽!"+ System.lineSeparator() + System.lineSeparator() +
                "*" + PropertiesPath.DICE_MESSAGE.getString("referral.balance") + "* %s₽";

    }

    @Cacheable("diceLoseMessage")
    public String loseMessage(){
        return PropertiesPath.DICE_MESSAGE.getString("lose") + " %s" + System.lineSeparator() + System.lineSeparator() +
                PropertiesPath.DICE_MESSAGE.getString("selected.number") + " %s"
                + System.lineSeparator() + System.lineSeparator() +
                "*" + PropertiesPath.DICE_MESSAGE.getString("referral.balance") + "* %s₽";
    }

}
