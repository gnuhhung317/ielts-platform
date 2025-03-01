package com.duchung.vn.service;

import com.duchung.vn.entity.BaseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

public interface BaseService<T extends BaseEntity, D> {

    D save(D dto);

    D update(Long id, D dto);

    void delete(Long id);

    void softDelete(Long id);

    Optional<D> findById(Long id);

    List<D> findAll();

    Page<D> findAll(Pageable pageable);

    Page<D> findAll(Specification<T> spec, Pageable pageable);
}