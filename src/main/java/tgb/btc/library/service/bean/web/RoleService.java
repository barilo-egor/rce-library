package tgb.btc.library.service.bean.web;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tgb.btc.library.bean.web.Role;
import tgb.btc.library.constants.enums.web.RoleConstants;
import tgb.btc.library.interfaces.service.bean.web.IRoleService;
import tgb.btc.library.repository.BaseRepository;
import tgb.btc.library.repository.web.RoleRepository;
import tgb.btc.library.service.bean.BasePersistService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class RoleService extends BasePersistService<Role> implements IRoleService {

    private RoleRepository roleRepository;

    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @PostConstruct
    public void initRoles() {
        List<String> rolesDBName = roleRepository.findAll().stream().map(Role::getName).collect(Collectors.toList());
        if (RoleConstants.values().length != rolesDBName.size()) {
            List<Role> roleList = new ArrayList<>();
            Arrays.stream(RoleConstants.values()).filter(role -> !rolesDBName.contains(role.name())).forEach(role -> roleList.add(new Role(role.name())));
            roleRepository.saveAll(roleList);
        }
    }

    @Override
    public Set<Role> getByName(String name) {
        return roleRepository.getByName(name);
    }

    @Override
    protected BaseRepository<Role> getBaseRepository() {
        return roleRepository;
    }

}
