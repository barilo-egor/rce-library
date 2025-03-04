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
public enum ErrorCode {
    OK(0, "Ok"),
    ORDER_NOT_FOUND_BY_ID(101, "Не удается найти ордер по указанному Id."),
    ORDER_NOT_FOUND_BY_EXTERNAL_ID(102, "Не удается найти ордер по указанному External Id."),
    INVALID_ORDER_STATUS_FOR_OPERATION(103, "Недопустимый текущий статус ордера для вызываемого метода."),
    INVALID_ORDER_SIDE_FOR_OPERATION(104, "Недопустимая сторона ордера для вызываемой операции."),
    BUY_ORDERS_FORBIDDEN_FOR_MERCHANT(105, "Создание ордеров Buy запрещено для мерчанта."),
    SELL_ORDERS_FORBIDDEN_FOR_MERCHANT(106, "Создание ордеров Sell запрещено для мерчанта."),
    ACTIVE_ORDERS_LIMIT_REACHED(107, "Достигнут лимит активных ордеров для мерчанта."),
    EMPTY_CLIENT_NAME_FORBIDDEN(108, "Пустое имя клиента запрещено для мерчанта."),
    PAYMENT_METHOD_NOT_FOUND(109, "Платежный метод указанный в запросе не найден."),
    PAYMENT_METHOD_DISABLED(110, "Платежный метод указанный в запросе отключен."),
    INVALID_CURRENCY_VALUE(111, "Недопустимое значение валюты в запросе."),
    INVALID_FIAT_AMOUNT(113, "Недопустимая фиатная сумма ордера."),
    INSUFFICIENT_BALANCE(114, "Недостаточный баланс для проведения вызываемой операции."),
    NO_AVAILABLE_TRADERS(115, "Нет доступных трейдеров."),
    DUPLICATE_EXTERNAL_ID(116, "Дубликат External Id."),
    INVALID_PAYMENT_SYSTEM_TYPE_FOR_SMART_ORDER(117, "Недопустимый тип платежной системы для смарт ордера."),
    NO_SUITABLE_PAYMENT_METHOD_FOR_CLIENT_ACCOUNT(118, "Не удалось найти платежный метод подходящий для указанного клиентского аккаунта."),
    RECEIPT_NOT_FOUND(119, "Отсутствует чек для ордера."),
    UNREADABLE_RECEIPT(122, "Загруженный мерчантом чек не читаем."),
    RECEIPT_MISMATCH(123, "Загруженный мерчантом чек не соответствует ордеру."),
    RECEIPT_ALREADY_UPLOADED(124, "Такой чек уже загружен в систему."),
    ORDER_PRIORITIZATION_FORBIDDEN(130, "Приоритезация ордера запрещена."),
    ACTIVE_PRIORITIZED_ORDERS_LIMIT_REACHED(131, "Достигнут лимит активных приоритетных ордеров."),
    ORDER_ALREADY_PRIORITIZED(132, "Ордер уже приоритезирован."),
    CLIENT_ACCOUNT_BLACKLISTED(134, "Аккаунт клиента находится в черном списке."),
    PAYMENT_METHOD_NOT_DETERMINED_BY_NSPK_CODE(135, "Не удалось определить платежный метод по коду НСПК."),
    PAYMENT_METHOD_NOT_AVAILABLE_FOR_BUY_ORDERS(136, "Указанный платежный метод недоступен для Buy ордеров."),
    PAYMENT_METHOD_NOT_AVAILABLE_FOR_SELL_ORDERS(137, "Указанный платежный метод недоступен для Sell ордеров."),
    INVALID_FILE_ID(201, "Неверный идентификатор прикрепленного файла."),
    TICKET_NOT_FOUND_BY_ORDER_ID(202, "Не удается найти тикет по идентификатору ордера."),
    INVALID_TICKET_ID(203, "Неверный идентификатор тикета."),
    TICKET_NOT_FOUND_BY_TRANSACTION_ID(204, "Не удается найти тикет по идентификатору транзакции."),
    INVALID_TICKET_STATUS_FOR_OPERATION(205, "Недопустимый статус тикета для вызываемой операции."),
    DEPOSIT_ADDRESS_NOT_SET(301, "Не установлен адрес для депозитов мерчанта."),
    INVALID_TRANSACTION_ID(302, "Неверный идентификатор транзакции."),
    INVALID_WITHDRAWAL_PARAMETERS(303, "Недопустимые параметры для запроса на вывод денежных средств со счета."),
    APPEAL_NOT_FOUND_BY_ID(501, "Не удается найти апелляцию по ID апелляции."),
    APPEAL_NOT_FOUND_BY_ORDER_ID_OR_EXTERNAL_ID(502, "Не удается найти апелляцию по Id или External Id ордера."),
    APPEAL_ALREADY_EXISTS(503, "Невозможно создать новую апелляцию, не завершенная апелляция по этому ордеру уже существует."),
    APPEAL_FORBIDDEN_FOR_ORDER(504, "Невозможно создать новую апелляцию по этому ордеру. Установлен запрет на этот ордер."),
    ATTACHED_FILE_NOT_FOUND(505, "Невозможно найти прикрепленный файл по указанному Id."),
    ORDER_TOO_OLD_FOR_APPEAL(506, "Ордер слишком старый для подачи апелляции."),
    TEMPORARY_INTERNAL_ERROR(996, "Внутренняя ошибка временного характера, попробуйте отправить запрос еще раз."),
    PRICE_FETCH_FAILED(997, "Не удалось получить актуальную цену во время выполнения операции."),
    FUNCTION_DISABLED(998, "Функция отключена."),
    REQUEST_VALIDATION_ERROR(999, "Ошибка валидации запроса. В запросе присутствуют некорректные значения.");

    final int code;

    final String description;

    public static ErrorCode fromCode(int code) {
        for (ErrorCode errorCode : ErrorCode.values()) {
            if (errorCode.code == code) {
                return errorCode;
            }
        }
        return null;
    }

    public static class Deserializer extends JsonDeserializer<ErrorCode> {
        @Override
        public ErrorCode deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
            return ErrorCode.fromCode(Integer.parseInt(jsonParser.getValueAsString()));
        }
    }
}
