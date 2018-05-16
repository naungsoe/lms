package com.hsystems.lms.entity;

import java.io.IOException;
import java.util.Optional;

/**
 * Created by administrator on 6/1/17.
 */
public interface Repository<T extends Entity> {

  Optional<T> findBy(String id)
      throws IOException;

  void add(T entity)
      throws IOException;

  void update(T entity)
      throws IOException;

  void remove(T entity)
      throws IOException;
}