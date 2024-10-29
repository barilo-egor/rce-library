package tgb.btc.library.interfaces.service.bean.web;

import tgb.btc.library.bean.web.api.ApiCalculation;
import tgb.btc.library.bean.web.api.ApiUser;
import tgb.btc.library.interfaces.service.IBasePersistService;

import java.util.List;

public interface IApiCalculationService extends IBasePersistService<ApiCalculation> {

    List<ApiCalculation> getByApiUserPid(Long pid);

    List<ApiCalculation> findAllByApiUser(ApiUser apiUser);

    Long countAllByApiUser(ApiUser apiUser);
}
