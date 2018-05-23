package com.hsystems.lms.level.repository.solr;

import com.google.inject.Inject;

import com.hsystems.lms.common.query.Criterion;
import com.hsystems.lms.common.query.Query;
import com.hsystems.lms.common.query.QueryResult;
import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.entity.Auditable;
import com.hsystems.lms.entity.Repository;
import com.hsystems.lms.level.repository.entity.Level;
import com.hsystems.lms.solr.SolrClient;
import com.hsystems.lms.solr.SolrQueryMapper;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by naungsoe on 12/10/16.
 */
public final class SolrLevelRepository
    implements Repository<Auditable<Level>> {

  private static final String LEVEL_COLLECTION = "lms.levels";

  private final SolrClient solrClient;

  private final SolrQueryMapper queryMapper;

  private final SolrLevelMapper levelMapper;

  private final SolrLevelDocMapper levelDocMapper;

  @Inject
  SolrLevelRepository(SolrClient solrClient) {
    this.solrClient = solrClient;
    this.queryMapper = new SolrQueryMapper();
    this.levelMapper = new SolrLevelMapper();
    this.levelDocMapper = new SolrLevelDocMapper();
  }

  public QueryResult<Auditable<Level>> findAllBy(Query query)
      throws IOException {

    QueryResponse queryResponse = executeQuery(query);
    SolrDocumentList documentList = queryResponse.getResults();
    long elapsedTime = queryResponse.getElapsedTime();
    long start = documentList.getStart();
    long numFound = documentList.getNumFound();
    List<Auditable<Level>> levels = new ArrayList<>();

    for (SolrDocument document : documentList) {
      levels.add(levelMapper.from(document));
    }

    return new QueryResult<>(elapsedTime, start, numFound, levels);
  }

  private QueryResponse executeQuery(Query query)
      throws IOException {

    SolrQuery solrQuery = queryMapper.from(query);
    return solrClient.query(solrQuery, LEVEL_COLLECTION);
  }

  @Override
  public Optional<Auditable<Level>> findBy(String id)
      throws IOException {

    Query query = Query.create();
    query.addCriterion(Criterion.createEqual("id", id));

    QueryResponse queryResponse = executeQuery(query);
    SolrDocumentList documentList = queryResponse.getResults();

    if (CollectionUtils.isEmpty(documentList)) {
      return Optional.empty();
    }

    SolrDocument document = documentList.get(0);
    return Optional.of(levelMapper.from(document));
  }

  public void addAll(List<Auditable<Level>> entities)
      throws IOException {

    List<SolrInputDocument> documents = new ArrayList<>();

    for (Auditable<Level> entity : entities) {
      documents.add(levelDocMapper.from(entity));
    }

    solrClient.saveAll(documents, LEVEL_COLLECTION);
  }

  @Override
  public void add(Auditable<Level> entity)
      throws IOException {

    SolrInputDocument document = levelDocMapper.from(entity);
    solrClient.save(document, LEVEL_COLLECTION);
  }

  @Override
  public void update(Auditable<Level> entity)
      throws IOException {

    SolrInputDocument document = levelDocMapper.from(entity);
    solrClient.save(document, LEVEL_COLLECTION);
  }

  @Override
  public void remove(Auditable<Level> entity)
      throws IOException {

    String id = entity.getEntity().getId();
    solrClient.deleteBy(id, LEVEL_COLLECTION);
  }
}