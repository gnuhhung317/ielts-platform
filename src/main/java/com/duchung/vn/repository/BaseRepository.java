package com.duchung.vn.repository;

import com.duchung.vn.entity.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface BaseRepository<T extends BaseEntity> extends JpaRepository<T, Long>, JpaSpecificationExecutor<T> {

    List<T> findAllByActive(Boolean active);

    Optional<T> findByIdAndActive(Long id, Boolean active);

    boolean existsByIdAndActive(Long id, Boolean active);

    default Optional<T> findActiveById(Long id) {
        return findByIdAndActive(id, true);
    }

    default List<T> findAllActive() {
        return findAllByActive(true);
    }

    default boolean existsActiveById(Long id) {
        return existsByIdAndActive(id, true);
    }
}