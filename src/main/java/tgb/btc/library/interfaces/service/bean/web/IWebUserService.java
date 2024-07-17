package tgb.btc.library.interfaces.service.bean.web;

import tgb.btc.library.bean.web.Role;
import tgb.btc.library.bean.web.WebUser;
import tgb.btc.library.interfaces.service.IBasePersistService;

import java.util.List;

public interface IWebUserService extends IBasePersistService<WebUser> {

    int countByUsername(String username);

    WebUser getByUsername(String username);

    List<Role> getRolesByUsername(String username);

    WebUser getByChatId(Long chatId);

    boolean existsByChatId(Long chatId);

    boolean existsByUsername(String username);

    List<String> getUsernames();

    Long getChatIdByUsername(String username);

    Boolean getSoundEnabledByUsername(String username);

    /**
     * UPDATE
     */

    WebUser save(String username, Long chatId, String token);

    void updateUsernameByPid(Long pid, String username);

    void updateUsername(String newUsername, String oldUsername);

    void updateSoundEnabled(String username, Boolean soundEnabled);

    List<String> getNotTiedToApiWebUsernames();

    List<String> getWebUsernamesByApiUserPid(Long apiUserPid);

    void addWebUser(String username, Long apiUserPid);

    void removeWebUser(String username, Long apiUserPid);
}
