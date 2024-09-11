package tgb.btc.library.repository.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import tgb.btc.library.bean.web.Role;

import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ActiveProfiles("test")
class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    void getByName() {
        Role role = new Role();
        role.setName("test");
        roleRepository.save(role);
        assertEquals(Set.of(role), roleRepository.getByName("test"));
        assertEquals(Collections.emptySet(), roleRepository.getByName(""));
    }
}