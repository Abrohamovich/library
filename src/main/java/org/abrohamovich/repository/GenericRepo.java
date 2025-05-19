package org.abrohamovich.repository;

import java.util.List;
import java.util.Optional;

public interface GenericRepo<T, ID> {
    Optional<T> save(T entity);

    Optional<T> findById(long id);

    List<T> findAll();

    Optional<T> update(T entity);

    void deleteById(long id);
}
