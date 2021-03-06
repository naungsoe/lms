package com.hsystems.lms.user.repository.solr;

import com.google.inject.Inject;

import com.hsystems.lms.common.query.Criterion;
import com.hsystems.lms.common.query.Query;
import com.hsystems.lms.common.query.QueryResult;
import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.entity.Auditable;
import com.hsystems.lms.entity.Repository;
import com.hsystems.lms.solr.SolrClient;
import com.hsystems.lms.solr.SolrQueryMapper;
import com.hsystems.lms.user.repository.entity.AppUser;

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
public final class SolrUserRepository
    implements Repository<Auditable<AppUser>> {

  private static final String USER_COLLECTION = "lms.users";

  private final SolrClient solrClient;

  private final SolrQueryMapper queryMapper;

  private final SolrUserMapper userMapper;

  private final SolrUserDocMapper userDocMapper;

  @Inject
  SolrUserRepository(SolrClient solrClient) {
    this.solrClient = solrClient;
    this.queryMapper = new SolrQueryMapper();
    this.userMapper = new SolrUserMapper();
    this.userDocMapper = new SolrUserDocMapper();
  }

  public QueryResult<Auditable<AppUser>> findAllBy(Query query)
      throws IOException {

    QueryResponse queryResponse = executeQuery(query);
    SolrDocumentList documentList = queryResponse.getResults();
    long elapsedTime = queryResponse.getElapsedTime();
    long start = documentList.getStart();
    long numFound = documentList.getNumFound();
    List<Auditable<AppUser>> users = new ArrayList<>();

    for (SolrDocument document : documentList) {
      users.add(userMapper.from(document));
    }

    return new QueryResult<>(elapsedTime, start, numFound, users);
  }

  private QueryResponse executeQuery(Query query)
      throws IOException {

    SolrQuery solrQuery = queryMapper.from(query);
    return solrClient.query(solrQuery, USER_COLLECTION);
  }

  @Override
  public Optional<Auditable<AppUser>> findBy(String id)
      throws IOException {

    Query query = Query.create();
    query.addCriterion(Criterion.createEqual("id", id));

    QueryResponse queryResponse = executeQuery(query);
    SolrDocumentList documentList = queryResponse.getResults();

    if (CollectionUtils.isEmpty(documentList)) {
      return Optional.empty();
    }

    SolrDocument document = documentList.get(0);
    return Optional.of(userMapper.from(document));
  }

  public void addAll(List<Auditable<AppUser>> entities)
      throws IOException {

    List<SolrInputDocument> documents = new ArrayList<>();

    for (Auditable<AppUser> entity : entities) {
      documents.add(userDocMapper.from(entity));
    }

    solrClient.saveAll(documents, USER_COLLECTION);
  }

  @Override
  public void add(Auditable<AppUser> entity)
      throws IOException {

    SolrInputDocument document = userDocMapper.from(entity);
    solrClient.save(document, USER_COLLECTION);
  }

  @Override
  public void update(Auditable<AppUser> entity)
      throws IOException {

    SolrInputDocument document = userDocMapper.from(entity);
    solrClient.save(document, USER_COLLECTION);
  }

  @Override
  public void remove(Auditable<AppUser> entity)
      throws IOException {

    String id = entity.getEntity().getId();
    solrClient.deleteBy(id, USER_COLLECTION);
  }
}