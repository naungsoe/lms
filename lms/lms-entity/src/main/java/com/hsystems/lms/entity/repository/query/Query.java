package com.hsystems.lms.entity.repository.query;

import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.common.util.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by naungsoe on 3/11/16.
 */
public final class Query implements Serializable {

  private List<String> fields;

  private List<Criterion> criteria;

  private List<SortKey> sortKeys;

  private long offset;

  private long limit;

  public Query() {
    this.fields = new ArrayList<>();
    this.criteria = new ArrayList<>();
    this.sortKeys = new ArrayList<>();
    this.offset = Long.MIN_VALUE;
    this.limit = Long.MAX_VALUE;
  }

  public Query(
      List<String> fields,
      List<Criterion> criteria,
      List<SortKey> sortKeys,
      long offset,
      long limit) {

    this.fields = fields;
    this.criteria = criteria;
    this.sortKeys = sortKeys;
    this.offset = offset;
    this.limit = limit;
  }

  public List<String> getFields() {
    return Collections.unmodifiableList(fields);
  }

  public void addField(String... field) {
    this.fields.addAll(Arrays.asList(field));
  }

  public List<Criterion> getCriteria() {
    return Collections.unmodifiableList(criteria);
  }

  public void addCriterion(Criterion... criterion) {
    this.criteria.addAll(Arrays.asList(criterion));
  }

  public List<SortKey> getSortKeys() {
    return Collections.unmodifiableList(sortKeys);
  }

  public void addSortKey(SortKey... sortKey) {
    this.sortKeys.addAll(Arrays.asList(sortKey));
  }

  public long getOffset() {
    return offset;
  }

  public long getLimit() {
    return limit;
  }

  @Override
  public int hashCode() {
    int prime = 31;
    int result = 0;

    for (Criterion criterion : criteria) {
      result = result * prime + criterion.hashCode();
    }

    for (SortKey sortKey : sortKeys) {
      result = result * prime + sortKey.hashCode();
    }

    result = result * prime + Long.hashCode(offset);
    return result * prime + Long.hashCode(limit);
  }

  @Override
  public boolean equals(Object obj) {
    if ((obj == null) || (getClass() != obj.getClass())) {
      return false;
    }

    Query query = (Query) obj;
    return CollectionUtils.equals(criteria, query.getCriteria())
        && CollectionUtils.equals(sortKeys, query.getSortKeys())
        && (offset == query.getOffset())
        && (limit == query.getLimit());
  }

  @Override
  public String toString() {
    return String.format(
        "Query{criteria=%s, sortKeys=%s, offset=%s, limit=%s}",
        StringUtils.join(criteria, ","), StringUtils.join(sortKeys, ","),
        offset, limit);
  }
}