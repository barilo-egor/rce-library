package tgb.btc.library.service.bean.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tgb.btc.library.bean.web.api.ApiPaymentType;
import tgb.btc.library.bean.web.api.ApiRequisite;
import tgb.btc.library.exception.BaseException;
import tgb.btc.library.interfaces.service.bean.web.IApiPaymentTypeService;
import tgb.btc.library.interfaces.service.bean.web.IApiRequisiteService;
import tgb.btc.library.repository.BaseRepository;
import tgb.btc.library.repository.web.ApiRequisiteRepository;
import tgb.btc.library.service.bean.BasePersistService;

import java.util.Objects;

@Service
public class ApiRequisiteService extends BasePersistService<ApiRequisite> implements IApiRequisiteService {

    private ApiRequisiteRepository apiRequisiteRepository;

    private IApiPaymentTypeService apiPaymentTypeService;

    @Autowired
    public ApiRequisiteService(ApiRequisiteRepository apiRequisiteRepository,
                               IApiPaymentTypeService apiPaymentTypeService) {
        this.apiRequisiteRepository = apiRequisiteRepository;
        this.apiPaymentTypeService = apiPaymentTypeService;
    }

    @Override
    protected BaseRepository<ApiRequisite> getBaseRepository() {
        return apiRequisiteRepository;
    }

    @Override
    public ApiRequisite save(Long paymentTypePid, String requisite) {
        ApiPaymentType apiPaymentType = apiPaymentTypeService.findById(paymentTypePid);
        if (Objects.isNull(apiPaymentType)) {
            throw new BaseException("Апи тип оплаты pid=" + paymentTypePid + " не найден.");
        }
        return apiRequisiteRepository.save(ApiRequisite
                .builder()
                .apiPaymentType(apiPaymentType)
                .requisite(requisite)
                .isOn(false)
                .build());
    }
}
