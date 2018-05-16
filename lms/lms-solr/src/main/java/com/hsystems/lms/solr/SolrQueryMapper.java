package com.hsystems.lms.solr;

import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.common.query.Criterion;
import com.hsystems.lms.common.query.Operator;
import com.hsystems.lms.common.query.Query;
import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.common.util.StringUtils;

import org.apache.solr.client.solrj.SolrQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public final class SolrQueryMapper implements Mapper<Query, SolrQuery> {

  private static final String DEFAULT_QUERY = "*:*";

  private static final String FIELD_ALL = "*";

  private static final String QUERY_FORMAT = "%s *%s*";
  private static final String BLOCK_JOIN_FORMAT = "{!parent which='%s:(%s)'}";
  private static final String BLOCK_JOIN_QUERY_FORMAT
      = "(fieldName:%s AND %s:%s)";
  private static final String BLOCK_JOIN_FILTER_FORMAT = "%s(%s AND %s)";
  private static final String TRANSFORM_FORMAT
      = "[child parentFilter='%s' childFilter='%s' limit=%s]";
  private static final String MAGIC_FIELD_FORMAT = "%s _query_:\"%s\"";
  private static final String FIELD_FORMAT = "%s^%s";
  private static final String FILTER_FORMAT = "%s:(%s)";

  private static final String DOT_SEPARATOR = ".";
  private static final String OR_SEPARATOR = " OR ";
  private static final String NOT_SEPARATOR = "NOT ";

  private static final String ID_FIELD = "id";
  private static final String ENTITY_ID_FIELD = "entityId";
  private static final String TYPE_NAME_FIELD = "typeName";
  private static final String MEMBER_FIELD_NAME = "fieldName";

  private static final int MAX_LIMIT = Integer.MAX_VALUE;

  private final String typeName;

  public SolrQueryMapper(String typeName) {
    this.typeName = typeName;
  }

  @Override
  public SolrQuery from(Query source) {
    SolrQuery solrQuery = new SolrQuery();
    solrQuery.set("defType", "edismax");
    addQueryFields(solrQuery, source);
    addMainQuery(solrQuery, source);
    addBlockJoinQuery(solrQuery, source);
    addMainFilterQuery(solrQuery, source);
    addBlockJoinFilterQuery(solrQuery, source);

    long offset = source.getOffset();
    solrQuery.setStart((int) offset);

    long limit = source.getLimit();
    solrQuery.setRows((int) limit);
    return solrQuery;
  }

  private void addQueryFields(SolrQuery solrQuery, Query query) {
    List<String> fieldNames = query.getFields();

    if (CollectionUtils.isEmpty(fieldNames)) {
      solrQuery.addField(FIELD_ALL);
      return;
    }

    Predicate<String> withoutDotNotation
        = fieldName -> (fieldName.indexOf(DOT_SEPARATOR) == -1);
    fieldNames.stream().filter(withoutDotNotation)
        .forEach(fieldName -> solrQuery.addField(fieldName));

    Predicate<String> withDotNotation
        = fieldName -> (fieldName.indexOf(DOT_SEPARATOR) == -1);
    List<String> childFieldNames = fieldNames.stream()
        .filter(withDotNotation).collect(Collectors.toList());

    if (CollectionUtils.isNotEmpty(childFieldNames)) {
      String parentFilter = String.format(FILTER_FORMAT,
          TYPE_NAME_FIELD, typeName);
      String childFilter = CollectionUtils.isEmpty(childFieldNames)
          ? "" : String.format(FILTER_FORMAT, MEMBER_FIELD_NAME,
          StringUtils.join(childFieldNames, OR_SEPARATOR));
      String typeNameField = String.format(TRANSFORM_FORMAT,
          parentFilter, childFilter, MAX_LIMIT);
      solrQuery.addField(typeNameField);
    }
  }

  private void addMainQuery(SolrQuery solrQuery, Query query) {
    List<Criterion> queryCriteria = new ArrayList<>();
    List<Criterion> criteria = query.getCriteria();
    Predicate<Criterion> queryCriterion =
        criterion -> (criterion.getOperator() == Operator.LIKE)
            && !criterion.getField().contains(DOT_SEPARATOR);
    criteria.stream().filter(queryCriterion)
        .forEach(queryCriteria::add);

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

  private void addBlockJoinQuery(SolrQuery solrQuery, Query query) {
    List<Criterion> queryCriteria = new ArrayList<>();
    List<Criterion> criteria = query.getCriteria();
    Predicate<Criterion> blockJoinQueryCriteria
        = criterion -> (criterion.getOperator() == Operator.LIKE)
        && criterion.getField().contains(DOT_SEPARATOR);
    criteria.stream().filter(blockJoinQueryCriteria)
        .forEach(queryCriteria::add);

    if (CollectionUtils.isEmpty(queryCriteria)) {
      return;
    }

    List<String> fieldQueries = new ArrayList<>();

    for (Criterion criterion : queryCriteria) {
      int lastIndex = criterion.getField().lastIndexOf(DOT_SEPARATOR);
      String typeFieldName = criterion.getField().substring(0, lastIndex);
      String fieldName = criterion.getField().substring(lastIndex + 1);
      String fieldValue = criterion.getValues().get(0).toString();
      String fieldQuery = String.format(QUERY_FORMAT, fieldValue, fieldValue);
      fieldQueries.add(String.format(BLOCK_JOIN_QUERY_FORMAT,
          typeFieldName, fieldName, fieldQuery));
    }

    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(String.format(BLOCK_JOIN_FORMAT,
        TYPE_NAME_FIELD, typeName));
    queryBuilder.append(StringUtils.join(fieldQueries, OR_SEPARATOR));
    solrQuery.setQuery(String.format(MAGIC_FIELD_FORMAT,
        solrQuery.getQuery(), queryBuilder.toString()));
  }

  private void addMainFilterQuery(SolrQuery solrQuery, Query query) {
    String typeNameFilter = String.format(FILTER_FORMAT,
        TYPE_NAME_FIELD, typeName);
    solrQuery.addFilterQuery(typeNameFilter);

    List<Criterion> queryCriteria = new ArrayList<>();
    List<Criterion> criteria = query.getCriteria();
    Predicate<Criterion> queryCriterion
        = criterion -> !criterion.getField().contains(DOT_SEPARATOR);
    criteria.stream().filter(queryCriterion)
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

  private void addBlockJoinFilterQuery(SolrQuery solrQuery, Query query) {
    String typeNameFilter = String.format(BLOCK_JOIN_FORMAT,
        TYPE_NAME_FIELD, typeName);

    List<Criterion> queryCriteria = new ArrayList<>();
    List<Criterion> criteria = query.getCriteria();
    criteria.stream().filter(
        criterion -> criterion.getField().contains(DOT_SEPARATOR))
        .forEach(queryCriteria::add);

    queryCriteria.forEach(criterion -> {
      int lastIndex = criterion.getField().indexOf('.');
      String fieldName = criterion.getField().substring(lastIndex + 1);
      fieldName = ID_FIELD.equals(fieldName) ? ENTITY_ID_FIELD : fieldName;
      String memberFieldName = criterion.getField().substring(0, lastIndex);
      String memberFieldFilter = String.format(
          FILTER_FORMAT, MEMBER_FIELD_NAME, memberFieldName);

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
}