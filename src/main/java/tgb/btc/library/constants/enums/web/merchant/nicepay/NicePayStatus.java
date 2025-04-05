package tgb.btc.library.constants.enums.web.merchant.nicepay;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.IOException;

@AllArgsConstructor
@Getter
public enum NicePayStatus {
    CREATED(0, "Платёж создан."),
    SEARCHING_DETAILS_1(1, "Поиск реквизитов"),
    SEARCHING_DETAILS_2(2, "Поиск реквизитов"),
    AWAITING_PAYMENT(3, "Ожидание перевода по реквизитам"),
    AWAITING_CONFIRMATION(4, "Ожидание подтверждения оплаты"),
    COMPLETED(5, "Оплата успешно завершена"),
    UNDER_MODERATION(81, "На проверке модераторами"),
    MODERATED_WITHOUT_PAYMENT(82, "Платёж завершён модераторами без проведения оплаты"),
    TIMED_OUT(92, "Платёж завершён так как время на оплату вышло"),
    AWAITING_PROOF(93, "Ожидание подтверждения"),
    CANCELLED_BY_SYSTEM(95, "Платёж отменён системой по времени"),
    CANCELLED_BY_CLIENT(96, "Платёж отменён клиентом");

    private final int status;

    private final String description;

    public static NicePayStatus fromStatus(int status) {
        for (NicePayStatus nicePayStatus: NicePayStatus.values()) {
            if (nicePayStatus.getStatus() == status) {
                return nicePayStatus;
            }
        }
        return null;
    }

    public static class Deserializer extends JsonDeserializer<NicePayStatus> {

        @Override
        public NicePayStatus deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
            return NicePayStatus.fromStatus(p.getIntValue());
        }
    }
}
