package tgb.btc.library.repository.web;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tgb.btc.library.bean.web.api.ApiCalculation;
import tgb.btc.library.bean.web.api.ApiUser;
import tgb.btc.library.repository.BaseRepository;

import java.util.List;

@Repository
public interface ApiCalculationRepository extends BaseRepository<ApiCalculation> {

    @Query("from ApiCalculation where apiUser.pid=:pid")
    List<ApiCalculation> getByApiUserPid(Long pid);

    List<ApiCalculation> findAllByApiUser(ApiUser apiUser);

    Long countAllByApiUser(ApiUser apiUser);
}
