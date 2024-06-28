package tgb.btc.library.interfaces.service.bean.bot.deal.read;

import tgb.btc.library.constants.enums.bot.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface IDealPropertyService {

    CryptoCurrency getCryptoCurrencyByPid(Long pid);

    BigDecimal getCommissionByPid(Long pid);

    BigDecimal getAmountByPid(Long pid);

    BigDecimal getCryptoAmountByPid(Long pid);

    BigDecimal getDiscountByPid(Long pid);

    DealType getDealTypeByPid(Long pid);

    LocalDateTime getDateTimeByPid(Long pid);

    FiatCurrency getFiatCurrencyByPid(Long pid);

    String getAdditionalVerificationImageIdByPid(Long pid);

    DeliveryType getDeliveryTypeByPid(Long pid);

    BigDecimal getCreditedAmountByPid(Long pid);

    DealStatus getDealStatusByPid(Long pid);
}
