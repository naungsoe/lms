package com.hsystems.lms.repository.solr.mapper;

import com.hsystems.lms.common.query.Criterion;
import com.hsystems.lms.common.query.Operator;
import com.hsystems.lms.common.query.Query;
import com.hsystems.lms.common.util.CollectionUtils;
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
public class QueryMapper {

  private static final String DEFAULT_QUERY = "*:*";

  private static final String FIELD_ALL = "*";

  private static final String QUERY_FORMAT = "%s *%s*";
  private static final String BLOCK_JOIN_FORMAT = "{!parent which='%s:(%s)'}";
  private static final String BLOCK_JOIN_QUERY_FORMAT
      = "(fieldName:%s AND %s:%s)";
  private static final String BLOCK_JOIN_FILTER_FORMAT = "%s(%s AND %s)";
  private static final String TRANSFORM_FORMAT
      = "[child parentFilter='%s:(%s)' childFilter='%s' limit=%s]";
  private static final String MAGIC_FIELD_FORMAT = "%s _query_:\"%s\"";
  private static final String FIELD_FORMAT = "%s^%s";
  private static final String FILTER_FORMAT = "%s:(%s)";

  private static final String OR_SEPARATOR = " OR ";
  private static final String NOT_SEPARATOR = "NOT ";

  public QueryMapper() {

  }

  public <T> SolrQuery map(Query query, Class<T> type)
      throws InstantiationException, IllegalAccessException,
      InvocationTargetException, NoSuchFieldException {

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

    if (CollectionUtils.isEmpty(queryCriteria)) {
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

    solrQuery.set("qf", StringUtils.join(fieldNames, " "));

    StringBuilder queryBuilder = new StringBuilder();
    fieldValues.forEach(fieldValue -> {
      String fieldQuery = String.format(
          QUERY_FORMAT, fieldValue, fieldValue);
      queryBuilder.append(fieldQuery);
    });
    solrQuery.setQuery(queryBuilder.toString());
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

    if (CollectionUtils.isEmpty(queryCriteria)) {
      return;
    }

    List<String> fieldQueries = new ArrayList<>();

    for (Criterion criterion : queryCriteria) {
      int lastIndex = criterion.getField().lastIndexOf('.');
      String typeFieldName = criterion.getField().substring(0, lastIndex);
      String fieldName = criterion.getField().substring(lastIndex + 1);
      String fieldValue = criterion.getValues().get(0).toString();
      String fieldQuery = String.format(QUERY_FORMAT, fieldValue, fieldValue);
      fieldQueries.add(String.format(BLOCK_JOIN_QUERY_FORMAT,
          typeFieldName, fieldName, fieldQuery));
    }

    StringBuilder queryBuilder = new StringBuilder();
    String typeName = type.getSimpleName();
    typeName = type.isInterface() ? String.format("*%s", typeName) : typeName;
    queryBuilder.append(String.format(BLOCK_JOIN_FORMAT,
        Constants.FIELD_TYPE_NAME, typeName));
    queryBuilder.append(StringUtils.join(fieldQueries, OR_SEPARATOR));
    solrQuery.setQuery(String.format(MAGIC_FIELD_FORMAT,
        solrQuery.getQuery(), queryBuilder.toString()));
  }

  private Predicate<Criterion> isBlockJoinQueryCriteria() {
    return criterion -> criterion.getOperator() == Operator.LIKE
        && criterion.getField().contains(".");
  }

  private <T> void addMainFilterQuery(
      SolrQuery solrQuery, List<Criterion> criteria, Class<T> type) {

    String typeName = type.getSimpleName();
    typeName = type.isInterface() ? String.format("*%s", typeName) : typeName;
    String typeNameFilter = String.format(FILTER_FORMAT,
        Constants.FIELD_TYPE_NAME, typeName);
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
              StringUtils.prepend(criterion.getValues(), NOT_SEPARATOR));
          solrQuery.addFilterQuery(notEqualFilter);
          break;
        default:
          break;
      }
    });
  }

  private <T> void addBlockJoinFilterQuery(
      SolrQuery solrQuery, List<Criterion> criteria, Class<T> type) {

    String typeName = type.getSimpleName();
    typeName = type.isInterface() ? String.format("*%s", typeName) : typeName;
    String typeNameFilter = String.format(BLOCK_JOIN_FORMAT,
        Constants.FIELD_TYPE_NAME, typeName);

    List<Criterion> queryCriteria = new ArrayList<>();
    criteria.stream().filter(
        criterion -> criterion.getField().contains("."))
        .forEach(queryCriteria::add);

    queryCriteria.forEach(criterion -> {
      int lastIndex = criterion.getField().indexOf('.');
      String fieldName = criterion.getField().substring(lastIndex + 1);
      fieldName = Constants.FIELD_ID.equals(fieldName)
          ? Constants.FIELD_ENTITY_ID : fieldName;
      String memberFieldName = criterion.getField().substring(0, lastIndex);
      String memberFieldFilter = String.format(
          FILTER_FORMAT, Constants.MEMBER_FIELD_NAME, memberFieldName);

      switch (criterion.getOperator()) {
        case EQUAL:
          String equalFilter = String.format(FILTER_FORMAT, fieldName,
              StringUtils.join(criterion.getValues(), OR_SEPARATOR));
          solrQuery.addFilterQuery(String.format(BLOCK_JOIN_FILTER_FORMAT,
              typeNameFilter, memberFieldFilter, equalFilter));
          break;
        case NOT_EQUAL:
          String notEqualFilter = String.format(FILTER_FORMAT, fieldName,
              StringUtils.prepend(criterion.getValues(), NOT_SEPARATOR));
          solrQuery.addFilterQuery(String.format(BLOCK_JOIN_FILTER_FORMAT,
              typeNameFilter, memberFieldFilter, notEqualFilter));
          break;
        default:
          break;
      }
    });
  }

  private <T> void addQueryFields(
      SolrQuery solrQuery, List<String> fieldNames, Class<T> type) {

    List<String> childFieldNames = new ArrayList<>();

    if (CollectionUtils.isEmpty(fieldNames)) {
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
        } else {
          childFieldNames.add(fieldName);
        }
      }
    }

    String childFieldFilter = CollectionUtils.isEmpty(childFieldNames)
        ? "" : String.format(FILTER_FORMAT, Constants.MEMBER_FIELD_NAME,
        StringUtils.join(childFieldNames, OR_SEPARATOR));
    String typeName = type.getSimpleName();
    typeName = type.isInterface() ? String.format("*%s", typeName) : typeName;
    String typeNameField = String.format(TRANSFORM_FORMAT,
        Constants.FIELD_TYPE_NAME, typeName,
        childFieldFilter, Integer.MAX_VALUE);
    solrQuery.addField(typeNameField);
  }
}