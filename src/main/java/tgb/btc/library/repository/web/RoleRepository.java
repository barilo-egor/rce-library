package tgb.btc.library.repository.web;

import org.springframework.stereotype.Repository;
import tgb.btc.library.bean.web.Role;
import tgb.btc.library.repository.BaseRepository;

import java.util.Set;

@Repository
public interface RoleRepository extends BaseRepository<Role> {

    Set<Role> getByName(String name);
}
