package tgb.btc.library.service.bean.web;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tgb.btc.library.bean.web.api.ApiPaymentType;
import tgb.btc.library.bean.web.api.ApiRequisite;
import tgb.btc.library.exception.BaseException;
import tgb.btc.library.interfaces.service.bean.web.IApiDealService;
import tgb.btc.library.interfaces.service.bean.web.IApiPaymentTypeService;
import tgb.btc.library.interfaces.service.bean.web.IApiRequisiteService;
import tgb.btc.library.repository.BaseRepository;
import tgb.btc.library.repository.web.ApiRequisiteRepository;
import tgb.btc.library.service.bean.BasePersistService;

import java.util.Objects;

@Service
@Transactional
public class ApiRequisiteService extends BasePersistService<ApiRequisite> implements IApiRequisiteService {

    private final ApiRequisiteRepository apiRequisiteRepository;

    private final IApiPaymentTypeService apiPaymentTypeService;

    private final IApiDealService apiDealService;

    @Autowired
    public ApiRequisiteService(ApiRequisiteRepository apiRequisiteRepository,
                               IApiPaymentTypeService apiPaymentTypeService, IApiDealService apiDealService) {
        this.apiRequisiteRepository = apiRequisiteRepository;
        this.apiPaymentTypeService = apiPaymentTypeService;
        this.apiDealService = apiDealService;
    }

    @Override
    protected BaseRepository<ApiRequisite> getBaseRepository() {
        return apiRequisiteRepository;
    }

    @Override
    public ApiRequisite save(Long paymentTypePid, String requisite, String comment) {
        ApiPaymentType apiPaymentType = apiPaymentTypeService.findById(paymentTypePid);
        if (Objects.isNull(apiPaymentType)) {
            throw new BaseException("Апи тип оплаты pid=" + paymentTypePid + " не найден.");
        }
        return apiRequisiteRepository.save(ApiRequisite
                .builder()
                .apiPaymentType(apiPaymentType)
                .requisite(requisite)
                .comment(comment)
                .isOn(false)
                .build());
    }

    @Override
    public ApiRequisite update(Long paymentRequisitePid, String requisite, String comment, Boolean isOn) {
        ApiRequisite apiRequisite = apiRequisiteRepository.findById(paymentRequisitePid)
                .orElseThrow(() -> new BaseException("Апи реквизит pid=" + paymentRequisitePid + " не найден."));
        if (StringUtils.isNotEmpty(requisite)) {
            apiRequisite.setRequisite(requisite);
        }
        if (!Objects.isNull(isOn)) {
            apiRequisite.setIsOn(isOn);
        }
        apiRequisite.setComment(comment);
        return save(apiRequisite);
    }

    @Override
    public void delete(Long pid) {
        apiDealService.dropApiRequisite(pid);
        apiRequisiteRepository.deleteById(pid);
    }
}
