package tgb.btc.library.vo.web;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tgb.btc.library.constants.enums.bot.DeliveryType;
import tgb.btc.library.constants.serialize.LocalDateTimeDeserializer;
import tgb.btc.library.constants.serialize.LocalDateTimeSerializer;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PoolDeal {

    /**
     * Идентификатор сделки в пуле
     */
    private Long id;

    /**
     * Идентификатор бота
     */
    private String bot;

    /**
     * Номер заявки в боте
     */
    private Long pid;

    /**
     * Адрес отправки криптовалюты
     */
    private String address;

    /**
     * Сумма отправки криптовалюты
     */
    private String amount;

    /**
     * Дата и время добавления сделки в пул
     */
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime addDate;

    /**
     * Тип доставки
     */
    private DeliveryType deliveryType;

}
