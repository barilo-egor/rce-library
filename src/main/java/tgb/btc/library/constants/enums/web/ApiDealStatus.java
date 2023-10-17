package tgb.btc.library.constants.enums.web;

import com.fasterxml.jackson.databind.node.ObjectNode;
import tgb.btc.library.interfaces.ObjectNodeConvertable;
import tgb.btc.library.util.web.JacksonUtil;

import java.util.function.Function;

public enum ApiDealStatus implements ObjectNodeConvertable<ApiDealStatus> {
    CREATED("Создана"),
    PAID("Оплачена"),
    CANCELED("Отменена клиентом"),
    ACCEPTED("Подтверждена оператором"),
    DECLINED("Отклонена оператором");

    private final String description;

    ApiDealStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public Function<ApiDealStatus, ObjectNode> mapFunction() {
        return apiDealStatus -> JacksonUtil.getEmpty()
                .put("name", this.name())
                .put("description", this.getDescription());
    }
}
