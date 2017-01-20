package com.hsystems.lms.repository;

import com.hsystems.lms.common.query.Query;
import com.hsystems.lms.common.query.QueryResult;

import java.io.IOException;
import java.util.Optional;

/**
 * Created by naungsoe on 10/8/16.
 */
public interface IndexRepository {

  <T> Optional<T> findBy(String id, Class<T> type)
      throws IOException;

  <T> QueryResult findAllBy(Query query, Class<T> type)
      throws IOException;

  <T> void save(T entity)
      throws IOException;

  <T> void delete(T entity)
      throws IOException;
}
