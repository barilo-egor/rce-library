package tgb.btc.library.repository.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import tgb.btc.library.bean.web.api.ApiCalculation;
import tgb.btc.library.bean.web.api.ApiUser;
import tgb.btc.library.constants.enums.bot.FiatCurrency;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ActiveProfiles("test")
class ApiCalculationRepositoryTest {

    @Autowired
    private ApiUserRepository apiUserRepository;

    @Autowired
    private ApiCalculationRepository apiCalculationRepository;

    @Test
    void getByApiUserPid() {
        ApiUser apiUser1 = apiUserRepository.save(ApiUser.builder().fiatCurrency(FiatCurrency.RUB).build());
        assertEquals(new ArrayList<>(), apiCalculationRepository.getByApiUserPid(apiUser1.getPid()));
        Set<ApiCalculation> expected = new HashSet<>();
        for (int i = 0; i < 3; i++) {
            expected.add(apiCalculationRepository.save(ApiCalculation.builder().apiUser(apiUser1).build()));
        }
        for (int i = 0; i < 2; i++) {
            apiCalculationRepository.save(ApiCalculation.builder().build());
        }
        for (int i = 0; i < 5; i++) {
            apiCalculationRepository.save(ApiCalculation.builder()
                    .apiUser(
                            apiUserRepository.save(ApiUser.builder().fiatCurrency(FiatCurrency.BYN).build())
                    ).build());
        }
        assertEquals(expected, new HashSet<>(apiCalculationRepository.getByApiUserPid(apiUser1.getPid())));
        assertEquals(new ArrayList<>(), apiCalculationRepository.getByApiUserPid(Long.MAX_VALUE));
    }

    @Test
    void findAllByApiUser() {
        ApiUser apiUser1 = apiUserRepository.save(ApiUser.builder().fiatCurrency(FiatCurrency.RUB).build());
        assertEquals(new ArrayList<>(), apiCalculationRepository.getByApiUserPid(apiUser1.getPid()));
        Set<ApiCalculation> expected = new HashSet<>();
        for (int i = 0; i < 4; i++) {
            expected.add(apiCalculationRepository.save(ApiCalculation.builder().apiUser(apiUser1).build()));
        }
        for (int i = 0; i < 5; i++) {
            apiCalculationRepository.save(ApiCalculation.builder().build());
        }
        for (int i = 0; i < 2; i++) {
            apiCalculationRepository.save(ApiCalculation.builder()
                    .apiUser(
                            apiUserRepository.save(ApiUser.builder().fiatCurrency(FiatCurrency.BYN).build())
                    ).build());
        }
        assertEquals(expected, new HashSet<>(apiCalculationRepository.findAllByApiUser(apiUser1)));
        assertEquals(new ArrayList<>(), apiCalculationRepository.getByApiUserPid(Long.MAX_VALUE));
    }

    @Test
    void countAllByApiUser() {
        ApiUser apiUser1 = apiUserRepository.save(ApiUser.builder().fiatCurrency(FiatCurrency.RUB).build());
        assertEquals(new ArrayList<>(), apiCalculationRepository.getByApiUserPid(apiUser1.getPid()));
        Set<ApiCalculation> expected = new HashSet<>();
        for (int i = 0; i < 4; i++) {
            expected.add(apiCalculationRepository.save(ApiCalculation.builder().apiUser(apiUser1).build()));
        }
        for (int i = 0; i < 5; i++) {
            apiCalculationRepository.save(ApiCalculation.builder().build());
        }
        for (int i = 0; i < 2; i++) {
            apiCalculationRepository.save(ApiCalculation.builder()
                    .apiUser(
                            apiUserRepository.save(ApiUser.builder().fiatCurrency(FiatCurrency.BYN).build())
                    ).build());
        }
        assertEquals(expected.size(), apiCalculationRepository.countAllByApiUser(apiUser1));
        assertEquals(0, apiCalculationRepository.countAllByApiUser(
                apiUserRepository.save(ApiUser.builder().fiatCurrency(FiatCurrency.RUB).build())
        ));
    }
}