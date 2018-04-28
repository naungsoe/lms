package com.hsystems.lms.entity.repository;

import com.hsystems.lms.entity.repository.query.Query;
import com.hsystems.lms.entity.repository.query.QueryResult;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Created by administrator on 6/1/17.
 */
public interface Repository<T> {

  QueryResult<T> findAllBy(Query query)
      throws IOException;

  Optional<T> findBy(String id)
      throws IOException;

  void add(List<T> entity)
      throws IOException;

  void add(T entity)
      throws IOException;

  void update(T entity)
      throws IOException;

  void remove(T entity)
      throws IOException;
}