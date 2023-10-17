package tgb.btc.library.bean.web;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.security.core.GrantedAuthority;
import tgb.btc.library.bean.BasePersist;
import tgb.btc.library.bean.bot.User;
import tgb.btc.library.interfaces.ObjectNodeConvertable;
import tgb.btc.library.util.web.JacksonUtil;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;
import java.util.Set;
import java.util.function.Function;

@Entity
public class Role extends BasePersist implements GrantedAuthority, ObjectNodeConvertable<Role> {

    private String name;

    @Transient
    @ManyToMany(mappedBy = "roles")
    private Set<User> users;

    public Role() {
    }

    public Role(Long pid) {
        super(pid);
    }

    public Role(Long pid, String name) {
        super(pid);
        this.name = name;
    }

    @Override
    public String getAuthority() {
        return getName();
    }

    @Column(unique = true)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    @Override
    public Function<Role, ObjectNode> mapFunction() {
        return role -> JacksonUtil.getEmpty()
                .put("name", role.getName());
    }
}
