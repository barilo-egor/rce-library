package tgb.btc.library.repository.web;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import tgb.btc.library.bean.web.api.ApiCalculation;
import tgb.btc.library.bean.web.api.ApiUser;

import java.util.List;

@Repository
public interface ApiCalculationRepository extends PagingAndSortingRepository<ApiCalculation, Long> {

    @Query("from ApiCalculation where apiUser.pid=:pid")
    List<ApiCalculation> getByApiUserPid(Long pid);

    List<ApiCalculation> findAllByApiUser(ApiUser apiUser, Pageable pageable);

    Long countAllByApiUser(ApiUser apiUser);
}
