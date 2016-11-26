package com.hsystems.lms.repository;

import com.hsystems.lms.repository.exception.RepositoryException;
import com.hsystems.lms.common.query.Query;
import com.hsystems.lms.common.query.QueryResult;

/**
 * Created by naungsoe on 10/8/16.
 */
public interface IndexRepository {

  <T> QueryResult findAllBy(Query query, Class<T> type)
      throws RepositoryException;

  <T> void save(T model)
      throws RepositoryException;
}
