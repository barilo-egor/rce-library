package tgb.btc.library.bean;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.Objects;

@MappedSuperclass
public class BasePersist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Getter
    @Setter
    private Long pid;

    public BasePersist() {
    }

    public BasePersist(Long pid) {
        this.pid = pid;
    }

    @Override
    public String toString() {
        return "BasePersist{" +
                "pid=" + pid +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BasePersist that = (BasePersist) o;
        return Objects.equals(pid, that.pid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pid);
    }
}