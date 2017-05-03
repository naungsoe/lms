package com.hsystems.lms.common.query;

import com.hsystems.lms.common.util.ListUtils;
import com.hsystems.lms.common.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by naungsoe on 3/11/16.
 */
public class Query {

  private static final String PARAM_PATTERN = "([a-zA-Z0-9]*)=([^\\&]*)";
  private static final String FILTER_PATTERN
      = "([a-zA-Z0-9\\.\\,]*):([a-zA-Z0-9\\_\\s\\'\\\"]*)(\\s|$)";
  private static final String SORT_PATTERN = "([a-zA-Z0-9]*)\\s([a-zA-Z]*)";
  private static final String EQUAL_PATTERN = "MUST\\s(.*)";
  private static final String NOT_EQUAL_PATTERN = "NOT\\s(.*)";
  private static final String GREATER_THAN_EQUAL_PATTERN
      = "FROM\\s([a-zA-Z0-9]*)\\sTO\\s\\*";
  private static final String LESS_THAN_EQUAL_PATTERN
      = "FROM\\s\\*\\sTO\\s([a-zA-Z0-9]*)";
  private static final String LIKE_PATTERN = "LIKE\\s(.*)";

  private List<String> fields;

  private List<Criterion> criteria;

  private List<SortKey> sortKeys;

  private long offset;

  private long limit;

  Query() {
    this.fields = new ArrayList<>();
    this.criteria = new ArrayList<>();
    this.sortKeys =  new ArrayList<>();
    this.offset = 0;
    this.limit = 10;
  }

  public static Query create() {
    return new Query();
  }

  public static Query create(String queryString) {
    Query query = new Query();

    if (StringUtils.isEmpty(queryString)) {
      return query;
    }

    Pattern pattern = Pattern.compile(PARAM_PATTERN);
    Matcher matcher = pattern.matcher(queryString);

    while (matcher.find()) {
      String name = matcher.group(1);
      String value = matcher.group(2);

      switch (name) {
        case "fields":
          query.addField(value.split(","));
          break;
        case "query":
          List<Criterion> queryCriteria = getQueryCriteria(value);
          query.addCriterion(queryCriteria.toArray(new Criterion[0]));
          break;
        case "filters":
          List<Criterion> filterCriteria = getFilterCriteria(value);
          query.addCriterion(filterCriteria.toArray(new Criterion[0]));
          break;
        case "sort":
          List<SortKey> sortKeys = getSortKeys(value);
          query.addSortKey(sortKeys.toArray(new SortKey[0]));
          break;
        case "offset":
          query.offset = Integer.valueOf(value);
          break;
        case "limit":
          query.limit = Integer.valueOf(value);
          break;
        default:
          break;
      }
    }

    return query;
  }

  private static List<Criterion> getQueryCriteria(String query) {
    List<Criterion> criteria = new ArrayList<>();
    Pattern pattern = Pattern.compile(FILTER_PATTERN);
    Matcher matcher = pattern.matcher(query);

    if (matcher.find()) {
      String[] fields = matcher.group(1).split(",");
      String value = matcher.group(2);

      Arrays.asList(fields).forEach(
          field -> populateLike(criteria, field, value));
    }

    return criteria;
  }

  private static List<Criterion> getFilterCriteria(String query) {
    List<Criterion> criteria = new ArrayList<>();
    Pattern pattern = Pattern.compile(FILTER_PATTERN);
    Matcher matcher = pattern.matcher(query);

    while (matcher.find()) {
      String field = matcher.group(1);
      String value = matcher.group(2);
      populateEqual(criteria, field, value);
      populateNotEqual(criteria, field, value);
      populateGreaterThanEqual(criteria, field, value);
      populateLessThanEqual(criteria, field, value);
    }

    return criteria;
  }

  private static void populateEqual(
      List<Criterion> criteria, String field, String query) {

    Pattern pattern = Pattern.compile(EQUAL_PATTERN);
    Matcher matcher = pattern.matcher(query);

    if (matcher.matches()) {
      MatchResult result = matcher.toMatchResult();
      String value = result.group(1);
      Criterion criterion = Criterion.createEqual(field, value);
      criteria.add(criterion);
    }
  }

  private static void populateNotEqual(
      List<Criterion> criteria, String field, String query) {

    Pattern pattern = Pattern.compile(NOT_EQUAL_PATTERN);
    Matcher matcher = pattern.matcher(query);

    if (matcher.matches()) {
      MatchResult result = matcher.toMatchResult();
      String value = result.group(1);
      Criterion criterion = Criterion.createNotEqual(field, value);
      criteria.add(criterion);
    }
  }

  private static void populateGreaterThanEqual(
      List<Criterion> criteria, String field, String query) {

    Pattern pattern = Pattern.compile(GREATER_THAN_EQUAL_PATTERN);
    Matcher matcher = pattern.matcher(query);

    if (matcher.matches()) {
      MatchResult result = matcher.toMatchResult();
      String value = result.group(1);
      Criterion criterion = Criterion.createGreaterThanEqual(field, value);
      criteria.add(criterion);
    }
  }

  private static void populateLessThanEqual(
      List<Criterion> criteria, String field, String query) {

    Pattern pattern = Pattern.compile(LESS_THAN_EQUAL_PATTERN);
    Matcher matcher = pattern.matcher(query);

    if (matcher.matches()) {
      MatchResult result = matcher.toMatchResult();
      String value = result.group(1);
      Criterion criterion = Criterion.createLessThanEqual(field, value);
      criteria.add(criterion);
    }
  }

  private static void populateLike(
      List<Criterion> criteria, String field, String query) {

    Pattern pattern = Pattern.compile(LIKE_PATTERN);
    Matcher matcher = pattern.matcher(query);

    if (matcher.matches()) {
      MatchResult result = matcher.toMatchResult();
      String value = result.group(1);
      Criterion criterion = Criterion.createLike(field, value);
      criteria.add(criterion);
    }
  }

  private static List<SortKey> getSortKeys(String query) {
    List<SortKey> sortKeys = new ArrayList<>();
    String[] params = query.split(",");
    Pattern pattern = Pattern.compile(SORT_PATTERN);

    for (String param : params) {
      Matcher matcher = pattern.matcher(param);

      if (matcher.matches()) {
        MatchResult result = matcher.toMatchResult();
        String field = result.group(1);
        String direction = result.group(2);

        SortOrder sortOrder = "desc".equals(direction)
            ? SortOrder.DESCENDING : SortOrder.ASCENDING;
        sortKeys.add(new SortKey(field, sortOrder));
      }
    }

    return sortKeys;
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
    return ListUtils.equals(criteria, query.getCriteria())
        && ListUtils.equals(sortKeys, query.getSortKeys())
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
