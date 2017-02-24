package com.hsystems.lms.common.query;

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

  private List<String> fields;

  private List<Criterion> criteria;

  private List<SortKey> sortKeys;

  private int offset;

  private int limit;

  Query() {
    this.fields = new ArrayList<>();
    this.criteria = new ArrayList<>();
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

    String[] params = queryString.split("&");
    Pattern pattern = Pattern.compile("([a-z0-9]*)(=|!=|>|>=|<|<=|%=)(.*)");

    for (String param : params) {
      Matcher matcher = pattern.matcher(param);

      if (matcher.matches()) {
        MatchResult result = matcher.toMatchResult();
        String name = result.group(1);
        String operator = result.group(2);
        String value = result.group(3);

        switch (name) {
          case "fields":
            query.addFields(Arrays.asList(value.split(",")));
            break;

          case "sort":
            List<SortKey> sortKeys = getSortKeys(value);
            query.addSortKeys(sortKeys);
            break;

          case "offset":
            query.offset = Integer.valueOf(value);
            break;

          case "limit":
            query.limit = Integer.valueOf(value);
            break;

          default:
            Criterion criterion = getCriterion(name, operator, value);
            query.addCriterion(criterion);
            break;
        }
      }
    }

    return query;
  }

  private static List<SortKey> getSortKeys(String query) {
    List<SortKey> sortKeys = new ArrayList<>();
    String[] params = query.split(",");
    Pattern pattern = Pattern.compile("(\\+|\\-)(.*)");

    for (String param : params) {
      Matcher matcher = pattern.matcher(param);

      if (matcher.matches()) {
        MatchResult result = matcher.toMatchResult();
        String operator = result.group(1);
        String field = result.group(2);

        SortOrder sortOrder = "-".equals(operator)
            ? SortOrder.DESCENDING : SortOrder.ASCENDING;
        sortKeys.add(new SortKey(field, sortOrder));
      }
    }

    return sortKeys;
  }

  private static Criterion getCriterion(
      String name, String operator, String value) {

    switch (operator) {
      case "=":
        return Criterion.createEqual(name, value);

      case "!=":
        return Criterion.createNotEqual(name, value);

      case ">":
        return Criterion.createGreaterThan(name, value);

      case ">=":
        return Criterion.createGreaterThanEqual(name, value);

      case "<":
        return Criterion.createLessThan(name, value);

      case "<=":
        return Criterion.createLessThanEqual(name, value);

      case "%=":
        return Criterion.createLike(name, value);

      default:
        return Criterion.createEmpty();
    }
  }

  public List<String> getFields() {
    return Collections.unmodifiableList(fields);
  }

  public void addField(String field) {
    this.fields.add(field);
  }

  public void addFields(List<String> fields) {
    this.fields.addAll(fields);
  }

  public List<Criterion> getCriteria() {
    return Collections.unmodifiableList(criteria);
  }

  public void addCriterion(Criterion criterion) {
    this.criteria.add(criterion);
  }

  public void addCriteria(List<Criterion> criteria) {
    this.criteria.addAll(criteria);
  }

  public List<SortKey> getSortKeys() {
    return Collections.unmodifiableList(sortKeys);
  }

  public void addSortKey(SortKey sortKey) {
    this.sortKeys.add(sortKey);
  }

  public void addSortKeys(List<SortKey> sortKeys) {
    this.sortKeys.addAll(sortKeys);
  }

  public int getOffset() {
    return offset;
  }

  public int getLimit() {
    return limit;
  }

  @Override
  public int hashCode() {
    int prime = 31;
    int result = 0;

    for (String field : fields) {
      result = result * prime + field.hashCode();
    }

    for (Criterion criterion : criteria) {
      result = result * prime + criterion.hashCode();
    }

    for (SortKey sortKey : sortKeys) {
      result = result * prime + sortKey.hashCode();
    }

    result = result * prime + offset;
    return result * prime + limit;
  }

  @Override
  public boolean equals(Object obj) {
    if ((obj == null) || (getClass() != obj.getClass())) {
      return false;
    }

    Query query = (Query) obj;
    long totalFields = query.getFields().stream()
        .filter(field -> fields.stream()
            .anyMatch(y -> field.equals(y))).count();
    long totalCriteria = query.getCriteria().stream()
        .filter(criterion -> criteria.stream()
            .anyMatch(y -> y.equals(criterion))).count();
    long totalSortKeys = query.getSortKeys().stream()
        .filter(sortKey -> sortKeys.stream()
            .anyMatch(y -> y.equals(sortKey))).count();

    return (fields.size() == totalFields)
        && (criteria.size() == totalCriteria)
        && (sortKeys.size() == totalSortKeys)
        && (offset == query.getOffset())
        && (limit == query.getLimit());
  }

  @Override
  public String toString() {
    StringBuilder fieldsBuilder = new StringBuilder();
    fields.forEach(field -> fieldsBuilder.append(field).append(","));

    StringBuilder criteriaBuilder = new StringBuilder();
    criteria.forEach(criterion
        -> criteriaBuilder.append(criterion).append(","));

    StringBuilder sortKeysBuilder = new StringBuilder();
    sortKeys.forEach(sortKey
        -> sortKeysBuilder.append(sortKey).append(","));

    return String.format(
        "Query{fields=%s, criteria=%s, sortKeys=%s, offset=%s, limit=%s}",
        fieldsBuilder, criteriaBuilder, sortKeysBuilder, offset, limit);
  }
}
