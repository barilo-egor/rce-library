package tgb.btc.library.service.bean.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import tgb.btc.library.bean.web.api.ApiPaymentType;
import tgb.btc.library.constants.enums.bot.DealType;
import tgb.btc.library.interfaces.service.bean.web.IApiPaymentTypeService;
import tgb.btc.library.repository.BaseRepository;
import tgb.btc.library.repository.web.ApiPaymentTypeRepository;
import tgb.btc.library.service.bean.BasePersistService;

import java.util.List;

@Service
public class ApiPaymentTypeService extends BasePersistService<ApiPaymentType> implements IApiPaymentTypeService {

    private ApiPaymentTypeRepository apiPaymentTypeRepository;

    @Autowired
    public void setApiPaymentTypeRepository(ApiPaymentTypeRepository apiPaymentTypeRepository) {
        this.apiPaymentTypeRepository = apiPaymentTypeRepository;
    }

    @Override
    protected BaseRepository<ApiPaymentType> getBaseRepository() {
        return apiPaymentTypeRepository;
    }

    public List<ApiPaymentType> findAll(DealType dealType) {
        return apiPaymentTypeRepository.findAll(Example.of(ApiPaymentType.builder().dealType(dealType).build()));
    }
}
