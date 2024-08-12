package tgb.btc.library.interfaces.service.bean.web;

import tgb.btc.library.bean.web.api.ApiPaymentType;
import tgb.btc.library.constants.enums.bot.DealType;
import tgb.btc.library.interfaces.service.IBasePersistService;

import java.util.List;

public interface IApiPaymentTypeService extends IBasePersistService<ApiPaymentType> {
    List<ApiPaymentType> findAll(DealType dealType);

    ApiPaymentType update(Long pid, String name, String id, String comment);
}
