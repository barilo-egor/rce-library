package tgb.btc.library.constants.enums.bot;

import com.fasterxml.jackson.databind.node.ObjectNode;
import tgb.btc.library.interfaces.ObjectNodeConvertable;
import tgb.btc.library.util.web.JacksonUtil;

import java.util.function.Function;

public enum DealStatus implements ObjectNodeConvertable<DealStatus> {
    NEW("Новая", "blackColor"),
    PAID("Оплачено", "mediumSeaGreenColor"),
    AWAITING_VERIFICATION("Ожидает верификацию", "orangeColor"),
    VERIFICATION_REJECTED("Верификация отклонена", "redColor"),
    VERIFICATION_RECEIVED("Верификация получена", "violetColor"),
    AWAITING_WITHDRAWAL("Ожидает вывод", "powderBlueColor"),
    CONFIRMED("Подтверждена", "lightGrey");

    private final String displayName;

    private final String color;

    DealStatus(String displayName, String color) {
        this.displayName = displayName;
        this.color = color;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getColor() {
        return color;
    }

    @Override
    public Function<DealStatus, ObjectNode> mapFunction() {
        return dealStatus -> JacksonUtil.getEmpty()
                .put("name", dealStatus.name())
                .put("displayName", dealStatus.getDisplayName());
    }
}
