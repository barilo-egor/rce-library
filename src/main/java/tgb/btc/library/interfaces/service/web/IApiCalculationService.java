package tgb.btc.library.interfaces.service.web;

import tgb.btc.library.bean.web.api.ApiCalculation;
import tgb.btc.library.bean.web.api.ApiUser;

import java.util.List;

public interface IApiCalculationService {

    List<ApiCalculation> getByApiUserPid(Long pid);

    List<ApiCalculation> findAllByApiUser(ApiUser apiUser);

    Long countAllByApiUser(ApiUser apiUser);
}
