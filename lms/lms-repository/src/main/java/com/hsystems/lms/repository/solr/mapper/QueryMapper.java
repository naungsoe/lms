package com.hsystems.lms.repository.solr.mapper;

import com.hsystems.lms.common.query.Criterion;
import com.hsystems.lms.common.query.Operator;
import com.hsystems.lms.common.query.Query;
import com.hsystems.lms.common.util.ReflectionUtils;
import com.hsystems.lms.common.util.StringUtils;

import org.apache.solr.client.solrj.SolrQuery;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Created by naungse on 2/12/16.
 */
public class QueryMapper extends Mapper<SolrQuery> {

  protected static final String DEFAULT_QUERY = "*:*";

  private static final String FIELD_ALL = "*";

  private static final String BLOCK_JOIN_FORMAT = "{!parent which=\"%s:%s\"}";
  private static final String BLOCK_JOIN_FIELD_FORMAT = "{!edismax qf=%s}";
  private static final String TRANSFORM_FORMAT
      = "[child parentFilter=\"%s:(%s)\" childFilter=\"%s\"]";
  private static final String MAGIC_FIELD_FORMAT = "%s _query_:\"%s\"";
  private static final String FIELD_FORMAT = "%s^%s";
  private static final String FILTER_FORMAT = "%s:(%s)";
  private static final String OR_SEPARATOR = " OR ";
  private static final String NOT_SEPARATOR = "NOT ";
  private static final String GROUP_FILTER = "(%s)";

  private Class<?> type;

  public QueryMapper(Class<?> type) {
    this.type = type;
  }

  @Override
  public <T> SolrQuery map(T source)
      throws InstantiationException, IllegalAccessException,
      InvocationTargetException, NoSuchFieldException {

    Query query = (Query) source;
    return getSolrQuery(query, type);
  }

  private <T> SolrQuery getSolrQuery(Query query, Class<T> type)
      throws NoSuchFieldException {

    SolrQuery solrQuery = new SolrQuery();
    solrQuery.set("defType", "edismax");
    addQueryFields(solrQuery, query.getFields(), type);
    addMainQuery(solrQuery, query.getCriteria(), type);
    addBlockJoinQuery(solrQuery, query.getCriteria(), type);
    addMainFilterQuery(solrQuery, query.getCriteria(), type);
    addBlockJoinFilterQuery(solrQuery, query.getCriteria(), type);

    solrQuery.setStart((int) query.getOffset());
    solrQuery.setRows((int) query.getLimit());
    return solrQuery;
  }

  private <T> void addMainQuery(
      SolrQuery solrQuery, List<Criterion> criteria, Class<T> type)
      throws NoSuchFieldException {

    List<Criterion> queryCriteria = new ArrayList<>();
    criteria.stream().filter(isQueryCriteria()).forEach(queryCriteria::add);

    if (queryCriteria.isEmpty()) {
      solrQuery.setQuery(DEFAULT_QUERY);
      return;
    }

    List<String> fieldNames = new ArrayList<>();
    List<String> fieldValues = new ArrayList<>();

    for (Criterion criterion : queryCriteria) {
      if (!fieldNames.contains(criterion.getField())) {
        fieldNames.add(criterion.getField());
      }

      criterion.getValues().forEach(value -> {
        String fieldValue = value.toString();

        if (!fieldValues.contains(fieldValue)) {
          fieldValues.add(fieldValue);
        }
      });
    }

    solrQuery.set("qf", StringUtils.join(fieldValues, " "));
    solrQuery.setQuery(String.format(GROUP_FILTER,
        StringUtils.join(fieldValues, " ")));
  }

  private Predicate<Criterion> isQueryCriteria() {
    return criterion -> criterion.getOperator() == Operator.LIKE
        && !criterion.getField().contains(".");
  }

  private <T> void addBlockJoinQuery(
      SolrQuery solrQuery, List<Criterion> criteria, Class<T> type)
      throws NoSuchFieldException {

    List<Criterion> queryCriteria = new ArrayList<>();
    criteria.stream().filter(isBlockJoinQueryCriteria())
        .forEach(queryCriteria::add);

    if (queryCriteria.isEmpty()) {
      return;
    }

    List<String> fieldNames = new ArrayList<>();
    List<String> fieldValues = new ArrayList<>();

    for (Criterion criterion : queryCriteria) {
      int startIndex = criterion.getField().indexOf('.');
      String fieldName = criterion.getField().substring(startIndex);

      if (!fieldNames.contains(fieldName)) {
        fieldNames.add(fieldName);
      }

      criterion.getValues().forEach(value -> {
        String fieldValue = value.toString();

        if (!fieldValues.contains(fieldValue)) {
          fieldValues.add(fieldValue);
        }
      });
    }

    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(String.format(BLOCK_JOIN_FORMAT,
        FIELD_TYPE_NAME, type.getSimpleName()));
    queryBuilder.append(String.format(BLOCK_JOIN_FIELD_FORMAT,
        StringUtils.join(fieldNames, " ")));
    queryBuilder.append(String.format(GROUP_FILTER,
        StringUtils.join(fieldValues, " ")));
    solrQuery.setQuery(String.format(MAGIC_FIELD_FORMAT,
        solrQuery.getQuery(), queryBuilder.toString()));
  }

  private Predicate<Criterion> isBlockJoinQueryCriteria() {
    return criterion -> criterion.getOperator() == Operator.LIKE
        && criterion.getField().contains(".");
  }

  private <T> void addMainFilterQuery(
      SolrQuery solrQuery, List<Criterion> criteria, Class<T> type) {

    String typeNameFilter = String.format(FILTER_FORMAT,
        FIELD_TYPE_NAME, type.getSimpleName());
    solrQuery.addFilterQuery(typeNameFilter);

    List<Criterion> queryCriteria = new ArrayList<>();
    criteria.stream().filter(
        criterion -> !criterion.getField().contains("."))
        .forEach(queryCriteria::add);

    queryCriteria.forEach(criterion -> {
      String fieldName = criterion.getField();

      switch (criterion.getOperator()) {
        case EQUAL:
          String equalFilter = String.format(FILTER_FORMAT, fieldName,
              StringUtils.join(criterion.getValues(), OR_SEPARATOR));
          solrQuery.addFilterQuery(equalFilter);
          break;
        case NOT_EQUAL:
          String notEqualFilter = String.format(FILTER_FORMAT, fieldName,
              StringUtils.join(criterion.getValues(), NOT_SEPARATOR));
          solrQuery.addFilterQuery(notEqualFilter);
          break;
        default:
          break;
      }
    });
  }

  private <T> void addBlockJoinFilterQuery(
      SolrQuery solrQuery, List<Criterion> criteria, Class<T> type) {

    String typeNameFilter = String.format(BLOCK_JOIN_FORMAT,
        FIELD_TYPE_NAME, type.getSimpleName());

    List<Criterion> queryCriteria = new ArrayList<>();
    criteria.stream().filter(
        criterion -> criterion.getField().contains("."))
        .forEach(queryCriteria::add);

    queryCriteria.forEach(criterion -> {
      int startIndex = criterion.getField().indexOf('.') + 1;
      String fieldName = criterion.getField().substring(startIndex);

      switch (criterion.getOperator()) {
        case EQUAL:
          String equalFilter = String.format(FILTER_FORMAT, fieldName,
              StringUtils.join(criterion.getValues(), OR_SEPARATOR));
          solrQuery.addFilterQuery(String.format("%s%s",
              typeNameFilter, equalFilter));
          break;
        case NOT_EQUAL:
          String notEqualFilter = String.format(FILTER_FORMAT, fieldName,
              StringUtils.join(criterion.getValues(), NOT_SEPARATOR));
          solrQuery.addFilterQuery(String.format("%s%s",
              typeNameFilter, notEqualFilter));
          break;
        default:
          break;
      }
    });
  }

  private <T> void addQueryFields(
      SolrQuery solrQuery, List<String> fieldNames, Class<T> type) {

    List<String> childFieldNames = new ArrayList<>();

    if (fieldNames.isEmpty()) {
      solrQuery.addField(FIELD_ALL);

    } else {
      for (String fieldName : fieldNames) {
        Optional<Field> fieldOptional
            = ReflectionUtils.getField(type, fieldName);

        if (fieldOptional.isPresent()) {
          Field field = fieldOptional.get();
          Class<?> fieldType = field.getType();

          if (fieldType.isPrimitive() || fieldType.isEnum()
              || (fieldType == LocalDateTime.class)
              || (fieldType == String.class)) {

            solrQuery.addField(fieldName);

          } else {
            childFieldNames.add(fieldName);
          }
        }
      }
    }

    String childFieldFilter = childFieldNames.isEmpty()
        ? "" : String.format(FILTER_FORMAT, FIELD_NAME,
        StringUtils.join(childFieldNames, OR_SEPARATOR));
    String typeNameField = String.format(TRANSFORM_FORMAT,
        FIELD_TYPE_NAME, type.getSimpleName(), childFieldFilter);
    solrQuery.addField(typeNameField);
  }
}