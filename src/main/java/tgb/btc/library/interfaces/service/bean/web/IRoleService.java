package tgb.btc.library.interfaces.service.bean.web;

import tgb.btc.library.bean.web.Role;

import java.util.Set;

public interface IRoleService {

    Set<Role> getByName(String name);
}
