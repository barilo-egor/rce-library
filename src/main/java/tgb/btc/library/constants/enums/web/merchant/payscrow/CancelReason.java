package tgb.btc.library.constants.enums.web.merchant.payscrow;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.IOException;

@Getter
@AllArgsConstructor
public enum CancelReason {

    MONEY_NOT_RECEIVED("MoneyNotReceived",
            "Деньги не получены, используется для ордеров на пополнения в странах с ручным флоу."),
    HIGH_RISK_OF_FRAUD("HighRiskOfFraud",
            "При попытке совершить перевод от банка получена ошибка: высокий риск мошенничества."),
    RECIPIENT_SHOULD_VISIT_BANK("RecipientShouldVisitBank",
            "При попытке совершить перевод от банка получена ошибка: Получатель должен посетить банк."),
    TRANSFER_DECLINED_BY_BANK_OF_RECIPIENT("TransferDeclinedByBankOfRecipient",
            "Перевод отклонен банком получателя."),
    REQUESTED_BANK_NOT_FOUND_FOR_RECIPIENT_ACCOUNT("RequestedBankNotFoundForRecipientAccount",
            "Указанный банк в ордере СБП недоступен для указанного номера телефона."),
    RECIPIENT_CARD_IS_LOCKED("RecipientCardIsLocked",
            "Карта получателя заблокирована."),
    INVALID_RECIPIENT_NAME("InvalidRecipientName",
            "Неверное имя получателя. При создании ордера было указано имя получателя, и оно не соответствует данным, полученным при создании перевода."),
    REQUESTED_BY_MERCHANT("RequestedByMerchant",
            "Отменено по запросу мерчанта."),
    UNABLE_TO_PROCESS_ORDER("UnableToProcessOrder",
            "Прочие причины отмены, в том числе из-за проблем на стороне трейдера.");

    final String value;

    final String description;

    public static CancelReason getByValue(String value) {
        for (CancelReason cancelReason : CancelReason.values()) {
            if (cancelReason.value.equals(value)) {
                return cancelReason;
            }
        }
        return null;
    }

    public static class Deserializer extends JsonDeserializer<CancelReason> {
        @Override
        public CancelReason deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
            return CancelReason.getByValue(jsonParser.getValueAsString());
        }
    }
}
