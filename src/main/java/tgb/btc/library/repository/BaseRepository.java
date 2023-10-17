package tgb.btc.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import tgb.btc.library.bean.BasePersist;

@Repository
@Transactional
public interface BaseRepository<T extends BasePersist> extends JpaRepository<T, Long> {
}

