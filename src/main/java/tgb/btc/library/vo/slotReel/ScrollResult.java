package tgb.btc.library.vo.slotReel;

import lombok.Builder;
import lombok.Getter;
import tgb.btc.library.constants.enums.SlotValue;

@Getter
@Builder
public class ScrollResult {

    private final String winAmount;

    private final SlotValue[] slotValues;

}
