package tgb.btc.library.constants.enums.bot;

import com.fasterxml.jackson.databind.node.ObjectNode;
import tgb.btc.library.interfaces.ObjectNodeConvertable;
import tgb.btc.library.util.web.JacksonUtil;

import java.util.function.Function;

public enum DealStatus implements ObjectNodeConvertable<DealStatus> {
    NEW("Новая"),
    AWAITING_VERIFICATION("Ожидает верификацию"),
    VERIFICATION_REJECTED("Верификация отклонена"),
    VERIFICATION_RECEIVED("Верификация получена"),
    CONFIRMED("Подтверждена");

    private final String displayName;

    DealStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public Function<DealStatus, ObjectNode> mapFunction() {
        return dealStatus -> JacksonUtil.getEmpty()
                .put("name", dealStatus.name())
                .put("displayName", dealStatus.getDisplayName());
    }
}
