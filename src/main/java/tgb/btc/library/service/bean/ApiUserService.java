package tgb.btc.library.service.bean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tgb.btc.library.repository.web.ApiUserRepository;
import tgb.btc.library.repository.web.UsdApiUserCourseRepository;

@Service
public class ApiUserService {

    private ApiUserRepository apiUserRepository;

    private UsdApiUserCourseRepository usdApiUserCourseRepository;

    @Autowired
    public void setUsdApiUserCourseRepository(UsdApiUserCourseRepository usdApiUserCourseRepository) {
        this.usdApiUserCourseRepository = usdApiUserCourseRepository;
    }

    @Autowired
    public void setApiUserRepository(ApiUserRepository apiUserRepository) {
        this.apiUserRepository = apiUserRepository;
    }

    public boolean isExistsById(String id) {
        return apiUserRepository.countById(id) > 0;
    }
}
