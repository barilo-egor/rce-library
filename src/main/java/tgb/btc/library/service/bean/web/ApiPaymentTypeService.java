package tgb.btc.library.service.bean.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import tgb.btc.library.bean.web.api.ApiPaymentType;
import tgb.btc.library.bean.web.api.ApiRequisite;
import tgb.btc.library.constants.enums.bot.DealType;
import tgb.btc.library.interfaces.service.bean.web.IApiPaymentTypeService;
import tgb.btc.library.interfaces.service.bean.web.IApiRequisiteService;
import tgb.btc.library.interfaces.service.bean.web.IApiUserService;
import tgb.btc.library.repository.BaseRepository;
import tgb.btc.library.repository.web.ApiPaymentTypeRepository;
import tgb.btc.library.service.bean.BasePersistService;

import java.util.List;

@Service
public class ApiPaymentTypeService extends BasePersistService<ApiPaymentType> implements IApiPaymentTypeService {

    private final ApiPaymentTypeRepository apiPaymentTypeRepository;

    private final IApiUserService apiUserService;

    private final IApiRequisiteService apiRequisiteService;

    @Autowired
    public ApiPaymentTypeService(ApiPaymentTypeRepository apiPaymentTypeRepository, IApiUserService apiUserService,
                                 IApiRequisiteService apiRequisiteService) {
        this.apiPaymentTypeRepository = apiPaymentTypeRepository;
        this.apiUserService = apiUserService;
        this.apiRequisiteService = apiRequisiteService;
    }

    @Override
    protected BaseRepository<ApiPaymentType> getBaseRepository() {
        return apiPaymentTypeRepository;
    }

    @Override
    public List<ApiPaymentType> findAll(DealType dealType) {
        return apiPaymentTypeRepository.findAll(Example.of(ApiPaymentType.builder().dealType(dealType).build()));
    }

    @Override
    public ApiPaymentType update(Long pid, String name, String id, String comment) {
        ApiPaymentType apiPaymentType = apiPaymentTypeRepository.getById(pid);
        apiPaymentType.setId(id);
        apiPaymentType.setName(name);
        apiPaymentType.setComment(comment);
        return apiPaymentTypeRepository.save(apiPaymentType);
    }

    @Override
    public void delete(Long pid) {
        ApiPaymentType apiPaymentType = apiPaymentTypeRepository.getById(pid);
        List<String> apiUsersIds = apiUserService.getIdByPaymentTypePid(apiPaymentType.getPid());
        for (String apiUserId : apiUsersIds) {
            apiUserService.deletePaymentType(apiUserId, apiPaymentType.getPid());
        }
        List<ApiRequisite> apiRequisites = apiRequisiteService.findAll(Example.of(
                ApiRequisite.builder().apiPaymentType(apiPaymentType).build())
        );
        for (ApiRequisite apiRequisite : apiRequisites) {
            apiRequisiteService.delete(apiRequisite);
        }
        apiPaymentTypeRepository.deleteById(pid);
    }
}
