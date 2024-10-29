package tgb.btc.library.service.bean.web;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tgb.btc.library.bean.web.api.ApiPaymentType;
import tgb.btc.library.bean.web.api.ApiRequisite;
import tgb.btc.library.bean.web.api.ApiUser;
import tgb.btc.library.constants.enums.bot.DealType;
import tgb.btc.library.interfaces.service.bean.web.IApiDealService;
import tgb.btc.library.interfaces.service.bean.web.IApiPaymentTypeService;
import tgb.btc.library.interfaces.service.bean.web.IApiUserService;
import tgb.btc.library.repository.BaseRepository;
import tgb.btc.library.repository.web.ApiPaymentTypeRepository;
import tgb.btc.library.repository.web.ApiRequisiteRepository;
import tgb.btc.library.service.bean.BasePersistService;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class ApiPaymentTypeService extends BasePersistService<ApiPaymentType> implements IApiPaymentTypeService {

    private final ApiPaymentTypeRepository apiPaymentTypeRepository;

    private final IApiUserService apiUserService;

    private final ApiRequisiteRepository apiRequisiteRepository;

    private final IApiDealService apiDealService;

    @Autowired
    public ApiPaymentTypeService(ApiPaymentTypeRepository apiPaymentTypeRepository, IApiUserService apiUserService,
                                 ApiRequisiteRepository apiRequisiteRepository, IApiDealService apiDealService) {
        this.apiPaymentTypeRepository = apiPaymentTypeRepository;
        this.apiUserService = apiUserService;
        this.apiRequisiteRepository = apiRequisiteRepository;
        this.apiDealService = apiDealService;
    }

    @Override
    protected BaseRepository<ApiPaymentType> getBaseRepository() {
        return apiPaymentTypeRepository;
    }

    @Override
    public List<ApiPaymentType> findAll(DealType dealType, String apiUserId) {
        if (StringUtils.isNotEmpty(apiUserId)) {
            ApiUser apiUser = apiUserService.getById(apiUserId);
            if (Objects.isNull(apiUser)) {
                return Collections.emptyList();
            }
            return apiUser.getPaymentTypes().stream()
                    .filter(apiPaymentType -> apiPaymentType.getDealType().equals(dealType))
                    .collect(Collectors.toList());
        }
        return apiPaymentTypeRepository.findAll(Example.of(ApiPaymentType.builder().dealType(dealType).build()));
    }

    @Override
    public ApiPaymentType update(Long pid, String name, String id, String comment, BigDecimal minSum) {
        ApiPaymentType apiPaymentType = apiPaymentTypeRepository.getById(pid);
        apiPaymentType.setId(id);
        apiPaymentType.setName(name);
        apiPaymentType.setComment(comment);
        apiPaymentType.setMinSum(minSum);
        return apiPaymentTypeRepository.save(apiPaymentType);
    }

    @Override
    public void delete(Long pid) {
        ApiPaymentType apiPaymentType = apiPaymentTypeRepository.getById(pid);
        apiDealService.dropApiPaymentType(pid);
        List<String> apiUsersIds = apiUserService.getIdByPaymentTypePid(apiPaymentType.getPid());
        for (String apiUserId : apiUsersIds) {
            apiUserService.deletePaymentType(apiUserId, apiPaymentType.getPid());
        }
        List<ApiRequisite> apiRequisites = apiRequisiteRepository.findAll(Example.of(
                ApiRequisite.builder().apiPaymentType(apiPaymentType).build())
        );
        for (ApiRequisite apiRequisite : apiRequisites) {
            apiDealService.dropApiRequisite(apiRequisite.getPid());
            apiRequisiteRepository.deleteById(apiRequisite.getPid());
        }
        apiPaymentTypeRepository.deleteById(pid);
    }

    @Override
    public boolean exists(String id) {
        return apiPaymentTypeRepository.exists(Example.of(ApiPaymentType.builder().id(id).build()));
    }

    @Override
    public Optional<ApiPaymentType> findById(String id) {
        return apiPaymentTypeRepository.findOne(Example.of(ApiPaymentType.builder().id(id).build()));
    }

    @Override
    public List<ApiPaymentType> getAvailable(ApiUser apiUser, DealType dealType) {
        List<ApiPaymentType> apiPaymentTypes = new ArrayList<>(apiUser.getPaymentTypes());
        if (Objects.nonNull(dealType)) {
            apiPaymentTypes.removeIf(apiPaymentType -> !apiPaymentType.getDealType().equals(dealType));
        }
        apiPaymentTypes.removeIf(apiPaymentType -> apiPaymentType.getDealType().equals(DealType.BUY) &&
                CollectionUtils.isEmpty(apiRequisiteRepository.findAll(
                        Example.of(ApiRequisite.builder().apiPaymentType(apiPaymentType).build())
                )
        ));
        return apiPaymentTypes;
    }
}
