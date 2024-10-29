package tgb.btc.library.service.bean.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tgb.btc.library.bean.web.api.UsdApiUserCourse;
import tgb.btc.library.interfaces.service.bean.web.IUsdApiUserCourseService;
import tgb.btc.library.repository.BaseRepository;
import tgb.btc.library.repository.web.UsdApiUserCourseRepository;
import tgb.btc.library.service.bean.BasePersistService;

@Service
@Transactional
public class UsdApiUserCourseService extends BasePersistService<UsdApiUserCourse> implements IUsdApiUserCourseService {

    private UsdApiUserCourseRepository usdApiUserCourseRepository;

    @Autowired
    public void setUsdApiUserCourseRepository(
            UsdApiUserCourseRepository usdApiUserCourseRepository) {
        this.usdApiUserCourseRepository = usdApiUserCourseRepository;
    }

    @Override
    protected BaseRepository<UsdApiUserCourse> getBaseRepository() {
        return usdApiUserCourseRepository;
    }

}
