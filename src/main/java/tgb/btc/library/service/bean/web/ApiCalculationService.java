package tgb.btc.library.service.bean.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tgb.btc.library.bean.web.api.ApiCalculation;
import tgb.btc.library.bean.web.api.ApiUser;
import tgb.btc.library.interfaces.service.bean.web.IApiCalculationService;
import tgb.btc.library.repository.BaseRepository;
import tgb.btc.library.repository.web.ApiCalculationRepository;
import tgb.btc.library.service.bean.BasePersistService;

import java.util.List;

@Service
@Transactional
public class ApiCalculationService extends BasePersistService<ApiCalculation> implements IApiCalculationService {

    private ApiCalculationRepository apiCalculationRepository;

    @Autowired
    public void setApiCalculationRepository(ApiCalculationRepository apiCalculationRepository) {
        this.apiCalculationRepository = apiCalculationRepository;
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

    @Override
    protected BaseRepository<ApiCalculation> getBaseRepository() {
        return apiCalculationRepository;
    }

}
