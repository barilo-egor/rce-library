package tgb.btc.library.service.bean.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tgb.btc.library.bean.web.Role;
import tgb.btc.library.constants.enums.web.RoleConstants;
import tgb.btc.library.repository.web.RoleRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class RoleService {

    private RoleRepository roleRepository;

    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public void initRoles() {
        List<String> rolesDBName = roleRepository.findAll().stream().map(Role::getName).collect(Collectors.toList());
        if (RoleConstants.values().length != rolesDBName.size()) {
            List<Role> roleList = new ArrayList<>();
            Arrays.stream(RoleConstants.values()).filter(role -> !rolesDBName.contains(role.name())).forEach(role -> roleList.add(new Role(role.name())));
            roleRepository.saveAll(roleList);
        }
    }
}
