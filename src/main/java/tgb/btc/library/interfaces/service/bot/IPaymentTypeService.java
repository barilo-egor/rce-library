package tgb.btc.library.interfaces.service.bot;

import org.springframework.data.repository.query.Param;
import tgb.btc.library.bean.bot.PaymentType;
import tgb.btc.library.constants.enums.bot.DealType;
import tgb.btc.library.constants.enums.bot.FiatCurrency;

import java.math.BigDecimal;
import java.util.List;

public interface IPaymentTypeService {

    /** SELECT **/

    PaymentType getByPid(@Param("pid") Long pid);

    List<PaymentType> getByDealType(DealType dealType);

    List<PaymentType> getByDealTypeAndFiatCurrency(DealType dealType,  FiatCurrency fiatCurrency);

    long countByDealTypeAndFiatCurrency(DealType dealType,  FiatCurrency fiatCurrency);

    List<PaymentType> getByDealTypeAndIsOnAndFiatCurrency(DealType dealType, Boolean isOn, FiatCurrency fiatCurrency);

    Integer countByDealTypeAndIsOnAndFiatCurrency(DealType dealType, Boolean isOn, FiatCurrency fiatCurrency);

    DealType getDealTypeByPid(Long pid);

    long countByNameLike(String name);


    /** UPDATE **/

    void updateIsOnByPid(Boolean isOn, Long pid);

    void updateMinSumByPid(BigDecimal minSum, Long pid);

    void updateIsDynamicOnByPid(Boolean isDynamicOn, Long pid);
}