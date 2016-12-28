package com.hsystems.lms.repository;

import com.hsystems.lms.common.query.Query;
import com.hsystems.lms.common.query.QueryResult;

import java.io.IOException;

/**
 * Created by naungsoe on 10/8/16.
 */
public interface IndexRepository {

  <T> QueryResult findAllBy(Query query, Class<T> type)
      throws IOException;

  <T> void save(T model)
      throws IOException;
}
