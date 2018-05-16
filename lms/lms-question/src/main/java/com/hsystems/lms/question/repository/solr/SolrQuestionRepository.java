package com.hsystems.lms.question.repository.solr;

import com.google.inject.Inject;

import com.hsystems.lms.common.query.Criterion;
import com.hsystems.lms.common.query.Query;
import com.hsystems.lms.common.query.QueryResult;
import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.component.Component;
import com.hsystems.lms.component.Nested;
import com.hsystems.lms.component.repository.solr.SolrComponentRepository;
import com.hsystems.lms.entity.Auditable;
import com.hsystems.lms.entity.Repository;
import com.hsystems.lms.question.repository.entity.CompositeQuestion;
import com.hsystems.lms.question.repository.entity.Question;
import com.hsystems.lms.question.repository.entity.QuestionComponent;
import com.hsystems.lms.question.repository.entity.QuestionResource;
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
public final class SolrQuestionRepository
    implements Repository<Auditable<QuestionResource>> {

  private static final String QUESTION_COLLECTION = "lms.questions";

  private static final String TYPE_NAME_FIELD = "typeName";

  private static final String COMPONENTS_FIELD = "question.components";

  private final SolrClient solrClient;

  private final SolrQueryMapper queryMapper;

  private final SolrQuestionResourceMapper questionMapper;

  private final SolrQuestionResourceDocMapper questionDocMapper;

  private final SolrComponentRepository componentRepository;

  @Inject
  SolrQuestionRepository(
      SolrClient solrClient,
      SolrComponentRepository componentRepository) {

    this.solrClient = solrClient;
    this.componentRepository = componentRepository;

    String typeName = QuestionResource.class.getSimpleName();
    this.queryMapper = new SolrQueryMapper(typeName);
    this.questionMapper = new SolrQuestionResourceMapper();
    this.questionDocMapper = new SolrQuestionResourceDocMapper();
  }

  public QueryResult<Auditable<QuestionResource>> findAllBy(Query query)
      throws IOException {

    QueryResponse queryResponse = executeQuery(query);
    SolrDocumentList documentList = queryResponse.getResults();
    long elapsedTime = queryResponse.getElapsedTime();
    long start = documentList.getStart();
    long numFound = documentList.getNumFound();
    List<Auditable<QuestionResource>> resources = new ArrayList<>();
    documentList.forEach(document -> {
      Auditable<QuestionResource> resource = questionMapper.from(document);
      populateComponents(resource);
      resources.add(resource);
    });

    return new QueryResult<>(elapsedTime, start, numFound, resources);
  }

  private QueryResponse executeQuery(Query query)
      throws IOException {

    String typeName = QuestionResource.class.getSimpleName();
    query.addCriterion(Criterion.createEqual(TYPE_NAME_FIELD, typeName));

    SolrQuery solrQuery = queryMapper.from(query);
    return solrClient.query(solrQuery, QUESTION_COLLECTION);
  }

  private void populateComponents(Auditable<QuestionResource> resource) {
    Question question = resource.getEntity().getQuestion();

    if (question instanceof CompositeQuestion) {
      CompositeQuestion compositeQuestion = (CompositeQuestion) question;
      String resourceId = resource.getId();
      SolrQuestionComponentMapper componentMapper
          = new SolrQuestionComponentMapper(resourceId);
      componentRepository.setComponentMapper(componentMapper);

      try {
        List<Nested<Component>> components
            = componentRepository.findAllBy(resourceId);
        components.forEach(component -> {
          QuestionComponent questionComponent
              = (QuestionComponent) component.getComponent();
          compositeQuestion.addComponent(questionComponent);
        });
      } catch (IOException e) {
        throw new IllegalArgumentException(
            "error retrieving components", e);
      }
    }
  }

  @Override
  public Optional<Auditable<QuestionResource>> findBy(String id)
      throws IOException {

    Query query = Query.create();
    query.addCriterion(Criterion.createEqual("id", id));

    QueryResponse queryResponse = executeQuery(query);
    SolrDocumentList documentList = queryResponse.getResults();

    if (CollectionUtils.isEmpty(documentList)) {
      return Optional.empty();
    }

    SolrDocument document = documentList.get(0);
    Auditable<QuestionResource> resource = questionMapper.from(document);
    populateComponents(resource);
    return Optional.of(resource);
  }

  public void addAll(List<Auditable<QuestionResource>> entities)
      throws IOException {

    List<SolrInputDocument> documents = new ArrayList<>();

    for (Auditable<QuestionResource> entity : entities) {
      SolrInputDocument document = questionDocMapper.from(entity);
      documents.add(document);
      addComponents(entity);
    }

    solrClient.saveAll(documents, QUESTION_COLLECTION);
  }

  private void addComponents(Auditable<QuestionResource> resource) {
    Question question = resource.getEntity().getQuestion();

    if (question instanceof CompositeQuestion) {
      CompositeQuestion compositeQuestion = (CompositeQuestion) question;
      String resourceId = resource.getId();
      SolrQuestionComponentDocMapper componentDocMapper
          = new SolrQuestionComponentDocMapper(
              resourceId, resourceId, COMPONENTS_FIELD);
      componentRepository.setComponentDocMapper(componentDocMapper);

      try {
        List<Nested<Component>> components
            = componentRepository.findAllBy(resourceId);
        components.forEach(component -> {
          QuestionComponent questionComponent
              = (QuestionComponent) component.getComponent();
          compositeQuestion.addComponent(questionComponent);
        });
      } catch (IOException e) {
        throw new IllegalArgumentException(
            "error retrieving components", e);
      }
    }
  }

  @Override
  public void add(Auditable<QuestionResource> entity)
      throws IOException {

    SolrInputDocument document = questionDocMapper.from(entity);
    solrClient.save(document, QUESTION_COLLECTION);
    addComponents(entity);

  }

  @Override
  public void update(Auditable<QuestionResource> entity)
      throws IOException {

    SolrInputDocument document = questionDocMapper.from(entity);
    solrClient.save(document, QUESTION_COLLECTION);
    addComponents(entity);
  }

  @Override
  public void remove(Auditable<QuestionResource> entity)
      throws IOException {

    String id = entity.getEntity().getId();
    solrClient.deleteBy(id, QUESTION_COLLECTION);
  }
}