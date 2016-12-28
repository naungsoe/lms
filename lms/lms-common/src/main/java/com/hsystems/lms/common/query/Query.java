package com.hsystems.lms.common.query;

import org.apache.commons.lang3.StringUtils;

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

  public static Query create(String queryString) {
    Query query = new Query();

    if (StringUtils.isEmpty(queryString)) {
      return query;
    }

    String[] params = queryString.split("&");
    Pattern pattern = Pattern.compile("([0-9a-z]*)(=|!=|>|>=|<|<=|%=)(.*)");

    for (String param : params) {
      Matcher matcher = pattern.matcher(param);

      if (matcher.matches()) {
        MatchResult result = matcher.toMatchResult();
        String name = result.group(1);
        String operator = result.group(2);
        String value = result.group(3);

        switch (name) {
          case "fields":
            query.addField(value.split(","));
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

  public void addField(String... fields) {
    this.fields.addAll(Arrays.asList(fields));
  }

  public void addFields(List<String> fields) {
    this.fields.addAll(fields);
  }

  public List<Criterion> getCriteria() {
    return Collections.unmodifiableList(criteria);
  }

  public void addCriterion(Criterion... criteria) {
    this.criteria.addAll(Arrays.asList(criteria));
  }

  public void addCriteria(List<Criterion> criteria) {
    this.criteria.addAll(criteria);
  }

  public List<SortKey> getSortKeys() {
    return Collections.unmodifiableList(sortKeys);
  }

  public void addSortKey(SortKey... sortKeys) {
    this.sortKeys.addAll(Arrays.asList(sortKeys));
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

    for (String field: fields) {
      result = result * prime + field.hashCode();
    }

    for (Criterion criterion: criteria) {
      result = result * prime + criterion.hashCode();
    }

    for (SortKey sortKey: sortKeys) {
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
    long fieldCount = query.getFields().stream()
        .filter(x -> fields.stream()
            .anyMatch(y -> x.equals(y))).count();
    long criterionCount = query.getCriteria().stream()
        .filter(x -> criteria.stream()
            .anyMatch(y -> y.equals(x))).count();
    long sortKeyCount = query.getSortKeys().stream()
        .filter(x -> sortKeys.stream()
            .anyMatch(y -> y.equals(x))).count();
    return (fields.size() == fieldCount)
        && (criteria.size() == criterionCount)
        && (sortKeys.size() == sortKeyCount)
        && (offset == query.getOffset())
        && (limit == query.getLimit());
  }

  @Override
  public String toString() {
    StringBuilder fieldsBuilder = new StringBuilder();
    fields.forEach(x -> fieldsBuilder.append(x).append(","));

    StringBuilder criteriaBuilder = new StringBuilder();
    fields.forEach(x -> criteriaBuilder.append(x).append(","));

    StringBuilder sortKeysBuilder = new StringBuilder();
    fields.forEach(x -> sortKeysBuilder.append(x).append(","));

    return String.format(
        "Query{fields=%s, criteria=%s, sortKeys=%s, offset=%s, limit=%s}",
        fieldsBuilder, criteriaBuilder, sortKeysBuilder, offset, limit);
  }
}
