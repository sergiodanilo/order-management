package com.project.test.ordermanagement.service.base;

import java.util.List;
import java.util.Optional;

public interface CRUDBaseService<T, ID> {

    T save(T entity);

    Optional<T> findById(ID id);

    List<T> findAll();

    Optional<T> update(ID id, T entity);

    void delete(ID id);

}