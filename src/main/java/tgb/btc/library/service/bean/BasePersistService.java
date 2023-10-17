package tgb.btc.library.service.bean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tgb.btc.library.bean.BasePersist;
import tgb.btc.library.exception.BaseException;
import tgb.btc.library.repository.BaseRepository;

@Service
public class BasePersistService<T extends BasePersist> {
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

    public T save(T t) {
        return baseRepository.save(t);
    }
}