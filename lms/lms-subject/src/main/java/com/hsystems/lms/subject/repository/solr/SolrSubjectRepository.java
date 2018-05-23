package com.hsystems.lms.subject.repository.solr;

import com.google.inject.Inject;

import com.hsystems.lms.common.query.Criterion;
import com.hsystems.lms.common.query.Query;
import com.hsystems.lms.common.query.QueryResult;
import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.entity.Auditable;
import com.hsystems.lms.entity.Repository;
import com.hsystems.lms.solr.SolrClient;
import com.hsystems.lms.solr.SolrQueryMapper;
import com.hsystems.lms.subject.repository.entity.Subject;

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
public final class SolrSubjectRepository
    implements Repository<Auditable<Subject>> {

  private static final String SUBJECT_COLLECTION = "lms.subjects";

  private final SolrClient solrClient;

  private final SolrQueryMapper queryMapper;

  private final SolrSubjectMapper subjectMapper;

  private final SolrSubjectDocMapper subjectDocMapper;

  @Inject
  SolrSubjectRepository(SolrClient solrClient) {
    this.solrClient = solrClient;
    this.queryMapper = new SolrQueryMapper();
    this.subjectMapper = new SolrSubjectMapper();
    this.subjectDocMapper = new SolrSubjectDocMapper();
  }

  public QueryResult<Auditable<Subject>> findAllBy(Query query)
      throws IOException {

    QueryResponse queryResponse = executeQuery(query);
    SolrDocumentList documentList = queryResponse.getResults();
    long elapsedTime = queryResponse.getElapsedTime();
    long start = documentList.getStart();
    long numFound = documentList.getNumFound();
    List<Auditable<Subject>> subjects = new ArrayList<>();

    for (SolrDocument document : documentList) {
      subjects.add(subjectMapper.from(document));
    }

    return new QueryResult<>(elapsedTime, start, numFound, subjects);
  }

  private QueryResponse executeQuery(Query query)
      throws IOException {

    SolrQuery solrQuery = queryMapper.from(query);
    return solrClient.query(solrQuery, SUBJECT_COLLECTION);
  }

  @Override
  public Optional<Auditable<Subject>> findBy(String id)
      throws IOException {

    Query query = Query.create();
    query.addCriterion(Criterion.createEqual("id", id));

    QueryResponse queryResponse = executeQuery(query);
    SolrDocumentList documentList = queryResponse.getResults();

    if (CollectionUtils.isEmpty(documentList)) {
      return Optional.empty();
    }

    SolrDocument document = documentList.get(0);
    return Optional.of(subjectMapper.from(document));
  }

  public void addAll(List<Auditable<Subject>> entities)
      throws IOException {

    List<SolrInputDocument> documents = new ArrayList<>();

    for (Auditable<Subject> entity : entities) {
      documents.add(subjectDocMapper.from(entity));
    }

    solrClient.saveAll(documents, SUBJECT_COLLECTION);
  }

  @Override
  public void add(Auditable<Subject> entity)
      throws IOException {

    SolrInputDocument document = subjectDocMapper.from(entity);
    solrClient.save(document, SUBJECT_COLLECTION);
  }

  @Override
  public void update(Auditable<Subject> entity)
      throws IOException {

    SolrInputDocument document = subjectDocMapper.from(entity);
    solrClient.save(document, SUBJECT_COLLECTION);
  }

  @Override
  public void remove(Auditable<Subject> entity)
      throws IOException {

    String id = entity.getEntity().getId();
    solrClient.deleteBy(id, SUBJECT_COLLECTION);
  }
}