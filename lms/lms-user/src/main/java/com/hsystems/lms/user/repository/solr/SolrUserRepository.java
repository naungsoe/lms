package com.hsystems.lms.user.repository.solr;

import com.google.inject.Inject;

import com.hsystems.lms.common.query.Criterion;
import com.hsystems.lms.common.query.Query;
import com.hsystems.lms.common.query.QueryResult;
import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.entity.Auditable;
import com.hsystems.lms.entity.Repository;
import com.hsystems.lms.entity.User;
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

  private static final String TYPE_NAME_FIELD = "typeName";

  private final SolrClient solrClient;

  private final SolrQueryMapper queryMapper;

  private final SolrUserMapper userMapper;

  private final SolrUserDocMapper userDocMapper;

  @Inject
  SolrUserRepository(SolrClient solrClient) {
    this.solrClient = solrClient;

    String typeName = User.class.getSimpleName();
    this.queryMapper = new SolrQueryMapper(typeName);
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
    documentList.forEach(document -> {
      Auditable<AppUser> user = userMapper.from(document);
      users.add(user);
    });

    return new QueryResult<>(elapsedTime, start, numFound, users);
  }

  private QueryResponse executeQuery(Query query)
      throws IOException {

    String typeName = User.class.getSimpleName();
    query.addCriterion(Criterion.createEqual(TYPE_NAME_FIELD, typeName));

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
    Auditable<AppUser> user = userMapper.from(document);
    return Optional.of(user);
  }

  public void addAll(List<Auditable<AppUser>> entities)
      throws IOException {

    List<SolrInputDocument> documents = new ArrayList<>();

    for (Auditable<AppUser> entity : entities) {
      SolrInputDocument document = userDocMapper.from(entity);
      documents.add(document);
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