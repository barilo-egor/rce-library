package tgb.btc.library.service.process;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import tgb.btc.library.constants.enums.SlotValue;
import tgb.btc.library.constants.enums.properties.PropertiesPath;
import tgb.btc.library.service.properties.SlotReelPropertiesReader;
import tgb.btc.library.vo.slotReel.ScrollResult;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SlotReelService {

    private SlotReelPropertiesReader slotReelPropertiesReader;

    @Autowired
    public void setSlotReelPropertiesReader(SlotReelPropertiesReader slotReelPropertiesReader) {
        this.slotReelPropertiesReader = slotReelPropertiesReader;
    }

    public ScrollResult scrollResult(int diceValue) {
        diceValue = diceValue - 1;
        SlotValue[] values = SlotValue.values();
        SlotValue[] result = new SlotValue[3];
        for (int i = 0; i < 3; i++) {
            int ii = diceValue % 4;
            diceValue = diceValue / 4;
            result[i] = values[ii];
        }

        Map<SlotValue[], String> winCondition = getWinCondition();
        Optional<SlotValue[]> str = winCondition.keySet().stream().filter(key ->
                        Arrays.compare(key, result) == 0 ||
                                Arrays.compare(key, Arrays.copyOf(result, result.length - 1)) == 0)
                .findFirst();
        return str.map(value -> ScrollResult.builder()
                        .winAmount(winCondition.get(value)).slotValues(result).build())
                .orElse(ScrollResult.builder().slotValues(result).build());
    }

    @Cacheable("slotReelWinCondition")
    public Map<SlotValue[], String> getWinCondition() {
        Map<SlotValue[], String> winCondition = new LinkedHashMap<>();
        winCondition.put(SlotValue.BAR.getThree(), getTripleAmount(SlotValue.BAR));
        winCondition.put(SlotValue.CHERRY.getThree(), getTripleAmount(SlotValue.CHERRY));
        winCondition.put(SlotValue.LEMON.getThree(), getTripleAmount(SlotValue.LEMON));
        winCondition.put(SlotValue.SEVEN.getThree(), getTripleAmount(SlotValue.SEVEN));
        winCondition.put(SlotValue.BAR.getTwo(), getDoubleAmount(SlotValue.BAR));
        winCondition.put(SlotValue.CHERRY.getTwo(), getDoubleAmount(SlotValue.CHERRY));
        winCondition.put(SlotValue.LEMON.getTwo(), getDoubleAmount(SlotValue.LEMON));
        winCondition.put(SlotValue.SEVEN.getTwo(), getDoubleAmount(SlotValue.SEVEN));
        return winCondition;
    }

    private String getTripleAmount(SlotValue slotValue) {
        return slotReelPropertiesReader.getString(slotValue.name().toLowerCase().concat(".triple"));
    }

    private String getDoubleAmount(SlotValue slotValue) {
        return slotReelPropertiesReader.getString(slotValue.name().toLowerCase().concat(".double"));
    }

    public String slotCombinationToText(SlotValue[] values, String delimiter) {
        return Arrays.stream(values).map(SlotValue::getEmojiValue)
                .collect(Collectors.joining(delimiter));
    }

    @Cacheable("slotReelStartMessage")
    public String startMessage() {
        return PropertiesPath.SLOT_REEL_MESSAGE.getString("start") + System.lineSeparator() + System.lineSeparator() +
                getStartMessageRow(SlotValue.SEVEN.getThree(), getTripleAmount(SlotValue.SEVEN)) +
                getStartMessageRow(SlotValue.LEMON.getThree(), getTripleAmount(SlotValue.LEMON)) +
                getStartMessageRow(SlotValue.CHERRY.getThree(), getTripleAmount(SlotValue.CHERRY)) +
                getStartMessageRow(new SlotValue[]{SlotValue.BAR}, getTripleAmount(SlotValue.BAR)) +
                System.lineSeparator() +
                getStartMessageRow(SlotValue.SEVEN.getTwo(), getDoubleAmount(SlotValue.SEVEN)) +
                getStartMessageRow(SlotValue.LEMON.getTwo(), getDoubleAmount(SlotValue.LEMON)) +
                getStartMessageRow(SlotValue.CHERRY.getTwo(), getDoubleAmount(SlotValue.CHERRY)) +
                getStartMessageRow(new SlotValue[]{SlotValue.BAR}, getDoubleAmount(SlotValue.BAR)) +
                System.lineSeparator() + PropertiesPath.SLOT_REEL_MESSAGE.getString("try.cost") + " " +
                slotReelPropertiesReader.getString("try") + "₽";
    }

    private String getStartMessageRow(SlotValue[] slotValues, String amount) {
        return slotCombinationToText(slotValues, StringUtils.EMPTY) + " - " + amount + "₽" + System.lineSeparator();

    }

}
