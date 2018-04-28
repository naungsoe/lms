package com.hsystems.lms.repository;

import com.hsystems.lms.common.query.Query;
import com.hsystems.lms.common.query.QueryResult;
import com.hsystems.lms.repository.entity.Entity;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Created by naungsoe on 10/8/16.
 */
public interface IndexRepository<T extends Entity> {

  Optional<T> findBy(String id, Class<T> type)
      throws IOException;

  QueryResult<T> findAllBy(Query query, Class<T> type)
      throws IOException;

  void save(List<T> entities)
      throws IOException;

  void save(T entity)
      throws IOException;

  void delete(T entity)
      throws IOException;
}
