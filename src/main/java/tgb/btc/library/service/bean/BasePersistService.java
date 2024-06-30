package tgb.btc.library.service.bean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tgb.btc.library.bean.BasePersist;
import tgb.btc.library.exception.BaseException;
import tgb.btc.library.interfaces.service.IBasePersistService;
import tgb.btc.library.repository.BaseRepository;

import java.util.List;

@Service
public class BasePersistService<T extends BasePersist> implements IBasePersistService<T> {
    private final BaseRepository<T> baseRepository;

    @Autowired
    public BasePersistService(BaseRepository<T> baseRepository) {
        this.baseRepository = baseRepository;
    }

    public T findById(Long pid) {
        return baseRepository.findById(pid).orElseThrow(
                () -> new BaseException("Запись по pid " + pid + " не найдена."));
    }

    public void deleteById(Long pid) {
        baseRepository.deleteById(pid);
    }

    public void delete(T t) {
        baseRepository.delete(t);
    }

    public void deleteAll() {
        baseRepository.deleteAll();
    }

    public T save(T t) {
        return baseRepository.save(t);
    }

    public List<T> findAll() {
        return baseRepository.findAll();
    }

    public boolean existsById(Long pid) {
        return baseRepository.existsById(pid);
    }
}