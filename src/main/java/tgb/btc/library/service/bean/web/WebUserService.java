package tgb.btc.library.service.bean.web;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import tgb.btc.library.bean.web.WebUser;
import tgb.btc.library.constants.enums.web.RoleConstants;
import tgb.btc.library.repository.bot.UserRepository;
import tgb.btc.library.repository.web.ApiUserRepository;
import tgb.btc.library.repository.web.RoleRepository;
import tgb.btc.library.repository.web.WebUserRepository;

import java.util.Objects;

@Service
public class WebUserService implements UserDetailsService {

    private WebUserRepository webUserRepository;

    private BCryptPasswordEncoder passwordEncoder;

    private RoleRepository roleRepository;

    private ApiUserRepository apiUserRepository;

    private UserRepository userRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
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

    public WebUser save(String username, Long chatId, String token) {
        WebUser webUser = new WebUser();
        webUser.setChatId(chatId);
        webUser.setUsername(username);
        webUser.setPassword(RandomStringUtils.randomAlphanumeric(10));
        webUser.setEnabled(true);
        RoleConstants roleConstants;
        if (StringUtils.isNotEmpty(token) && apiUserRepository.countByToken(token) == 1)
            roleConstants = RoleConstants.ROLE_API_CLIENT;
        else if (userRepository.isAdminByChatId(chatId)) roleConstants = RoleConstants.ROLE_OPERATOR;
        else roleConstants = RoleConstants.ROLE_USER;
        webUser.setRoles(roleRepository.getByName(roleConstants.name()));
        return save(webUser);
    }

    public void changePassword(String userName, String newPassword) {
        WebUser webUser = getUser(userName);
        webUser.setPassword(passwordEncoder.encode(newPassword));
        webUserRepository.save(webUser);
    }

    public WebUser getUser(String username) {
        return (WebUser) loadUserByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return webUserRepository.getByUsername(username);
    }
}
