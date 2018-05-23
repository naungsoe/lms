package com.hsystems.lms.group.repository.solr;

import com.google.inject.Inject;

import com.hsystems.lms.common.query.Criterion;
import com.hsystems.lms.common.query.Query;
import com.hsystems.lms.common.query.QueryResult;
import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.entity.Auditable;
import com.hsystems.lms.entity.Repository;
import com.hsystems.lms.group.repository.entity.Group;
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
public final class SolrGroupRepository
    implements Repository<Auditable<Group>> {

  private static final String GROUP_COLLECTION = "lms.groups";

  private final SolrClient solrClient;

  private final SolrQueryMapper queryMapper;

  private final SolrGroupMapper groupMapper;

  private final SolrGroupDocMapper groupDocMapper;

  @Inject
  SolrGroupRepository(SolrClient solrClient) {
    this.solrClient = solrClient;
    this.queryMapper = new SolrQueryMapper();
    this.groupMapper = new SolrGroupMapper();
    this.groupDocMapper = new SolrGroupDocMapper();
  }

  public QueryResult<Auditable<Group>> findAllBy(Query query)
      throws IOException {

    QueryResponse queryResponse = executeQuery(query);
    SolrDocumentList documentList = queryResponse.getResults();
    long elapsedTime = queryResponse.getElapsedTime();
    long start = documentList.getStart();
    long numFound = documentList.getNumFound();
    List<Auditable<Group>> users = new ArrayList<>();

    for (SolrDocument document : documentList) {
      users.add(groupMapper.from(document));
    }

    return new QueryResult<>(elapsedTime, start, numFound, users);
  }

  private QueryResponse executeQuery(Query query)
      throws IOException {

    SolrQuery solrQuery = queryMapper.from(query);
    return solrClient.query(solrQuery, GROUP_COLLECTION);
  }

  @Override
  public Optional<Auditable<Group>> findBy(String id)
      throws IOException {

    Query query = Query.create();
    query.addCriterion(Criterion.createEqual("id", id));

    QueryResponse queryResponse = executeQuery(query);
    SolrDocumentList documentList = queryResponse.getResults();

    if (CollectionUtils.isEmpty(documentList)) {
      return Optional.empty();
    }

    SolrDocument document = documentList.get(0);
    return Optional.of(groupMapper.from(document));
  }

  public void addAll(List<Auditable<Group>> entities)
      throws IOException {

    List<SolrInputDocument> documents = new ArrayList<>();

    for (Auditable<Group> entity : entities) {
      documents.add(groupDocMapper.from(entity));
    }

    solrClient.saveAll(documents, GROUP_COLLECTION);
  }

  @Override
  public void add(Auditable<Group> entity)
      throws IOException {

    SolrInputDocument document = groupDocMapper.from(entity);
    solrClient.save(document, GROUP_COLLECTION);
  }

  @Override
  public void update(Auditable<Group> entity)
      throws IOException {

    SolrInputDocument document = groupDocMapper.from(entity);
    solrClient.save(document, GROUP_COLLECTION);
  }

  @Override
  public void remove(Auditable<Group> entity)
      throws IOException {

    String id = entity.getEntity().getId();
    solrClient.deleteBy(id, GROUP_COLLECTION);
  }
}