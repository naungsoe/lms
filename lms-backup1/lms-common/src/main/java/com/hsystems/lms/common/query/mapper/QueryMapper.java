package com.hsystems.lms.common.query.mapper;

import com.hsystems.lms.common.query.Criterion;
import com.hsystems.lms.common.query.Query;
import com.hsystems.lms.common.query.SortKey;
import com.hsystems.lms.common.query.SortOrder;
import com.hsystems.lms.common.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by naungsoe on 3/11/16.
 */
public final class QueryMapper {

  private static final String PARAM_PATTERN = "%s=([^\\&]*)";
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

  public QueryMapper() {

  }

  public Query map(String queryString) {
    if (StringUtils.isEmpty(queryString)) {
      return new Query();
    }

    List<String> fields = getFields(queryString);
    List<Criterion> criteria = getCriteria(queryString);
    List<SortKey> sortKeys = getSortKeys(queryString);
    long offset = getOffset(queryString);
    long limit = getLimit(queryString);
    return new Query(
        fields,
        criteria,
        sortKeys,
        offset,
        limit
    );
  }

  private List<String> getFields(String queryString) {
    List<String> fields = new ArrayList<>();
    String regex = String.format(PARAM_PATTERN, "fields");
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(queryString);

    while (matcher.find()) {
      String value = matcher.group(1);
      String[] values = value.split(",");
      fields.addAll(Arrays.asList(values));
    }

    return fields;
  }

  private List<Criterion> getCriteria(String queryString) {
    List<Criterion> criteria = new ArrayList<>();
    List<Criterion> queryCriteria = getQueryCriteria(queryString);
    criteria.addAll(queryCriteria);

    List<Criterion> filterCriteria = getFilterCriteria(queryString);
    criteria.addAll(filterCriteria);
    return criteria;
  }

  private List<Criterion> getQueryCriteria(String queryString) {
    List<Criterion> criteria = new ArrayList<>();
    String regex = String.format(PARAM_PATTERN, "query");
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(queryString);

    while (matcher.find()) {
      String value = matcher.group(1);
      addQueryCriteria(criteria, value);
    }

    return criteria;
  }

  private void addQueryCriteria(List<Criterion> criteria, String query) {
    Pattern pattern = Pattern.compile(FILTER_PATTERN);
    Matcher matcher = pattern.matcher(query);

    if (matcher.find()) {
      String[] fields = matcher.group(1).split(",");
      String value = matcher.group(2);

      Arrays.asList(fields).forEach(
          field -> addLikeCriterion(criteria, field, value));
    }
  }

  private List<Criterion> getFilterCriteria(String queryString) {
    List<Criterion> criteria = new ArrayList<>();
    String regex = String.format(PARAM_PATTERN, "filters");
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(queryString);

    while (matcher.find()) {
      String value = matcher.group(1);
      addFilterCriteria(criteria, value);
    }

    return criteria;
  }

  private void addFilterCriteria(List<Criterion> criteria, String query) {
    Pattern pattern = Pattern.compile(FILTER_PATTERN);
    Matcher matcher = pattern.matcher(query);

    while (matcher.find()) {
      String field = matcher.group(1);
      String value = matcher.group(2);
      addEqualCriterion(criteria, field, value);
      addNotEqualCriterion(criteria, field, value);
      addGreaterThanEqualCriterion(criteria, field, value);
      addLessThanEqualCriterion(criteria, field, value);
    }
  }

  private void addEqualCriterion(
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

  private void addNotEqualCriterion(
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

  private void addGreaterThanEqualCriterion(
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

  private void addLessThanEqualCriterion(
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

  private void addLikeCriterion(
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

  private List<SortKey> getSortKeys(String queryString) {
    List<SortKey> sortKeys = new ArrayList<>();
    String regex = String.format(PARAM_PATTERN, "sort");
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(queryString);

    while (matcher.find()) {
      String value = matcher.group(1);
      addSortKeys(sortKeys, value);
    }

    return sortKeys;
  }

  private void addSortKeys(List<SortKey> sortKeys, String query) {
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
  }

  private long getOffset(String queryString) {
    String regex = String.format(PARAM_PATTERN, "offset");
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(queryString);

    if (matcher.find()) {
      String value = matcher.group(1);
      return Integer.valueOf(value);
    }

    return Long.MIN_VALUE;
  }

  private long getLimit(String queryString) {
    String regex = String.format(PARAM_PATTERN, "limit");
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(queryString);

    if (matcher.find()) {
      String value = matcher.group(1);
      return Integer.valueOf(value);
    }

    return Long.MAX_VALUE;
  }
}
