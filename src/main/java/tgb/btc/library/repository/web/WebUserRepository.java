package tgb.btc.library.repository.web;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tgb.btc.library.bean.web.Role;
import tgb.btc.library.bean.web.WebUser;
import tgb.btc.library.repository.BaseRepository;

import java.util.List;

@Repository
public interface WebUserRepository extends BaseRepository<WebUser> {

    int countByUsername(String username);

    WebUser getByUsername(String username);

    @Query("select roles from WebUser where username=:username")
    List<Role> getRolesByUsername(String username);

    WebUser getByChatId(Long chatId);

    boolean existsByChatId(Long chatId);

    boolean existsByUsername(String username);

    @Query("select username from WebUser")
    List<String> getUsernames();

    @Query("select chatId from WebUser where username=:username")
    Long getChatIdByUsername(String username);

    @Query("select soundEnabled from WebUser where username=:username")
    Boolean getSoundEnabledByUsername(String username);

    /**
     * UPDATE
     */

    @Modifying
    @Query("update WebUser set username=:username where pid=:pid")
    void updateUsernameByPid(Long pid, String username);

    @Modifying
    @Query("update WebUser set username=:newUsername where username=:oldUsername")
    void updateUsername(String newUsername, String oldUsername);

    @Modifying
    @Query("update WebUser set soundEnabled=:soundEnabled where username=:username")
    void updateSoundEnabled(String username, Boolean soundEnabled);
}
