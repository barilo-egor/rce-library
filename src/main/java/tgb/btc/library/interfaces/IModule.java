package tgb.btc.library.interfaces;

import java.util.Objects;

public interface IModule<T> {

    T getCurrent();

    default boolean isCurrent(T t) {
        if (Objects.isNull(t))
            return false;
        return getCurrent().equals(t);
    }
}
