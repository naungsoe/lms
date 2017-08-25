package com.hsystems.lms.common.query;

import com.hsystems.lms.common.util.StringUtils;

import java.util.List;

/**
 * Created by naungsoe on 3/11/16.
 */
public class QueryResult<T> {

  private long elapsedTime;

  private long start;

  private long numFound;

  private List<T> items;

  QueryResult() {

  }

  public QueryResult(
      long elapsedTime, long start, long numFound, List<T> items) {

    this.elapsedTime = elapsedTime;
    this.start = start;
    this.numFound = numFound;
    this.items = items;
  }

  public long getElapsedTime() {
    return elapsedTime;
  }

  public long getStart() {
    return start;
  }

  public long getNumFound() {
    return numFound;
  }

  public List<T> getItems() {
    return items;
  }

  @Override
  public int hashCode() {
    int prime = 31;
    int result = 0;
    result = result * prime + Long.hashCode(elapsedTime);
    result = result * prime + Long.hashCode(start);
    result = result * prime + Long.hashCode(numFound);

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
        .filter(itemX -> items.stream()
            .anyMatch(itemY -> itemX.equals(itemY)))
        .count();

    return (elapsedTime == queryResult.getElapsedTime())
        && (start == queryResult.getStart())
        && (numFound == queryResult.getNumFound())
        && (items.size() == itemCount);
  }

  @Override
  public String toString() {
    return String.format(
        "QueryResult{elapsedTime=%s, start=%s, numFound=%s, entities=%s}",
        elapsedTime, start, numFound, StringUtils.join(items, ","));
  }
}
