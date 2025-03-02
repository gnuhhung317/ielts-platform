package com.duchung.vn.service.impl;

import com.duchung.vn.entity.BaseEntity;
import com.duchung.vn.exception.ResourceNotFoundException;
import com.duchung.vn.mapper.EntityMapper;
import com.duchung.vn.repository.BaseRepository;
import com.duchung.vn.service.BaseService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public abstract class BaseServiceImpl<T extends BaseEntity, D> implements BaseService<T, D> {

    private final BaseRepository<T> repository;
    private final EntityMapper<T, D> mapper;
    private final String entityName;

    @Override
    @Transactional
    public D save(D dto) {
        T entity = mapper.toEntity(dto);
        T savedEntity = repository.save(entity);
        return mapper.toDto(savedEntity);
    }

    @Override
    @Transactional
    public D update(Long id, D dto) {
        return repository.findActiveById(id)
                .map(existingEntity -> {
                    T entity = mapper.partialUpdate(existingEntity, dto);
                    T savedEntity = repository.save(entity);
                    return mapper.toDto(savedEntity);
                })
                .orElseThrow(() -> new ResourceNotFoundException(entityName, "id", id));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    @Transactional
    public void softDelete(Long id) {
        repository.findActiveById(id)
                .map(entity -> {
                    entity.setActive(false);
                    repository.save(entity);
                    return entity;
                })
                .orElseThrow(() -> new ResourceNotFoundException(entityName, "id", id));
    }

    @Override
    public Optional<D> findById(Long id) {
        return repository.findActiveById(id).map(mapper::toDto);
    }

    @Override
    public List<D> findAll() {
        return repository.findAllActive().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Page<D> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toDto);
    }

    @Override
    public Page<D> findAll(Specification<T> spec, Pageable pageable) {
        return repository.findAll(spec, pageable).map(mapper::toDto);
    }

}