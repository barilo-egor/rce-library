package tgb.btc.library.service.bean.web;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tgb.btc.library.bean.web.WebUser;
import tgb.btc.library.bean.web.api.ApiDeal;
import tgb.btc.library.bean.web.api.ApiUser;
import tgb.btc.library.interfaces.service.web.IApiUserService;
import tgb.btc.library.repository.BaseRepository;
import tgb.btc.library.repository.web.ApiDealRepository;
import tgb.btc.library.repository.web.ApiUserRepository;
import tgb.btc.library.service.bean.BasePersistService;

import java.util.List;
import java.util.Objects;

@Service
public class ApiUserService extends BasePersistService<ApiUser> implements IApiUserService {

    private ApiUserRepository apiUserRepository;

    private ApiDealRepository apiDealRepository;

    @Autowired
    public ApiUserService(BaseRepository<ApiUser> baseRepository) {
        super(baseRepository);
    }

    @Autowired
    public void setApiDealRepository(ApiDealRepository apiDealRepository) {
        this.apiDealRepository = apiDealRepository;
    }

    @Autowired
    public void setApiUserRepository(ApiUserRepository apiUserRepository) {
        this.apiUserRepository = apiUserRepository;
    }

    public boolean isExistsById(String id) {
        return apiUserRepository.countById(id) > 0;
    }

    @Transactional
    public void delete(String deleteUserId, String newUserId) {
        ApiUser apiUser = null;
        if (Objects.nonNull(newUserId)) {
            apiUser = apiUserRepository.getById(newUserId);
        }
        List<ApiDeal> deals = apiDealRepository.getByApiUserId(deleteUserId);
        ApiUser finalApiUser = apiUser;
        deals.forEach(deal -> {
            deal.setApiUser(finalApiUser);
        });
        apiDealRepository.saveAllAndFlush(deals);
        if (Objects.isNull(newUserId)) apiDealRepository.deleteAll(deals);
        apiUserRepository.deleteById(deleteUserId);
    }

    @Transactional
    public String generateToken(String username) {
        ApiUser apiUser = apiUserRepository.getByUsername(username);
        String token = RandomStringUtils.randomAlphanumeric(42);
        while (apiUserRepository.countByToken(token) > 0) {
            token = RandomStringUtils.randomAlphanumeric(42);
        }
        apiUser.setToken(token);
        apiUserRepository.save(apiUser);
        return token;
    }

    @Override
    public long countByToken(String token) {
        return apiUserRepository.countByToken(token);
    }

    @Override
    public ApiUser getByToken(String token) {
        return apiUserRepository.getByToken(token);
    }

    @Override
    public long countById(String id) {
        return apiUserRepository.countById(id);
    }

    @Override
    public Boolean isBanned(Long pid) {
        return apiUserRepository.isBanned(pid);
    }

    @Override
    public Long getPidByToken(String token) {
        return apiUserRepository.getPidByToken(token);
    }

    @Override
    public ApiUser getById(String id) {
        return apiUserRepository.getById(id);
    }

    @Override
    public Long getLastPaidDealPidByUserPid(Long pid) {
        return apiUserRepository.getLastPaidDealPidByUserPid(pid);
    }

    @Override
    public ApiDeal getLastPaidDeal(Long userPid) {
        return apiUserRepository.getLastPaidDeal(userPid);
    }

    @Override
    public Long getPidByUsername(String username) {
        return apiUserRepository.getPidByUsername(username);
    }

    @Override
    public ApiUser getByUsername(String username) {
        return apiUserRepository.getByUsername(username);
    }

    @Override
    public WebUser getWebUser(Long pid) {
        return apiUserRepository.getWebUser(pid);
    }

    @Override
    public void deleteById(String id) {
        apiUserRepository.deleteById(id);
    }

    @Override
    public void updateLastPidDeal(Long userPid, ApiDeal lastPaidDeal) {
        apiUserRepository.updateLastPidDeal(userPid, lastPaidDeal);
    }

    @Override
    public void updateWebUser(Long pid, WebUser webUser) {
        apiUserRepository.updateWebUser(pid, webUser);
    }

}
