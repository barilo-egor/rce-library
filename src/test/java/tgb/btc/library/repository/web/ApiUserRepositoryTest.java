package tgb.btc.library.repository.web;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import tgb.btc.library.bean.bot.GroupChat;
import tgb.btc.library.bean.web.WebUser;
import tgb.btc.library.bean.web.api.ApiDeal;
import tgb.btc.library.bean.web.api.ApiPaymentType;
import tgb.btc.library.bean.web.api.ApiUser;
import tgb.btc.library.constants.enums.MemberStatus;
import tgb.btc.library.constants.enums.bot.DealType;
import tgb.btc.library.constants.enums.bot.FiatCurrency;
import tgb.btc.library.repository.bot.GroupChatRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class ApiUserRepositoryTest {

    @Autowired
    private ApiUserRepository apiUserRepository;

    @Autowired
    private ApiDealRepository apiDealRepository;

    @Autowired
    private WebUserRepository webUserRepository;

    @Autowired
    private GroupChatRepository groupChatRepository;

    @Autowired
    private ApiPaymentTypeRepository apiPaymentTypeRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void isBanned() {
        ApiUser apiUser = new ApiUser();
        apiUser.setFiatCurrency(FiatCurrency.BYN);
        apiUser.setIsBanned(true);
        apiUserRepository.save(apiUser);
        assertTrue(apiUserRepository.isBanned(apiUser.getPid()));
        apiUser.setIsBanned(null);
        apiUserRepository.save(apiUser);
        assertNull(apiUserRepository.isBanned(apiUser.getPid()));
        apiUser.setIsBanned(false);
        apiUserRepository.save(apiUser);
        assertFalse(apiUserRepository.isBanned(apiUser.getPid()));
    }

    @Test
    void getPidByToken() {
        ApiUser apiUser = new ApiUser();
        apiUser.setFiatCurrency(FiatCurrency.BYN);
        apiUser.setToken("token");
        apiUserRepository.save(apiUser);
        Long expected = apiUser.getPid();
        assertEquals(expected, apiUserRepository.getPidByToken("token"));
    }

    @Test
    void getById() {
        ApiUser apiUser = new ApiUser();
        apiUser.setFiatCurrency(FiatCurrency.BYN);
        apiUser.setId("id");
        apiUserRepository.save(apiUser);
        assertEquals(apiUser, apiUserRepository.getById(apiUser.getId()));
        assertNull(apiUserRepository.getById("qwe"));
    }

    @Test
    void getLastPaidDealPidByUserPid() {
        ApiDeal apiDeal = apiDealRepository.save(ApiDeal.builder().build());
        ApiUser apiUser = new ApiUser();
        apiUser.setFiatCurrency(FiatCurrency.BYN);
        apiUser.setLastPaidDeal(apiDeal);
        apiUserRepository.save(apiUser);
        assertEquals(apiDeal.getPid(), apiUserRepository.getLastPaidDealPidByUserPid(apiUser.getPid()));
        assertNull(apiUserRepository.getLastPaidDealPidByUserPid(Long.MAX_VALUE));
    }

    @Test
    void getLastPaidDeal() {
        ApiDeal apiDeal = apiDealRepository.save(ApiDeal.builder().build());
        ApiUser apiUser = new ApiUser();
        apiUser.setFiatCurrency(FiatCurrency.BYN);
        apiUser.setLastPaidDeal(apiDeal);
        apiUserRepository.save(apiUser);
        assertEquals(apiDeal, apiUserRepository.getLastPaidDeal(apiUser.getPid()));
        assertNull(apiUserRepository.getLastPaidDeal(Long.MAX_VALUE));
    }

    @Test
    void getWebUsers() {
        WebUser webUser = new WebUser();
        webUser.setChatId(1L);
        webUser.setUsername("username");
        webUserRepository.save(webUser);
        ApiUser apiUser = new ApiUser();
        apiUser.setFiatCurrency(FiatCurrency.BYN);
        apiUser.getWebUsers().add(webUser);
        apiUserRepository.save(apiUser);
        assertEquals(List.of(webUser), apiUserRepository.getWebUsers(apiUser.getPid()));
        assertEquals(Collections.emptyList(), apiUserRepository.getWebUsers(Long.MAX_VALUE));
    }

    @Test
    void getByGroupChatPid() {
        GroupChat groupChat = new GroupChat();
        groupChat.setChatId(1L);
        groupChat.setMemberStatus(MemberStatus.MEMBER);
        groupChat.setRegisterDateTime(LocalDateTime.now());
        groupChat.setIsSendMessageEnabled(true);
        groupChatRepository.save(groupChat);
        ApiUser apiUser = new ApiUser();
        apiUser.setFiatCurrency(FiatCurrency.BYN);
        apiUser.setGroupChat(groupChat);
        apiUserRepository.save(apiUser);
        assertEquals(apiUser, apiUserRepository.getByGroupChatPid(groupChat.getPid()));
        assertNull(apiUserRepository.getByGroupChatPid(Long.MAX_VALUE));
    }

    @Test
    void getByGroupChatId() {
        GroupChat groupChat = new GroupChat();
        groupChat.setChatId(1L);
        groupChat.setMemberStatus(MemberStatus.MEMBER);
        groupChat.setRegisterDateTime(LocalDateTime.now());
        groupChat.setIsSendMessageEnabled(true);
        groupChatRepository.save(groupChat);
        ApiUser apiUser = new ApiUser();
        apiUser.setFiatCurrency(FiatCurrency.BYN);
        apiUser.setGroupChat(groupChat);
        apiUserRepository.save(apiUser);
        assertEquals(apiUser, apiUserRepository.getByGroupChatId(1L));
        assertNull(apiUserRepository.getByGroupChatPid(Long.MAX_VALUE));
    }

    @Test
    void getIdByPaymentTypePid() {
        ApiPaymentType apiPaymentType = new ApiPaymentType();
        apiPaymentType.setDealType(DealType.BUY);
        apiPaymentType.setFiatCurrency(FiatCurrency.BYN);
        apiPaymentType.setMinSum(new BigDecimal(1));
        apiPaymentTypeRepository.save(apiPaymentType);
        ApiUser apiUser = new ApiUser();
        apiUser.setId("id");
        apiUser.setFiatCurrency(FiatCurrency.BYN);
        apiUser.setPaymentTypes(List.of(apiPaymentType));
        apiUserRepository.save(apiUser);
        assertEquals(List.of("id"), apiUserRepository.getIdByPaymentTypePid(apiPaymentType.getPid()));
        assertEquals(List.of(), apiUserRepository.getIdByPaymentTypePid(Long.MAX_VALUE));
    }

    @Test
    void getIdExcludePaymentTypePid() {
        ApiPaymentType apiPaymentType = new ApiPaymentType();
        apiPaymentType.setDealType(DealType.BUY);
        apiPaymentType.setFiatCurrency(FiatCurrency.BYN);
        apiPaymentType.setMinSum(new BigDecimal(1));
        apiPaymentTypeRepository.save(apiPaymentType);
        ApiPaymentType excludeApiPaymentType = new ApiPaymentType();
        excludeApiPaymentType.setDealType(DealType.BUY);
        excludeApiPaymentType.setFiatCurrency(FiatCurrency.BYN);
        excludeApiPaymentType.setMinSum(new BigDecimal(1));
        apiPaymentTypeRepository.save(excludeApiPaymentType);
        Set<String> expected = new HashSet<>();
        for (int i = 0; i < 4; i++) {
            ApiUser apiUser = new ApiUser();
            apiUser.setFiatCurrency(FiatCurrency.BYN);
            if (i != 0) {
                String id = "id" + i;
                apiUser.setId(id);
                expected.add(id);
                apiUser.setPaymentTypes(List.of(apiPaymentType));
            } else {
                apiUser.setId("id");
                apiUser.setPaymentTypes(List.of(excludeApiPaymentType));
            }
            apiUserRepository.save(apiUser);
        }
        assertEquals(expected, new HashSet<>(apiUserRepository.getIdExcludePaymentTypePid(excludeApiPaymentType.getPid())));
    }

    @Test
    void getIdLikeQuery() {
        Set<String> allExpected = new HashSet<>();
        Set<String> expected = new HashSet<>();
        for (int i = 0; i < 10; i++) {
            ApiUser apiUser = new ApiUser();
            apiUser.setFiatCurrency(FiatCurrency.BYN);
            apiUser.setId("userId" + i);
            apiUserRepository.save(apiUser);
            if (i == 0) {
                expected.add(apiUser.getId());
            }
        }
        assertAll(
                () -> assertEquals(expected, new HashSet<>(apiUserRepository.getIdLikeQuery("userId0"))),
                () -> assertEquals(allExpected, new HashSet<>(apiUserRepository.getIdLikeQuery("userId"))),
                () -> assertEquals(allExpected, new HashSet<>(apiUserRepository.getIdLikeQuery("id")))
        );
    }

    @Test
    void deleteById() {
        ApiUser apiUserToBeDeleted = new ApiUser();
        apiUserToBeDeleted.setFiatCurrency(FiatCurrency.BYN);
        apiUserToBeDeleted.setId("id");
        apiUserRepository.save(apiUserToBeDeleted);
        for (int i = 0; i < 3; i++) {
            ApiUser apiUser = new ApiUser();
            apiUser.setFiatCurrency(FiatCurrency.BYN);
            apiUserRepository.save(apiUser);
        }
        assertEquals(4, apiUserRepository.count());
        apiUserRepository.deleteById(apiUserToBeDeleted.getId());
        assertEquals(3, apiUserRepository.count());
        assertNull(apiUserRepository.getById(apiUserToBeDeleted.getId()));
    }

    @Test
    void updateLastPidDeal() {
        ApiUser apiUser = new ApiUser();
        apiUser.setFiatCurrency(FiatCurrency.BYN);
        apiUserRepository.save(apiUser);
        assertNull(apiUserRepository.getLastPaidDealPidByUserPid(apiUser.getPid()));
        ApiDeal apiDeal = new ApiDeal();
        apiDealRepository.save(apiDeal);
        apiUserRepository.updateLastPidDeal(apiUser.getPid(), apiDeal);
        entityManager.clear();
        apiUser = apiUserRepository.getById(apiUser.getPid());
        assertEquals(apiDeal.getPid(), apiUserRepository.getLastPaidDealPidByUserPid(apiUser.getPid()));
    }

    @Test
    void updateGroupChat() {
        ApiUser apiUser = new ApiUser();
        apiUser.setId("id");
        apiUser.setFiatCurrency(FiatCurrency.BYN);
        apiUserRepository.save(apiUser);
        GroupChat groupChat = new GroupChat();
        groupChat.setChatId(1L);
        groupChat.setMemberStatus(MemberStatus.MEMBER);
        groupChat.setRegisterDateTime(LocalDateTime.now());
        groupChat.setIsSendMessageEnabled(true);
        groupChatRepository.save(groupChat);
        apiUserRepository.updateGroupChat(groupChat, apiUser.getPid());
        entityManager.clear();
        apiUser = apiUserRepository.getById("id");
        assertEquals(groupChat.getPid(), apiUser.getGroupChat().getPid());
    }
}