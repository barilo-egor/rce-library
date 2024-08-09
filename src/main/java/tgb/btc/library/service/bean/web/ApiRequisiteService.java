package tgb.btc.library.service.bean.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tgb.btc.library.bean.web.api.ApiRequisite;
import tgb.btc.library.interfaces.service.bean.web.IApiRequisiteService;
import tgb.btc.library.repository.BaseRepository;
import tgb.btc.library.repository.web.ApiRequisiteRepository;
import tgb.btc.library.service.bean.BasePersistService;

@Service
public class ApiRequisiteService extends BasePersistService<ApiRequisite> implements IApiRequisiteService {

    private ApiRequisiteRepository apiRequisiteRepository;

    @Autowired
    public void setApiRequisiteRepository(ApiRequisiteRepository apiRequisiteRepository) {
        this.apiRequisiteRepository = apiRequisiteRepository;
    }

    @Override
    protected BaseRepository<ApiRequisite> getBaseRepository() {
        return apiRequisiteRepository;
    }
}
