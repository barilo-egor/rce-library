package tgb.btc.library.service.bean.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tgb.btc.library.bean.web.api.ApiCalculation;
import tgb.btc.library.bean.web.api.ApiUser;
import tgb.btc.library.interfaces.service.web.IApiCalculationService;
import tgb.btc.library.repository.BaseRepository;
import tgb.btc.library.repository.web.ApiCalculationRepository;
import tgb.btc.library.service.bean.BasePersistService;

import java.util.List;

@Service
public class ApiCalculationService extends BasePersistService<ApiCalculation> implements IApiCalculationService {

    private ApiCalculationRepository apiCalculationRepository;

    @Autowired
    public void setApiCalculationRepository(ApiCalculationRepository apiCalculationRepository) {
        this.apiCalculationRepository = apiCalculationRepository;
    }

    @Autowired
    public ApiCalculationService(BaseRepository<ApiCalculation> baseRepository) {
        super(baseRepository);
    }

    @Override
    public List<ApiCalculation> getByApiUserPid(Long pid) {
        return apiCalculationRepository.getByApiUserPid(pid);
    }

    @Override
    public List<ApiCalculation> findAllByApiUser(ApiUser apiUser) {
        return apiCalculationRepository.findAllByApiUser(apiUser);
    }

    @Override
    public Long countAllByApiUser(ApiUser apiUser) {
        return apiCalculationRepository.countAllByApiUser(apiUser);
    }

}
