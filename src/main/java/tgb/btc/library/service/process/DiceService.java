package tgb.btc.library.service.process;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import tgb.btc.library.service.properties.DiceMessagePropertiesReader;

@Service
@Slf4j
public class DiceService {

    private DiceMessagePropertiesReader diceMessagePropertiesReader;

    @Autowired
    public void setDiceMessagePropertiesReader(DiceMessagePropertiesReader diceMessagePropertiesReader) {
        this.diceMessagePropertiesReader = diceMessagePropertiesReader;
    }

    @Cacheable("diceSelectWinNumberMessage")
    public String selectBetMessage(){
        return diceMessagePropertiesReader.getString("start")+ System.lineSeparator()+ System.lineSeparator() +
                diceMessagePropertiesReader.getString("start.rules")+ System.lineSeparator()+ System.lineSeparator() +
                "*" + diceMessagePropertiesReader.getString("referral.balance") + "* %s₽"+ System.lineSeparator()+ System.lineSeparator() +
                diceMessagePropertiesReader.getString("start.triple.sum");
    }

    @Cacheable("diceWinMessage")
    public String winMessage(){
        return "*" + diceMessagePropertiesReader.getString("win") + "* %s" + System.lineSeparator() + System.lineSeparator() +
                "*Вы утроили: %s*₽!"+ System.lineSeparator() + System.lineSeparator() +
                "*" + diceMessagePropertiesReader.getString("referral.balance") + "* %s₽";

    }

    @Cacheable("diceLoseMessage")
    public String loseMessage(){
        return diceMessagePropertiesReader.getString("lose") + " %s" + System.lineSeparator() + System.lineSeparator() +
                diceMessagePropertiesReader.getString("selected.number") + " %s"
                + System.lineSeparator() + System.lineSeparator() +
                "*" + diceMessagePropertiesReader.getString("referral.balance") + "* %s₽";
    }

}
