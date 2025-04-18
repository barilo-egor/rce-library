package tgb.btc.library.bean.bot;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import tgb.btc.library.bean.BasePersist;
import tgb.btc.library.constants.enums.bot.CryptoCurrency;
import tgb.btc.library.constants.enums.bot.DeliveryType;
import tgb.btc.library.constants.enums.web.merchant.AutoConfirmType;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AutoConfirmConfig extends BasePersist {

    @Enumerated(EnumType.STRING)
    private CryptoCurrency cryptoCurrency;

    @Enumerated(EnumType.STRING)
    private AutoConfirmType autoConfirmType;

    @Enumerated(EnumType.STRING)
    private DeliveryType deliveryType;
}
