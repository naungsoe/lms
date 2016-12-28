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

  @Override
  public int hashCode() {
    int prime = 31;
    int result = 0;
    result = result * prime + Long.hashCode(elapsedTime);

    for (T entity : entities) {
      result = result * prime + entity.hashCode();
    }

    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if ((obj == null) || (getClass() != obj.getClass())) {
      return false;
    }

    QueryResult<T> queryResult = (QueryResult<T>) obj;
    long entityCount = queryResult.getEntities().stream()
        .filter(x -> entities.stream()
            .anyMatch(y -> x.equals(y))).count();

    return (elapsedTime == queryResult.getElapsedTime())
        && (entities.size() == entityCount);
  }

  @Override
  public String toString() {
    StringBuilder entitiesBuilder = new StringBuilder();
    entities.forEach(x -> entitiesBuilder.append(x).append(","));

    return String.format(
        "QueryResult{elapsedTime=%s, entities=%s}",
        elapsedTime, entitiesBuilder);
  }
}
