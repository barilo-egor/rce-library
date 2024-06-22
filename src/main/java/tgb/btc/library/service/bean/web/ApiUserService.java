package tgb.btc.library.service.bean.web;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tgb.btc.library.bean.web.api.ApiDeal;
import tgb.btc.library.bean.web.api.ApiUser;
import tgb.btc.library.repository.web.ApiDealRepository;
import tgb.btc.library.repository.web.ApiUserRepository;

import java.util.List;
import java.util.Objects;

@Service
public class ApiUserService {

    private ApiUserRepository apiUserRepository;

    private ApiDealRepository apiDealRepository;

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
}
