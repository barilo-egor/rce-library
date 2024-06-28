package tgb.btc.library.service.bean.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tgb.btc.library.bean.web.api.UsdApiUserCourse;
import tgb.btc.library.interfaces.service.web.IUsdApiUserCourseService;
import tgb.btc.library.repository.BaseRepository;
import tgb.btc.library.service.bean.BasePersistService;

@Service
public class UsdApiUserCourseService extends BasePersistService<UsdApiUserCourse> implements IUsdApiUserCourseService {

    @Autowired
    public UsdApiUserCourseService(BaseRepository<UsdApiUserCourse> baseRepository) {
        super(baseRepository);
    }

}
