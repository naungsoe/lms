package com.hsystems.lms.common.query;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by naungsoe on 3/11/16.
 */
public final class Query implements Serializable {

  private static final long serialVersionUID = 579403067507001416L;

  private List<String> fields;

  private List<Criterion> criteria;

  private List<SortKey> sortKeys;

  private long offset;

  private long limit;

  Query() {

  }

  Query(
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

  public static Query create() {
    return new Query(
        new ArrayList<>(),
        new ArrayList<>(),
        new ArrayList<>(),
        Long.MIN_VALUE,
        Long.MAX_VALUE
    );
  }

  public static Query create(String query) {
    QueryMapper mapper = new QueryMapper();
    return mapper.from(query);
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
}