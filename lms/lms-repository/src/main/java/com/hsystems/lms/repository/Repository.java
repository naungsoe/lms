package com.hsystems.lms.repository;

import com.hsystems.lms.repository.entity.Entity;

import java.io.IOException;
import java.util.Optional;

/**
 * Created by administrator on 6/1/17.
 */
public interface Repository<T extends Entity> {

  Optional<T> findBy(String id)
      throws IOException;

  void create(T entity)
      throws IOException;

  void update(T entity)
      throws IOException;

  void delete(T entity)
      throws IOException;
}