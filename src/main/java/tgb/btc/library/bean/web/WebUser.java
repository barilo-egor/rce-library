package tgb.btc.library.bean.web;

import org.apache.commons.lang.BooleanUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.CollectionUtils;
import tgb.btc.library.bean.BasePersist;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "WEB_USER")
public class WebUser extends BasePersist implements UserDetails {

    private String username;

    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles;

    private Boolean isEnabled;

    private Long chatId;

    public WebUser() {
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    @Column(name = "USERNAME", nullable = false, unique = true)
    public String getUsername() {
        return username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }


    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Set<Role> getRoles() {
        if (CollectionUtils.isEmpty(roles)) return new HashSet<>();
        return roles;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return BooleanUtils.isNotFalse(isEnabled);
    }

    public void setEnabled(Boolean enabled) {
        isEnabled = enabled;
    }

    @Column(nullable = false)
    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }
}
