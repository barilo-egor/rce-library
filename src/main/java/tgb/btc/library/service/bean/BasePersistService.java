package tgb.btc.library.service.bean;

import org.springframework.data.jpa.repository.Modifying;
import tgb.btc.library.bean.BasePersist;
import tgb.btc.library.exception.BaseException;
import tgb.btc.library.interfaces.service.IBasePersistService;
import tgb.btc.library.repository.BaseRepository;

import java.util.Collection;
import java.util.List;

public abstract class BasePersistService<T extends BasePersist> implements IBasePersistService<T> {

    protected abstract BaseRepository<T> getBaseRepository();

    public T findById(Long pid) {
        return getBaseRepository().findById(pid).orElseThrow(
                () -> new BaseException("Запись по pid " + pid + " не найдена."));
    }

    public void deleteById(Long pid) {
        getBaseRepository().deleteById(pid);
    }

    public void delete(T t) {
        getBaseRepository().delete(t);
    }

    public void deleteAll() {
        getBaseRepository().deleteAll();
    }

    public void deleteAll(Collection<T> collection) {
        getBaseRepository().deleteAll(collection);
    }

    @Modifying
    public T save(T t) {
        return getBaseRepository().save(t);
    }

    public List<T> findAll() {
        return getBaseRepository().findAll();
    }

    public boolean existsById(Long pid) {
        return getBaseRepository().existsById(pid);
    }
}