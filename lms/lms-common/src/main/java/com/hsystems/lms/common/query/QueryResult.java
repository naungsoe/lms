package com.hsystems.lms.common.query;

import java.util.List;

/**
 * Created by naungsoe on 3/11/16.
 */
public class QueryResult<T> {

  private long elapsedTime;

  private List<T> entities;

  public QueryResult(long elapsedTime, List<T> entities) {
    this.elapsedTime = elapsedTime;
    this.entities = entities;
  }

  public long getElapsedTime() {
    return elapsedTime;
  }

  public List<T> getEntities() {
    return entities;
  }
}
