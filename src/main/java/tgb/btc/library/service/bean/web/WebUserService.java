package tgb.btc.library.service.bean.web;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tgb.btc.library.bean.web.Role;
import tgb.btc.library.bean.web.WebUser;
import tgb.btc.library.bean.web.api.ApiUser;
import tgb.btc.library.constants.enums.web.RoleConstants;
import tgb.btc.library.interfaces.service.bean.bot.user.IReadUserService;
import tgb.btc.library.interfaces.service.bean.web.IWebUserService;
import tgb.btc.library.repository.BaseRepository;
import tgb.btc.library.repository.web.ApiUserRepository;
import tgb.btc.library.repository.web.RoleRepository;
import tgb.btc.library.repository.web.WebUserRepository;
import tgb.btc.library.service.bean.BasePersistService;

import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class WebUserService extends BasePersistService<WebUser> implements UserDetailsService, IWebUserService {

    private WebUserRepository webUserRepository;

    private BCryptPasswordEncoder passwordEncoder;

    private RoleRepository roleRepository;

    private ApiUserRepository apiUserRepository;

    private IReadUserService readUserService;

    @Autowired
    public void setReadUserService(IReadUserService readUserService) {
        this.readUserService = readUserService;
    }

    @Autowired
    public void setApiUserRepository(ApiUserRepository apiUserRepository) {
        this.apiUserRepository = apiUserRepository;
    }

    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Autowired
    public void setWebUserRepository(WebUserRepository webUserRepository) {
        this.webUserRepository = webUserRepository;
    }

    @Autowired
    public void setPasswordEncoder(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected BaseRepository<WebUser> getBaseRepository() {
        return webUserRepository;
    }

    public WebUser save(WebUser webUser) {
        webUser.setPassword(passwordEncoder.encode(webUser.getPassword()));
        return webUserRepository.save(webUser);
    }

    public WebUser save(String username, String password, RoleConstants role, Long chatId) {
        WebUser webUser = new WebUser();
        webUser.setUsername(username);
        webUser.setPassword(passwordEncoder.encode(password));
        webUser.setEnabled(true);
        webUser.setChatId(chatId);
        if (Objects.isNull(role)) {
            role = RoleConstants.ROLE_USER;
        }
        webUser.setRoles(roleRepository.getByName(role.name()));
        return webUserRepository.save(webUser);
    }

    @Transactional
    @Override
    public WebUser save(String username, Long chatId, String token) {
        WebUser webUser = new WebUser();
        webUser.setChatId(chatId);
        webUser.setUsername(username);
        webUser.setPassword(RandomStringUtils.randomAlphanumeric(10));
        webUser.setEnabled(true);
        RoleConstants roleConstants;
        ApiUser apiUser = null;
        if (StringUtils.isNotEmpty(token) && apiUserRepository.countByToken(token) == 1) {
            roleConstants = RoleConstants.ROLE_API_CLIENT;
            apiUser = apiUserRepository.getByToken(token);
        }
        else if (readUserService.isAdminByChatId(chatId)) roleConstants = RoleConstants.ROLE_OPERATOR;
        else roleConstants = RoleConstants.ROLE_USER;
        webUser.setRoles(roleRepository.getByName(roleConstants.name()));
        webUser = save(webUser);
        if (Objects.nonNull(apiUser)) {
            apiUser.getWebUsers().add(webUser);
            apiUserRepository.save(apiUser);
        }
        return webUser;
    }

    public WebUser getUser(String username) {
        return (WebUser) loadUserByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return webUserRepository.getByUsername(username);
    }

    @Override
    public int countByUsername(String username) {
        return webUserRepository.countByUsername(username);
    }

    @Override
    public WebUser getByUsername(String username) {
        return webUserRepository.getByUsername(username);
    }

    @Override
    public List<Role> getRolesByUsername(String username) {
        return webUserRepository.getRolesByUsername(username);
    }

    @Override
    public WebUser getByChatId(Long chatId) {
        return webUserRepository.getByChatId(chatId);
    }

    @Override
    public boolean existsByChatId(Long chatId) {
        return webUserRepository.existsByChatId(chatId);
    }

    @Override
    public boolean existsByUsername(String username) {
        return webUserRepository.existsByUsername(username);
    }

    @Override
    public List<String> getUsernames() {
        return webUserRepository.getUsernames();
    }

    @Override
    public Long getChatIdByUsername(String username) {
        return webUserRepository.getChatIdByUsername(username);
    }

    @Override
    public Boolean getSoundEnabledByUsername(String username) {
        return webUserRepository.getSoundEnabledByUsername(username);
    }

    @Override
    public void updateUsernameByPid(Long pid, String username) {
        webUserRepository.updateUsernameByPid(pid, username);
    }

    @Override
    public void updateUsername(String newUsername, String oldUsername) {
        webUserRepository.updateUsername(newUsername, oldUsername);
    }

    @Override
    public void updateSoundEnabled(String username, Boolean soundEnabled) {
        webUserRepository.updateSoundEnabled(username, soundEnabled);
    }

    @Override
    public List<String> getNotTiedToApiWebUsernames() {
        return webUserRepository.getNotTiedToApiWebUsernames();
    }

    @Override
    public List<String> getWebUsernamesByApiUserPid(Long apiUserPid) {
        return webUserRepository.getWebUsernamesByApiUserPid(apiUserPid);
    }

    @Override
    @Transactional
    public void addWebUser(String username, Long apiUserPid) {
        ApiUser apiUser = apiUserRepository.getById(apiUserPid);
        WebUser webUser = webUserRepository.getByUsername(username);
        apiUser.getWebUsers().add(webUser);
        apiUserRepository.save(apiUser);
    }

    @Override
    @Transactional
    public void removeWebUser(String username, Long apiUserPid) {
        ApiUser apiUser = apiUserRepository.getById(apiUserPid);
        apiUser.getWebUsers().removeIf(user -> user.getUsername().equals(username));
        apiUserRepository.save(apiUser);
    }
}
