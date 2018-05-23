package com.hsystems.lms.solr;

import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.common.query.Criterion;
import com.hsystems.lms.common.query.Query;
import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.common.util.StringUtils;

import org.apache.solr.client.solrj.SolrQuery;

import java.util.ArrayList;
import java.util.List;

public final class SolrQueryMapper implements Mapper<Query, SolrQuery> {

  private static final String DEFAULT_QUERY = "*:*";

  private static final String FIELD_ALL = "*";

  private static final String QUERY_FORMAT = "%s *%s*";
  private static final String FILTER_FORMAT = "%s:(%s)";

  private static final String OR_SEPARATOR = " OR ";
  private static final String NOT_SEPARATOR = "NOT ";

  public SolrQueryMapper() {

  }

  @Override
  public SolrQuery from(Query source) {
    SolrQuery solrQuery = new SolrQuery();
    solrQuery.set("defType", "edismax");
    addQueryFields(solrQuery, source);
    addQuery(solrQuery, source);
    addFilterQuery(solrQuery, source);

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

    for (String fieldName : fieldNames) {
      solrQuery.addField(fieldName);
    }
  }

  private void addQuery(SolrQuery solrQuery, Query query) {
    List<Criterion> queryCriteria = new ArrayList<>();
    List<Criterion> criteria = query.getCriteria();

    if (CollectionUtils.isEmpty(queryCriteria)) {
      solrQuery.setQuery(DEFAULT_QUERY);
      return;
    }

    List<String> fieldNames = new ArrayList<>();
    List<String> fieldValues = new ArrayList<>();

    for (Criterion criterion : criteria) {
      if (!fieldNames.contains(criterion.getField())) {
        fieldNames.add(criterion.getField());
      }

      List<Object> values = criterion.getValues();

      for (Object value : values) {
        String fieldValue = value.toString();

        if (!fieldValues.contains(fieldValue)) {
          fieldValues.add(fieldValue);
        }
      }
    }

    solrQuery.set("qf", StringUtils.join(fieldNames, " "));

    StringBuilder queryBuilder = new StringBuilder();

    for (String fieldValue : fieldValues) {
      String fieldQuery = String.format(
          QUERY_FORMAT, fieldValue, fieldValue);
      queryBuilder.append(fieldQuery);
    }

    solrQuery.setQuery(queryBuilder.toString());
  }

  private void addFilterQuery(SolrQuery solrQuery, Query query) {
    List<Criterion> criteria = query.getCriteria();

    for (Criterion criterion : criteria) {
      String fieldName = criterion.getField();
      List<Object> values = criterion.getValues();

      switch (criterion.getOperator()) {
        case EQUAL:
          String equalFilter = String.format(FILTER_FORMAT,
              fieldName, StringUtils.join(values, OR_SEPARATOR));
          solrQuery.addFilterQuery(equalFilter);
          break;
        case NOT_EQUAL:
          String notEqualFilter = String.format(FILTER_FORMAT,
              fieldName, StringUtils.prepend(values, NOT_SEPARATOR));
          solrQuery.addFilterQuery(notEqualFilter);
          break;
        default:
          break;
      }
    }
  }
}