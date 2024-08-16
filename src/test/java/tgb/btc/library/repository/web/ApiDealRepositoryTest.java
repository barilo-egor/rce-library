package tgb.btc.library.repository.web;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import tgb.btc.library.bean.BasePersist;
import tgb.btc.library.bean.web.api.ApiDeal;
import tgb.btc.library.bean.web.api.ApiUser;
import tgb.btc.library.constants.enums.ApiDealType;
import tgb.btc.library.constants.enums.bot.FiatCurrency;
import tgb.btc.library.constants.enums.bot.ReceiptFormat;
import tgb.btc.library.constants.enums.web.ApiDealStatus;

import javax.persistence.EntityManager;
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

    @Autowired
    private EntityManager entityManager;

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
        ApiUser apiUserForAccepted = new ApiUser();
        apiUserForAccepted.setFiatCurrency(FiatCurrency.BYN);
        apiUserRepository.save(apiUserForAccepted);

        ApiUser apiUserForConfirmed = new ApiUser();
        apiUserForConfirmed.setFiatCurrency(FiatCurrency.BYN);
        apiUserRepository.save(apiUserForConfirmed);

        int dealsCount = 3;
        Set<ApiDeal> expected = new HashSet<>();
        for (int i = 1; i <= dealsCount; i++) {
            LocalDateTime dateTime = LocalDateTime.of(2001, 1, i, 0, 1);

            ApiDeal acceptedApiDeal = new ApiDeal();
            acceptedApiDeal.setApiUser(apiUserForAccepted);
            acceptedApiDeal.setApiDealStatus(ApiDealStatus.ACCEPTED);
            acceptedApiDeal.setDateTime(dateTime);
            apiDealRepository.save(acceptedApiDeal);
            expected.add(acceptedApiDeal);

            ApiDeal confirmedApiDeal = new ApiDeal();
            confirmedApiDeal.setApiUser(apiUserForConfirmed);
            confirmedApiDeal.setApiDealStatus(ApiDealStatus.ACCEPTED);
            confirmedApiDeal.setDateTime(dateTime);
            apiDealRepository.save(confirmedApiDeal);

            ApiDeal apiDealWithoutUser = new ApiDeal();
            apiDealWithoutUser.setApiDealStatus(ApiDealStatus.ACCEPTED);
            apiDealWithoutUser.setDateTime(dateTime);
            apiDealRepository.save(apiDealWithoutUser);
        }

        assertEquals(expected, new HashSet<>(apiDealRepository.getAcceptedByDateTimeBetween(
                LocalDateTime.of(2001, 1, 1, 0, 1),
                LocalDateTime.of(2001, 1, dealsCount, 0, 1),
                apiUserForAccepted.getPid())));
    }

    @Test
    void getAcceptedByDateTimeAfter() {
        ApiUser apiUserForAccepted = new ApiUser();
        apiUserForAccepted.setFiatCurrency(FiatCurrency.BYN);
        apiUserRepository.save(apiUserForAccepted);

        ApiUser apiUserForConfirmed = new ApiUser();
        apiUserForConfirmed.setFiatCurrency(FiatCurrency.BYN);
        apiUserRepository.save(apiUserForConfirmed);

        int dealsCount = 3;
        Set<ApiDeal> expected = new HashSet<>();
        for (int i = 1; i <= dealsCount; i++) {
            LocalDateTime dateTime = LocalDateTime.of(2001, 1, i, 0, 1);

            ApiDeal acceptedApiDeal = new ApiDeal();
            acceptedApiDeal.setApiUser(apiUserForAccepted);
            acceptedApiDeal.setApiDealStatus(ApiDealStatus.ACCEPTED);
            acceptedApiDeal.setDateTime(dateTime);
            apiDealRepository.save(acceptedApiDeal);
            if (i > 1) {
                expected.add(acceptedApiDeal);
            }

            ApiDeal confirmedApiDeal = new ApiDeal();
            confirmedApiDeal.setApiUser(apiUserForConfirmed);
            confirmedApiDeal.setApiDealStatus(ApiDealStatus.ACCEPTED);
            confirmedApiDeal.setDateTime(dateTime);
            apiDealRepository.save(confirmedApiDeal);

            ApiDeal apiDealWithoutUser = new ApiDeal();
            apiDealWithoutUser.setApiDealStatus(ApiDealStatus.ACCEPTED);
            apiDealWithoutUser.setDateTime(dateTime);
            apiDealRepository.save(apiDealWithoutUser);
        }

        assertEquals(expected, new HashSet<>(apiDealRepository.getAcceptedByDateTimeAfter(
                LocalDateTime.of(2001, 1, 2, 0, 1),
                apiUserForAccepted.getPid())));
    }

    @Test
    void getAcceptedByDateTimeBefore() {
        ApiUser apiUserForAccepted = new ApiUser();
        apiUserForAccepted.setFiatCurrency(FiatCurrency.BYN);
        apiUserRepository.save(apiUserForAccepted);

        ApiUser apiUserForConfirmed = new ApiUser();
        apiUserForConfirmed.setFiatCurrency(FiatCurrency.BYN);
        apiUserRepository.save(apiUserForConfirmed);

        int dealsCount = 3;
        Set<ApiDeal> expected = new HashSet<>();
        for (int i = 1; i <= dealsCount; i++) {
            LocalDateTime dateTime = LocalDateTime.of(2001, 1, i, 0, 1);

            ApiDeal acceptedApiDeal = new ApiDeal();
            acceptedApiDeal.setApiUser(apiUserForAccepted);
            acceptedApiDeal.setApiDealStatus(ApiDealStatus.ACCEPTED);
            acceptedApiDeal.setDateTime(dateTime);
            apiDealRepository.save(acceptedApiDeal);
            if (i != dealsCount) {
                expected.add(acceptedApiDeal);
            }

            ApiDeal confirmedApiDeal = new ApiDeal();
            confirmedApiDeal.setApiUser(apiUserForConfirmed);
            confirmedApiDeal.setApiDealStatus(ApiDealStatus.ACCEPTED);
            confirmedApiDeal.setDateTime(dateTime);
            apiDealRepository.save(confirmedApiDeal);

            ApiDeal apiDealWithoutUser = new ApiDeal();
            apiDealWithoutUser.setApiDealStatus(ApiDealStatus.ACCEPTED);
            apiDealWithoutUser.setDateTime(dateTime);
            apiDealRepository.save(apiDealWithoutUser);
        }

        assertEquals(expected, new HashSet<>(apiDealRepository.getAcceptedByDateTimeBefore(
                LocalDateTime.of(2001, 1, dealsCount - 1, 0, 1),
                apiUserForAccepted.getPid())));
    }

    @Test
    void getCountByTokenAndPid() {
        ApiUser apiUser = new ApiUser();
        apiUser.setToken("token");
        apiUser.setFiatCurrency(FiatCurrency.BYN);
        apiUserRepository.save(apiUser);
        Set<Long> pids = new HashSet<>();
        for (int i = 0; i < 3; i++) {
            ApiDeal apiDeal = new ApiDeal();
            apiDeal.setApiUser(apiUser);
            apiDeal.setApiDealStatus(ApiDealStatus.ACCEPTED);
            apiDealRepository.save(apiDeal);
            pids.add(apiDeal.getPid());
        }
        for (Long pid : pids) {
            assertEquals(1, apiDealRepository.getCountByTokenAndPid("token", pid));
            assertEquals(0, apiDealRepository.getCountByTokenAndPid("", pid));
        }
        assertEquals(0, apiDealRepository.getCountByTokenAndPid("token", Long.MAX_VALUE));
    }

    @Test
    void getApiDealTypeByPid() {
        ApiDealType variable = ApiDealType.API;
        ApiDeal apiDeal = new ApiDeal();
        apiDeal.setApiDealStatus(ApiDealStatus.ACCEPTED);
        apiDeal.setApiDealType(variable);
        apiDealRepository.save(apiDeal);
        assertEquals(variable, apiDealRepository.getApiDealTypeByPid(apiDeal.getPid()));
        assertNull(apiDealRepository.getApiDealTypeByPid(Long.MAX_VALUE));
    }

    @Test
    void getCheckImageIdByPid() {
        String variable = "check";
        ApiDeal apiDeal = new ApiDeal();
        apiDeal.setApiDealStatus(ApiDealStatus.ACCEPTED);
        apiDeal.setCheckImageId(variable);
        apiDealRepository.save(apiDeal);
        assertEquals(variable, apiDealRepository.getCheckImageIdByPid(apiDeal.getPid()));
        assertNull(apiDealRepository.getCheckImageIdByPid(Long.MAX_VALUE));
    }

    @Test
    void getReceiptFormatByPid() {
        ReceiptFormat variable = ReceiptFormat.PDF;
        ApiDeal apiDeal = new ApiDeal();
        apiDeal.setApiDealStatus(ApiDealStatus.ACCEPTED);
        apiDeal.setReceiptFormat(variable);
        apiDealRepository.save(apiDeal);
        assertEquals(variable, apiDealRepository.getReceiptFormatByPid(apiDeal.getPid()));
        assertNull(apiDealRepository.getReceiptFormatByPid(Long.MAX_VALUE));
    }

    @Test
    void updateApiDealStatusByPid() {
        ApiDealStatus variable = ApiDealStatus.PAID;
        ApiDeal apiDeal = new ApiDeal();
        apiDeal.setApiDealStatus(ApiDealStatus.CREATED);
        apiDealRepository.save(apiDeal);
        apiDealRepository.updateApiDealStatusByPid(variable, apiDeal.getPid());
        entityManager.clear();
        apiDeal = apiDealRepository.getByPid(apiDeal.getPid());
        assertEquals(variable, apiDeal.getApiDealStatus());
    }

    @Test
    void updateDealsApiUser() {

    }

    @Test
    void deleteByApiUserId() {
    }

    @Test
    void dropApiRequisite() {

    }

    @Test
    void dropApiPaymentType() {

    }
}