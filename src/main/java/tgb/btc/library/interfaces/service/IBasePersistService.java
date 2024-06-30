package tgb.btc.library.interfaces.service;

import tgb.btc.library.bean.BasePersist;

import java.util.Collection;
import java.util.List;

public interface IBasePersistService<T extends BasePersist> {

    T findById(Long pid);

    void deleteById(Long pid);

    void delete(T t);

    void deleteAll();

    void deleteAll(Collection<T> collection);

    T save(T t);

    List<T> findAll();
}
