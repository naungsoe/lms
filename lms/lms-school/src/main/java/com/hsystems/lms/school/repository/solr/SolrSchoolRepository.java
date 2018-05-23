package com.hsystems.lms.school.repository.solr;

import com.google.inject.Inject;

import com.hsystems.lms.common.query.Criterion;
import com.hsystems.lms.common.query.Query;
import com.hsystems.lms.common.query.QueryResult;
import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.entity.Auditable;
import com.hsystems.lms.entity.Repository;
import com.hsystems.lms.school.repository.entity.School;
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
public final class SolrSchoolRepository
    implements Repository<Auditable<School>> {

  private static final String SCHOOL_COLLECTION = "lms.schools";

  private final SolrClient solrClient;

  private final SolrQueryMapper queryMapper;

  private final SolrSchoolMapper schoolMapper;

  private final SolrSchoolDocMapper schoolDocMapper;

  @Inject
  SolrSchoolRepository(SolrClient solrClient) {
    this.solrClient = solrClient;
    this.queryMapper = new SolrQueryMapper();
    this.schoolMapper = new SolrSchoolMapper();
    this.schoolDocMapper = new SolrSchoolDocMapper();
  }

  public QueryResult<Auditable<School>> findAllBy(Query query)
      throws IOException {

    QueryResponse queryResponse = executeQuery(query);
    SolrDocumentList documentList = queryResponse.getResults();
    long elapsedTime = queryResponse.getElapsedTime();
    long start = documentList.getStart();
    long numFound = documentList.getNumFound();
    List<Auditable<School>> schools = new ArrayList<>();

    for (SolrDocument document : documentList) {
      schools.add(schoolMapper.from(document));
    }

    return new QueryResult<>(elapsedTime, start, numFound, schools);
  }

  private QueryResponse executeQuery(Query query)
      throws IOException {

    SolrQuery solrQuery = queryMapper.from(query);
    return solrClient.query(solrQuery, SCHOOL_COLLECTION);
  }

  @Override
  public Optional<Auditable<School>> findBy(String id)
      throws IOException {

    Query query = Query.create();
    query.addCriterion(Criterion.createEqual("id", id));

    QueryResponse queryResponse = executeQuery(query);
    SolrDocumentList documentList = queryResponse.getResults();

    if (CollectionUtils.isEmpty(documentList)) {
      return Optional.empty();
    }

    SolrDocument document = documentList.get(0);
    return Optional.of(schoolMapper.from(document));
  }

  public void addAll(List<Auditable<School>> entities)
      throws IOException {

    List<SolrInputDocument> documents = new ArrayList<>();

    for (Auditable<School> entity : entities) {
      documents.add(schoolDocMapper.from(entity));
    }

    solrClient.saveAll(documents, SCHOOL_COLLECTION);
  }

  @Override
  public void add(Auditable<School> entity)
      throws IOException {

    SolrInputDocument document = schoolDocMapper.from(entity);
    solrClient.save(document, SCHOOL_COLLECTION);
  }

  @Override
  public void update(Auditable<School> entity)
      throws IOException {

    SolrInputDocument document = schoolDocMapper.from(entity);
    solrClient.save(document, SCHOOL_COLLECTION);
  }

  @Override
  public void remove(Auditable<School> entity)
      throws IOException {

    String id = entity.getEntity().getId();
    solrClient.deleteBy(id, SCHOOL_COLLECTION);
  }
}