package tgb.btc.library.repository.web;

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
}
