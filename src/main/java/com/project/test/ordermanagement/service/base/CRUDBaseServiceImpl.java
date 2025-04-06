package com.project.test.ordermanagement.service.base;

import com.project.test.ordermanagement.utils.BeanCopyUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

@Transactional
@RequiredArgsConstructor
public class CRUDBaseServiceImpl<T, ID> implements CRUDBaseService<T, ID> {

    protected final JpaRepository<T, ID> repository;

    @Override
    public T save(T entity) {
        return repository.save(entity);
    }

    @Override
    public Optional<T> findById(ID id) {
        return repository.findById(id);
    }

    @Override
    public List<T> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<T> update(ID id, T entity) {
        Optional<T> optEntity = repository.findById(id);
        if (optEntity.isPresent()) {
            T savedEntity = optEntity.get();
            BeanCopyUtils.copyNonNullProperties(entity, savedEntity, "id");
            T updatedEntity = repository.save(savedEntity);
            return Optional.of(updatedEntity);
        }
        return Optional.empty();
    }

    @Override
    public void delete(ID id) {
        Optional<T> optEntity = repository.findById(id);
        if (optEntity.isPresent()) {
            repository.delete(optEntity.get());
        } else {
            throw new EntityNotFoundException("Entity not found with id: " + id);
        }
    }

}
