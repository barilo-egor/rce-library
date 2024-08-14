package tgb.btc.library.interfaces.service.bean.web;

import tgb.btc.library.bean.web.api.ApiPaymentType;
import tgb.btc.library.constants.enums.bot.DealType;
import tgb.btc.library.interfaces.service.IBasePersistService;

import java.math.BigDecimal;
import java.util.List;

public interface IApiPaymentTypeService extends IBasePersistService<ApiPaymentType> {
    List<ApiPaymentType> findAll(DealType dealType, String apiUserId);

    ApiPaymentType update(Long pid, String name, String id, String comment, BigDecimal minSum);

    void delete(Long pid);

    boolean exists(String id);
}
