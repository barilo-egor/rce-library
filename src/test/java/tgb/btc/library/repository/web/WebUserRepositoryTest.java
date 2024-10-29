package tgb.btc.library.repository.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import tgb.btc.library.bean.web.Role;
import tgb.btc.library.bean.web.WebUser;
import tgb.btc.library.bean.web.api.ApiUser;
import tgb.btc.library.constants.enums.bot.FiatCurrency;
import tgb.btc.library.constants.enums.web.RoleConstants;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class WebUserRepositoryTest {

    @Autowired
    private WebUserRepository webUserRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ApiUserRepository apiUserRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void getRolesByUsername() {
        Set<Role> roles = new HashSet<>();
        for (RoleConstants roleConstants : RoleConstants.values()) {
            Role role = new Role();
            role.setName(roleConstants.name());
            role = roleRepository.save(role);
            roles.add(role);
        }
        WebUser webUser = new WebUser();
        webUser.setRoles(roles);
        String username = "username";
        webUser.setUsername(username);
        webUser = webUserRepository.save(webUser);
        assertEquals(roles, new HashSet<>(webUserRepository.getRolesByUsername(username)));
        assertEquals(Collections.emptyList(), webUserRepository.getRolesByUsername(""));
    }

    @Test
    void getByChatId() {
        WebUser webUser = new WebUser();
        Long chatId = 12345678L;
        webUser.setChatId(chatId);
        webUserRepository.save(webUser);
        assertEquals(webUser, webUserRepository.getByChatId(chatId));
        assertNull(webUserRepository.getByChatId(Long.MAX_VALUE));
    }

    @Test
    void existsByChatId() {
        WebUser webUser = new WebUser();
        Long chatId = 12345678L;
        webUser.setChatId(chatId);
        webUserRepository.save(webUser);
        assertTrue(webUserRepository.existsByChatId(chatId));
        assertFalse(webUserRepository.existsByChatId(Long.MAX_VALUE));
    }

    @Test
    void existsByUsername() {
        WebUser webUser = new WebUser();
        String username = "username";
        webUser.setUsername(username);
        webUserRepository.save(webUser);
        assertTrue(webUserRepository.existsByUsername(username));
        assertFalse(webUserRepository.existsByUsername(""));
    }

    @Test
    void getUsernames() {
        assertEquals(Collections.emptyList(), webUserRepository.getUsernames());
        Set<String> expected = new HashSet<>();
        for (int i = 0; i < 5; i++) {
            String username = "username" + i;
            WebUser webUser = new WebUser();
            webUser.setUsername(username);
            webUserRepository.save(webUser);
            expected.add(username);
        }
        assertEquals(expected, new HashSet<>(webUserRepository.getUsernames()));
    }

    @Test
    void getChatIdByUsername() {
        WebUser webUser = new WebUser();
        Long expected = 12345678L;
        webUser.setChatId(expected);
        String username = "username";
        webUser.setUsername(username);
        webUserRepository.save(webUser);
        assertEquals(expected, webUserRepository.getChatIdByUsername(username));
        assertNull(webUserRepository.getChatIdByUsername(""));
    }

    @Test
    void getSoundEnabledByUsername() {
        WebUser webUser = new WebUser();
        Boolean expected = true;
        webUser.setSoundEnabled(expected);
        String username = "username";
        webUser.setUsername(username);
        webUserRepository.save(webUser);
        assertEquals(expected, webUserRepository.getSoundEnabledByUsername(username));
        assertNull(webUserRepository.getSoundEnabledByUsername(""));
    }

    @Test
    void getWebUsernamesByApiUserPid() {
        ApiUser apiUser = new ApiUser();
        apiUser.setFiatCurrency(FiatCurrency.BYN);
        apiUserRepository.save(apiUser);
        Set<String> expected = new HashSet<>();
        for (int i = 0; i < 3; i++) {
            WebUser webUser = new WebUser();
            String username = "username" + i;
            webUser.setUsername(username);
            expected.add(username);
            apiUser.getWebUsers().add(webUser);
            webUserRepository.save(webUser);
        }
        for (int i = 0; i < 3; i++) {
            WebUser webUser = new WebUser();
            String username = "username2" + i;
            webUser.setUsername(username);
            webUserRepository.save(webUser);
        }
        assertEquals(expected, new HashSet<>(webUserRepository.getWebUsernamesByApiUserPid(apiUser.getPid())));
        assertEquals(Collections.emptyList(), webUserRepository.getWebUsernamesByApiUserPid(Long.MAX_VALUE));
    }

    @Test
    void getNotTiedToApiWebUsernames() {
        ApiUser apiUser = new ApiUser();
        apiUser.setFiatCurrency(FiatCurrency.BYN);
        apiUserRepository.save(apiUser);
        Set<String> expected = new HashSet<>();
        for (int i = 0; i < 3; i++) {
            WebUser webUser = new WebUser();
            String username = "username" + i;
            webUser.setUsername(username);
            apiUser.getWebUsers().add(webUser);
            webUserRepository.save(webUser);
        }
        for (int i = 0; i < 3; i++) {
            WebUser webUser = new WebUser();
            String username = "username2" + i;
            expected.add(username);
            webUser.setUsername(username);
            webUserRepository.save(webUser);
        }
        assertEquals(expected, new HashSet<>(webUserRepository.getNotTiedToApiWebUsernames()));
    }

    @Test
    void updateUsernameByPid() {
        WebUser webUser = new WebUser();
        String username = "username";
        webUser.setUsername(username);
        webUserRepository.save(webUser);

        assertEquals(webUser, webUserRepository.getByUsername(username));
        String newUsername = "newUsername";

        webUserRepository.updateUsernameByPid(webUser.getPid(), newUsername);

        assertEquals(webUser.getPid(), webUserRepository.getByUsername(newUsername).getPid());
    }

    @Test
    void updateUsername() {
        WebUser webUser = new WebUser();
        String username = "username";
        webUser.setUsername(username);
        webUserRepository.save(webUser);
        assertEquals(webUser, webUserRepository.getByUsername(username));

        String newUsername = "newUsername";
        webUserRepository.updateUsername(newUsername, username);

        assertEquals(webUser.getPid(), webUserRepository.getByUsername(newUsername).getPid());
    }

    @Test
    void updateSoundEnabled() {
        WebUser webUser = new WebUser();
        String username = "username";
        webUser.setUsername(username);
        Boolean soundEnabled = true;
        webUser.setSoundEnabled(soundEnabled);
        webUserRepository.save(webUser);
        webUserRepository.updateSoundEnabled(username, false);
        entityManager.clear();
        assertFalse(webUserRepository.getByUsername(username).getSoundEnabled());
    }
}