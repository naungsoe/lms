package com.hsystems.lms.common.query;

import java.util.List;

/**
 * Created by naungsoe on 3/11/16.
 */
public class QueryResult<T> {

  private long elapsedTime;

  private List<T> items;

  public QueryResult(long elapsedTime, List<T> items) {
    this.elapsedTime = elapsedTime;
    this.items = items;
  }

  public long getElapsedTime() {
    return elapsedTime;
  }

  public List<T> getItems() {
    return items;
  }

  @Override
  public int hashCode() {
    int prime = 31;
    int result = 0;
    result = result * prime + Long.hashCode(elapsedTime);

    for (T item : items) {
      result = result * prime + item.hashCode();
    }

    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if ((obj == null) || (getClass() != obj.getClass())) {
      return false;
    }

    QueryResult<T> queryResult = (QueryResult<T>) obj;
    long itemCount = queryResult.getItems().stream()
        .filter(x -> items.stream()
            .anyMatch(y -> x.equals(y))).count();

    return (elapsedTime == queryResult.getElapsedTime())
        && (items.size() == itemCount);
  }

  @Override
  public String toString() {
    StringBuilder entitiesBuilder = new StringBuilder();
    items.forEach(x -> entitiesBuilder.append(x).append(","));

    return String.format(
        "QueryResult{elapsedTime=%s, entities=%s}",
        elapsedTime, entitiesBuilder);
  }
}
