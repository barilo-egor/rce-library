package tgb.btc.library.interfaces.service.bean.web;

import tgb.btc.library.bean.web.api.ApiRequisite;
import tgb.btc.library.interfaces.service.IBasePersistService;

public interface IApiRequisiteService extends IBasePersistService<ApiRequisite> {
    ApiRequisite save(Long paymentTypePid, String requisite);

    ApiRequisite update(Long paymentRequisitePid, String requisite, Boolean isOn);
}
