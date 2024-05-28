package tgb.btc.library.constants.enums.web;

import com.fasterxml.jackson.databind.node.ObjectNode;
import tgb.btc.library.interfaces.ObjectNodeConvertable;
import tgb.btc.library.util.web.JacksonUtil;

import java.util.function.Function;

public enum ApiDealStatus implements ObjectNodeConvertable<ApiDealStatus> {
    CREATED("Создана", "blackColor"),
    PAID("Оплачена", "mediumSeaGreenColor"),
    CANCELED("Отменена клиентом", "orangeColor"),
    ACCEPTED("Подтверждена оператором", "lightGrey"),
    DECLINED("Отклонена оператором", "redColor");

    private final String description;

    private final String color;

    ApiDealStatus(String description, String color) {
        this.description = description;
        this.color = color;
    }

    public String getDescription() {
        return description;
    }

    public String getColor() {
        return color;
    }

    @Override
    public Function<ApiDealStatus, ObjectNode> mapFunction() {
        return apiDealStatus -> JacksonUtil.getEmpty()
                .put("name", this.name())
                .put("description", this.getDescription());
    }
}
