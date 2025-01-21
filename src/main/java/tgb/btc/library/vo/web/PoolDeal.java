package tgb.btc.library.vo.web;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tgb.btc.library.constants.enums.bot.DeliveryType;

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
    private LocalDateTime addDate;

    /**
     * Тип доставки
     */
    private DeliveryType deliveryType;

}
