package com.hsystems.lms.repository;

import com.hsystems.lms.repository.entity.Entity;

import java.io.IOException;
import java.util.Optional;

/**
 * Created by administrator on 6/1/17.
 */
public interface EntityRepository<T extends Entity> {

  Optional<T> findBy(String id, long timestamp)
      throws IOException;

  void save(T entity, long timestamp)
      throws IOException;

  void delete(T entity, long timestamp)
      throws IOException;
}