package tgb.btc.library.repository.web;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import tgb.btc.library.bean.BasePersist;
import tgb.btc.library.bean.web.api.ApiDeal;
import tgb.btc.library.bean.web.api.ApiUser;
import tgb.btc.library.constants.enums.bot.FiatCurrency;
import tgb.btc.library.constants.enums.web.ApiDealStatus;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class ApiDealRepositoryTest {

    @Autowired
    private ApiDealRepository apiDealRepository;

    @Autowired
    private ApiUserRepository apiUserRepository;

    @Test
    void getByPid() {
        ApiDeal apiDeal = apiDealRepository.save(ApiDeal.builder().build());
        apiDealRepository.save(ApiDeal.builder().build());
        apiDealRepository.save(ApiDeal.builder().build());
        assertNull(apiDealRepository.getByPid(Long.MAX_VALUE));
        assertEquals(apiDeal, apiDealRepository.getByPid(apiDeal.getPid()));
    }

    @Test
    void countByPid() {
        ApiDeal apiDeal = apiDealRepository.save(ApiDeal.builder().build());
        apiDealRepository.save(ApiDeal.builder().build());
        apiDealRepository.save(ApiDeal.builder().build());
        assertEquals(0, apiDealRepository.countByPid(Long.MAX_VALUE));
        assertEquals(1, apiDealRepository.countByPid(apiDeal.getPid()));
    }

    @Test
    void getApiDealStatusByPid() {
        ApiDeal apiDeal = apiDealRepository.save(ApiDeal.builder().apiDealStatus(ApiDealStatus.CREATED).build());
        apiDealRepository.save(ApiDeal.builder().apiDealStatus(ApiDealStatus.ACCEPTED).build());
        apiDealRepository.save(ApiDeal.builder().build());
        assertNull(apiDealRepository.getApiDealStatusByPid(Long.MAX_VALUE));
        assertEquals(ApiDealStatus.CREATED, apiDealRepository.getApiDealStatusByPid(apiDeal.getPid()));
    }

    @Test
    void countByApiDealStatusAndApiUser_Pid() {
        for (ApiDealStatus apiDealStatus : ApiDealStatus.values()) {
            assertEquals(0, apiDealRepository.countByApiDealStatusAndApiUser_Pid(apiDealStatus, Long.MAX_VALUE));
        }
        ApiUser apiUser = apiUserRepository.save(ApiUser.builder().fiatCurrency(FiatCurrency.RUB).build());
        int expected = 5;
        for (ApiDealStatus apiDealStatus : ApiDealStatus.values()) {
            for (int i = 0; i < expected; i++) {
                apiDealRepository.save(ApiDeal.builder().apiUser(apiUser).apiDealStatus(apiDealStatus).build());
            }
        }
        for (ApiDealStatus apiDealStatus : ApiDealStatus.values()) {
            for (int i = 0; i < 3; i++) {
                apiDealRepository.save(ApiDeal.builder().apiDealStatus(apiDealStatus).build());
            }
        }
        ApiUser notQueryUser = apiUserRepository.save(ApiUser.builder().fiatCurrency(FiatCurrency.BYN).build());
        for (ApiDealStatus apiDealStatus : ApiDealStatus.values()) {
            for (int i = 0; i < 3; i++) {
                apiDealRepository.save(ApiDeal.builder()
                        .apiDealStatus(apiDealStatus)
                        .apiUser(notQueryUser)
                        .build());
            }
        }
        for (ApiDealStatus apiDealStatus : ApiDealStatus.values()) {
            assertEquals(expected, apiDealRepository.countByApiDealStatusAndApiUser_Pid(apiDealStatus, apiUser.getPid()));
        }
    }

    @Test
    void getActiveDealsPids() {
        assertEquals(new ArrayList<>(), apiDealRepository.getActiveDealsPids());
        Set<Long> expected = new HashSet<>();
        for (ApiDealStatus apiDealStatus : ApiDealStatus.values()) {
            for (int i = 0; i < 3; i++) {
                ApiDeal apiDeal = apiDealRepository.save(ApiDeal.builder().apiDealStatus(apiDealStatus).build());
                if (ApiDealStatus.PAID.equals(apiDealStatus)) {
                    expected.add(apiDeal.getPid());
                }
            }
        }
        assertEquals(expected, new HashSet<>(apiDealRepository.getActiveDealsPids()));
    }

    @Test
    @DisplayName("Должно вернуть все сделки в статусе ACCEPTED.")
    void getByDateBetweenReturnAllInStatus() {
        int dealsCount = 10;
        Set<ApiDeal> expected = new HashSet<>();
        for (int i = 0; i < dealsCount; i++) {
            ApiDeal apiDeal = new ApiDeal();
            apiDeal.setDateTime(LocalDateTime.of(2000, 1, i + 1, 0, 1));
            apiDeal.setApiDealStatus(ApiDealStatus.ACCEPTED);
            expected.add(apiDealRepository.save(apiDeal));
        }
        apiDealRepository.save(ApiDeal.builder()
                .dateTime(LocalDateTime.of(2000, 1, 2, 0, 1))
                .apiDealStatus(ApiDealStatus.PAID)
                .build());
        assertAll(
                () -> assertEquals(expected, new HashSet<>(
                        apiDealRepository.getByDateBetween(
                                LocalDateTime.of(1999, 1, 1, 0, 1),
                                LocalDateTime.of(2001, 1, 1, 0, 1),
                                ApiDealStatus.ACCEPTED)
                )),
                () -> assertEquals(expected, new HashSet<>(
                        apiDealRepository.getByDateBetween(
                                LocalDateTime.of(2000, 1, 1, 0, 1),
                                LocalDateTime.of(2000, 1, dealsCount, 0, 1),
                                ApiDealStatus.ACCEPTED)
                ))
        );
    }

    @Test
    @DisplayName("Должен вернуть по диапазону дат с пустыми датами за нижней границей.")
    void getByDateBetweenBottomBound() {
        int dealsCount = 10;
        Set<ApiDeal> expected = new HashSet<>();
        for (int i = 0; i < dealsCount; i++) {
            ApiDeal apiDeal = new ApiDeal();
            apiDeal.setDateTime(LocalDateTime.of(2000, 1, i + 1, 0, 1));
            apiDeal.setApiDealStatus(ApiDealStatus.ACCEPTED);
            if (i + 1 != dealsCount) expected.add(apiDealRepository.save(apiDeal));
        }
        apiDealRepository.save(ApiDeal.builder()
                .dateTime(LocalDateTime.of(2000, 1, 2, 0, 1))
                .apiDealStatus(ApiDealStatus.PAID)
                .build());
        assertAll(
                () -> assertEquals(expected, new HashSet<>(
                        apiDealRepository.getByDateBetween(
                                LocalDateTime.of(1999, 1, 1, 0, 1),
                                LocalDateTime.of(2000, 1, dealsCount - 1, 0, 1),
                                ApiDealStatus.ACCEPTED)
                )),
                () -> assertEquals(expected, new HashSet<>(
                        apiDealRepository.getByDateBetween(
                                LocalDateTime.of(2000, 1, 1, 0, 1),
                                LocalDateTime.of(2000, 1, dealsCount - 1, 0, 1),
                                ApiDealStatus.ACCEPTED)
                ))
        );
    }

    @Test
    @DisplayName("Должен вернуть по диапазону дат с пустыми датами за верхней границей.")
    void getByDateBetweenTopBound() {
        int dealsCount = 10;
        Set<ApiDeal> expected = new HashSet<>();
        for (int i = 0; i < dealsCount; i++) {
            ApiDeal apiDeal = new ApiDeal();
            apiDeal.setDateTime(LocalDateTime.of(2000, 1, i + 1, 0, 1));
            apiDeal.setApiDealStatus(ApiDealStatus.ACCEPTED);
            if (i != 0) expected.add(apiDealRepository.save(apiDeal));
        }
        apiDealRepository.save(ApiDeal.builder()
                .dateTime(LocalDateTime.of(2000, 1, 2, 0, 1))
                .apiDealStatus(ApiDealStatus.PAID)
                .build());
        assertAll(
                () -> assertEquals(expected, new HashSet<>(
                        apiDealRepository.getByDateBetween(
                                LocalDateTime.of(2000, 1, 2, 0, 1),
                                LocalDateTime.of(2000 + dealsCount, 1, 1, 0, 1),
                                ApiDealStatus.ACCEPTED)
                )),
                () -> assertEquals(expected, new HashSet<>(
                        apiDealRepository.getByDateBetween(
                                LocalDateTime.of(2000, 1, 2, 0, 1),
                                LocalDateTime.of(2000 + dealsCount, 1, 1, 0, 1),
                                ApiDealStatus.ACCEPTED)
                ))
        );
    }

    @Test
    void getByDateBetweenInnerRange() {
        int dealsCount = 10;
        Set<ApiDeal> expected = new HashSet<>();
        for (int i = 0; i < dealsCount; i++) {
            ApiDeal apiDeal = new ApiDeal();
            apiDeal.setDateTime(LocalDateTime.of(2000, 1, i + 1, 0, 1));
            apiDeal.setApiDealStatus(ApiDealStatus.ACCEPTED);
            if (i != 0 && i + 1 != dealsCount) expected.add(apiDealRepository.save(apiDeal));
        }
        apiDealRepository.save(ApiDeal.builder()
                .dateTime(LocalDateTime.of(2000, 1, 2, 0, 1))
                .apiDealStatus(ApiDealStatus.PAID)
                .build());
        assertAll(
                () -> assertEquals(expected, new HashSet<>(
                        apiDealRepository.getByDateBetween(
                                LocalDateTime.of(2000, 1, 1, 23, 1),
                                LocalDateTime.of(2000, 1, dealsCount - 1, 23, 1),
                                ApiDealStatus.ACCEPTED)
                )),
                () -> assertEquals(expected, new HashSet<>(
                        apiDealRepository.getByDateBetween(
                                LocalDateTime.of(2000, 1, 2, 0, 1),
                                LocalDateTime.of(2000, 1, dealsCount - 1, 0, 1),
                                ApiDealStatus.ACCEPTED)
                ))
        );
    }

    @Test
    void getByDateBetweenOutOfRange() {
        int dealsCount = 10;
        for (int i = 0; i < dealsCount; i++) {
            ApiDeal apiDeal = new ApiDeal();
            apiDeal.setDateTime(LocalDateTime.of(2000, 1, i + 1, 0, 1));
            apiDeal.setApiDealStatus(ApiDealStatus.ACCEPTED);
            apiDealRepository.save(apiDeal);
        }
        apiDealRepository.save(ApiDeal.builder()
                .dateTime(LocalDateTime.of(2000, 1, 2, 0, 1))
                .apiDealStatus(ApiDealStatus.PAID)
                .build());
        assertAll(
                () -> assertEquals(new ArrayList<>(), apiDealRepository.getByDateBetween(
                        LocalDateTime.of(1900, 1, 1, 0, 1),
                        LocalDateTime.of(1901, 1, 1, 0, 1),
                        ApiDealStatus.ACCEPTED)
                ),
                () -> assertEquals(new ArrayList<>(),
                        apiDealRepository.getByDateBetween(
                                LocalDateTime.of(2100, 1, 1, 0, 1),
                                LocalDateTime.of(2101, 1, 1, 0, 1),
                                ApiDealStatus.ACCEPTED)
                )
        );
    }

    @Test
    void getByDateOneDay() {
        int dealsCount = 10;
        Set<ApiDeal> expected = new HashSet<>();
        for (int i = 0; i < dealsCount; i++) {
            ApiDeal apiDeal = new ApiDeal();
            apiDeal.setDateTime(LocalDateTime.of(2000, 1, 1, 0, 1));
            apiDeal.setApiDealStatus(ApiDealStatus.ACCEPTED);
            expected.add(apiDealRepository.save(apiDeal));
        }
        apiDealRepository.save(ApiDeal.builder()
                .dateTime(LocalDateTime.of(2000, 1, 2, 0, 1))
                .apiDealStatus(ApiDealStatus.ACCEPTED)
                .build());
        apiDealRepository.save(ApiDeal.builder()
                .dateTime(LocalDateTime.of(2000, 1, 1, 0, 1))
                .apiDealStatus(ApiDealStatus.PAID)
                .build());
        assertAll(
                () -> assertEquals(expected, new HashSet<>(
                        apiDealRepository.getByDateBetween(
                                LocalDateTime.of(2000, 1, 1, 0, 1),
                                LocalDateTime.of(2000, 1, 1, 0, 1),
                                ApiDealStatus.ACCEPTED))
                )
        );
    }

    @Test
    void getDealsByPids() {
        Set<ApiDeal> expected = new HashSet<>();
        for (int i = 0; i < 5; i++) {
            expected.add(apiDealRepository.save(ApiDeal.builder().build()));
        }
        assertEquals(expected, new HashSet<>(
                apiDealRepository.getDealsByPids(expected.stream().map(BasePersist::getPid).collect(Collectors.toList())
        )));
        assertEquals(new ArrayList<>(), apiDealRepository.getDealsByPids(List.of()));
        assertEquals(new ArrayList<>(), apiDealRepository.getDealsByPids(List.of(Long.MAX_VALUE)));
    }

    @Test
    void getByApiUserId() {
        ApiUser apiUser = new ApiUser();
        apiUser.setId("id");
        apiUser.setFiatCurrency(FiatCurrency.RUB);
        apiUserRepository.save(apiUser);
        assertEquals(new ArrayList<>(), apiDealRepository.getByApiUserId("id"));
        Set<ApiDeal> expected = new HashSet<>();
        for (int i = 0; i < 5; i++) {
            expected.add(apiDealRepository.save(ApiDeal.builder().apiUser(apiUser).build()));
        }
        apiDealRepository.save(ApiDeal.builder()
                        .apiUser(apiUserRepository.save(ApiUser.builder().id("id2").fiatCurrency(FiatCurrency.BYN).build()))
                .build());
        apiDealRepository.save(ApiDeal.builder().build());
        assertEquals(expected, new HashSet<>(
                apiDealRepository.getByApiUserId("id")
        ));
        assertEquals(new ArrayList<>(), apiDealRepository.getByApiUserId(""));
    }

    @Test
    void getDateTimeByPid() {
        ApiDeal apiDeal = new ApiDeal();
        LocalDateTime expected = LocalDateTime.of(2000, 1, 1, 0, 1);
        apiDealRepository.save(apiDeal);
        assertNull(apiDealRepository.getDateTimeByPid(apiDeal.getPid()));
        apiDeal.setDateTime(expected);
        apiDealRepository.save(apiDeal);
        assertEquals(expected, apiDealRepository.getDateTimeByPid(apiDeal.getPid()));
        assertNull(apiDealRepository.getDateTimeByPid(Long.MAX_VALUE));
    }

    @Test
    void getFirstDealPid() {
        Map<Long, Long> expected = new HashMap<>();
        int usersCount = 3;
        for (int i = 0; i < usersCount; i++) {
            ApiUser apiUser = new ApiUser();
            apiUser.setFiatCurrency(FiatCurrency.BYN);
            apiUserRepository.save(apiUser);
            assertNull(apiDealRepository.getFirstDealPid(apiUser.getPid()));
            for (int j = 5; j > 0; j--) {
                ApiDeal apiDeal = new ApiDeal();
                apiDeal.setApiUser(apiUser);
                LocalDateTime dateTime;
                if (j == 3) {
                    dateTime = LocalDateTime.of(2000, 1, 1, 0, 1);
                } else {
                    dateTime = LocalDateTime.of(2001, 1, j, 0, 1);
                }
                apiDeal.setDateTime(dateTime);
                apiDealRepository.save(apiDeal);
                if (j == 3) {
                    expected.put(apiUser.getPid(), apiDeal.getPid());
                }
            }
        }
        for (Long userPid : expected.keySet()) {
            assertEquals(expected.get(userPid), apiDealRepository.getFirstDealPid(userPid));
        }
        assertNull(apiDealRepository.getFirstDealPid(Long.MAX_VALUE));
    }

    @Test
    void getFirstDeal() {
        Map<Long, ApiDeal> expected = new HashMap<>();
        int usersCount = 3;
        for (int i = 0; i < usersCount; i++) {
            ApiUser apiUser = new ApiUser();
            apiUser.setFiatCurrency(FiatCurrency.BYN);
            apiUserRepository.save(apiUser);
            assertNull(apiDealRepository.getFirstDeal(apiUser.getPid()));
            for (int j = 5; j > 0; j--) {
                ApiDeal apiDeal = new ApiDeal();
                apiDeal.setApiUser(apiUser);
                LocalDateTime dateTime;
                if (j == 3) {
                    dateTime = LocalDateTime.of(2000, 1, 1, 0, 1);
                } else {
                    dateTime = LocalDateTime.of(2001, 1, j, 0, 1);
                }
                apiDeal.setDateTime(dateTime);
                apiDealRepository.save(apiDeal);
                if (j == 3) {
                    expected.put(apiUser.getPid(), apiDeal);
                }
            }
        }
        for (Long userPid : expected.keySet()) {
            assertEquals(expected.get(userPid), apiDealRepository.getFirstDeal(userPid));
        }
        assertNull(apiDealRepository.getFirstDeal(Long.MAX_VALUE));
    }

    @Test
    void getLastDeal() {
        Map<Long, ApiDeal> expected = new HashMap<>();
        int usersCount = 3;
        for (int i = 0; i < usersCount; i++) {
            ApiUser apiUser = new ApiUser();
            apiUser.setFiatCurrency(FiatCurrency.BYN);
            apiUserRepository.save(apiUser);
            assertNull(apiDealRepository.getLastDeal(apiUser.getPid()));
            for (int j = 5; j > 0; j--) {
                ApiDeal apiDeal = new ApiDeal();
                apiDeal.setApiUser(apiUser);
                LocalDateTime dateTime;
                if (j == 3) {
                    dateTime = LocalDateTime.of(2002, 1, 1, 0, 1);
                } else {
                    dateTime = LocalDateTime.of(2001, 1, j, 0, 1);
                }
                apiDeal.setDateTime(dateTime);
                apiDealRepository.save(apiDeal);
                if (j == 3) {
                    expected.put(apiUser.getPid(), apiDeal);
                }
            }
        }
        for (Long userPid : expected.keySet()) {
            assertEquals(expected.get(userPid), apiDealRepository.getLastDeal(userPid));
        }
        assertNull(apiDealRepository.getLastDeal(Long.MAX_VALUE));
    }

    @Test
    void getAcceptedByDateTimeBetween() {
    }

    @Test
    void getAcceptedByDateTimeBetweenExcludeEnd() {
    }

    @Test
    void getAcceptedByDateTimeAfter() {
    }

    @Test
    void getAcceptedByDateTimeBefore() {
    }

    @Test
    void getApiUserPidByDealPid() {
    }

    @Test
    void getCountByTokenAndPid() {
    }

    @Test
    void getApiDealTypeByPid() {
    }

    @Test
    void getCheckImageIdByPid() {
    }

    @Test
    void getReceiptFormatByPid() {
    }

    @Test
    void updateApiDealStatusByPid() {
    }

    @Test
    void updateDealsApiUser() {
    }

    @Test
    void deleteByApiUserId() {
    }
}