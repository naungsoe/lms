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

  private static final String USER_COLLECTION = "lms.users";

  private static final String TYPE_NAME_FIELD = "typeName";

  private final SolrClient solrClient;

  private final SolrQueryMapper queryMapper;

  private final SolrGroupMapper groupMapper;

  private final SolrGroupDocMapper groupDocMapper;

  @Inject
  SolrGroupRepository(SolrClient solrClient) {
    this.solrClient = solrClient;

    String typeName = Group.class.getSimpleName();
    this.queryMapper = new SolrQueryMapper(typeName);
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
    documentList.forEach(document -> {
      Auditable<Group> user = groupMapper.from(document);
      users.add(user);
    });

    return new QueryResult<>(elapsedTime, start, numFound, users);
  }

  private QueryResponse executeQuery(Query query)
      throws IOException {

    String typeName = Group.class.getSimpleName();
    query.addCriterion(Criterion.createEqual(TYPE_NAME_FIELD, typeName));

    SolrQuery solrQuery = queryMapper.from(query);
    return solrClient.query(solrQuery, USER_COLLECTION);
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
    Auditable<Group> user = groupMapper.from(document);
    return Optional.of(user);
  }

  public void addAll(List<Auditable<Group>> entities)
      throws IOException {

    List<SolrInputDocument> documents = new ArrayList<>();

    for (Auditable<Group> entity : entities) {
      SolrInputDocument document = groupDocMapper.from(entity);
      documents.add(document);
    }

    solrClient.saveAll(documents, USER_COLLECTION);
  }

  @Override
  public void add(Auditable<Group> entity)
      throws IOException {

    SolrInputDocument document = groupDocMapper.from(entity);
    solrClient.save(document, USER_COLLECTION);
  }

  @Override
  public void update(Auditable<Group> entity)
      throws IOException {

    SolrInputDocument document = groupDocMapper.from(entity);
    solrClient.save(document, USER_COLLECTION);
  }

  @Override
  public void remove(Auditable<Group> entity)
      throws IOException {

    String id = entity.getEntity().getId();
    solrClient.deleteBy(id, USER_COLLECTION);
  }
}