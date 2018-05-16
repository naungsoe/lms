package com.hsystems.lms.common.query;

import java.io.Serializable;
import java.util.List;

/**
 * Created by naungsoe on 3/11/16.
 */
public final class QueryResult<T> implements Serializable {

  private static final long serialVersionUID = -3635361752469270669L;

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
}
