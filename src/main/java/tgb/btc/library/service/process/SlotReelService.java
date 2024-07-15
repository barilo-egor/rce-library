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
public class SlotReelService {

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
        winCondition.put(SlotValue.BAR.getThree(), SlotValue.BAR.getTripleAmount());
        winCondition.put(SlotValue.CHERRY.getThree(), SlotValue.CHERRY.getTripleAmount());
        winCondition.put(SlotValue.LEMON.getThree(), SlotValue.LEMON.getTripleAmount());
        winCondition.put(SlotValue.SEVEN.getThree(), SlotValue.SEVEN.getTripleAmount());
        winCondition.put(SlotValue.BAR.getTwo(), SlotValue.BAR.getDoubleAmount());
        winCondition.put(SlotValue.CHERRY.getTwo(), SlotValue.CHERRY.getDoubleAmount());
        winCondition.put(SlotValue.LEMON.getTwo(), SlotValue.LEMON.getDoubleAmount());
        winCondition.put(SlotValue.SEVEN.getTwo(), SlotValue.SEVEN.getDoubleAmount());
        return winCondition;
    }

    public String slotCombinationToText(SlotValue[] values, String delimiter) {
        return Arrays.stream(values).map(SlotValue::getEmojiValue)
                .collect(Collectors.joining(delimiter));
    }

    @Cacheable("slotReelStartMessage")
    public String startMessage() {
        return PropertiesPath.SLOT_REEL_MESSAGE.getString("start") + System.lineSeparator() + System.lineSeparator() +
                getStartMessageRow(SlotValue.SEVEN.getThree(), SlotValue.SEVEN.getTripleAmount()) +
                getStartMessageRow(SlotValue.LEMON.getThree(), SlotValue.LEMON.getTripleAmount()) +
                getStartMessageRow(SlotValue.CHERRY.getThree(), SlotValue.CHERRY.getTripleAmount()) +
                getStartMessageRow(new SlotValue[]{SlotValue.BAR}, SlotValue.BAR.getTripleAmount()) +
                System.lineSeparator() +
                getStartMessageRow(SlotValue.SEVEN.getTwo(), SlotValue.SEVEN.getDoubleAmount()) +
                getStartMessageRow(SlotValue.LEMON.getTwo(), SlotValue.LEMON.getDoubleAmount()) +
                getStartMessageRow(SlotValue.CHERRY.getTwo(), SlotValue.CHERRY.getDoubleAmount()) +
                getStartMessageRow(new SlotValue[]{SlotValue.BAR}, SlotValue.BAR.getDoubleAmount()) +
                System.lineSeparator() + PropertiesPath.SLOT_REEL_MESSAGE.getString("try.cost") + " " +
                PropertiesPath.SLOT_REEL_PROPERTIES.getString("try") + "₽";
    }

    private String getStartMessageRow(SlotValue[] slotValues, String amount) {
        return slotCombinationToText(slotValues, StringUtils.EMPTY) + " - " + amount + "₽" + System.lineSeparator();

    }

}
