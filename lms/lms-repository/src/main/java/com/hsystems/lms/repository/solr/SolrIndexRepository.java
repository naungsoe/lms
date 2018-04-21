package com.hsystems.lms.repository.solr;

import com.google.inject.Inject;

import com.hsystems.lms.common.query.Criterion;
import com.hsystems.lms.common.query.Query;
import com.hsystems.lms.common.query.QueryResult;
import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.repository.IndexRepository;
import com.hsystems.lms.repository.entity.Entity;
import com.hsystems.lms.repository.solr.provider.SolrClient;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Created by naungsoe on 10/8/16.
 */
public class SolrIndexRepository<T extends Entity>
    implements IndexRepository<T> {

  private final SolrClient client;

  @Inject
  SolrIndexRepository(SolrClient client) {
    this.client = client;
  }

  @Override
  public Optional<T> findBy(String id, Class<T> type)
      throws IOException {

    Query query = new Query();
    query.addCriterion(Criterion.createEqual("id", id));
    QueryResult<T> queryResult = client.query(query, type);

    if (CollectionUtils.isEmpty(queryResult.getItems())) {
      return Optional.empty();
    }

    T item = queryResult.getItems().get(0);
    return Optional.of(item);
  }

  @Override
  public QueryResult<T> findAllBy(Query query, Class<T> type)
      throws IOException {

    return client.query(query, type);
  }

  @Override
  public void save(List<T> entities)
      throws IOException {

    if (CollectionUtils.isNotEmpty(entities)) {
      client.index(entities);
    }
  }

  @Override
  public void save(T entity)
      throws IOException {

    client.index(entity);
  }

  @Override
  public void delete(T entity)
      throws IOException {

    client.delete(entity);
  }
}
