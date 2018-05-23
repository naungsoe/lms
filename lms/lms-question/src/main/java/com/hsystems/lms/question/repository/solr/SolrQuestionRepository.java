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
import com.hsystems.lms.question.repository.QuestionComponentUtils;
import com.hsystems.lms.question.repository.entity.CompositeQuestion;
import com.hsystems.lms.question.repository.entity.Question;
import com.hsystems.lms.question.repository.entity.QuestionComponent;
import com.hsystems.lms.question.repository.entity.QuestionResource;
import com.hsystems.lms.solr.SolrClient;
import com.hsystems.lms.solr.SolrQueryMapper;
import com.hsystems.lms.solr.SolrUtils;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;

/**
 * Created by naungsoe on 12/10/16.
 */
public final class SolrQuestionRepository
    implements Repository<Auditable<QuestionResource>> {

  private static final String QUESTION_COLLECTION = "lms.questions";

  private static final String ID_FIELD = "id";

  private final SolrClient solrClient;

  private final SolrComponentRepository componentRepository;

  private final SolrQueryMapper queryMapper;

  @Inject
  SolrQuestionRepository(
      SolrClient solrClient,
      SolrComponentRepository componentRepository) {

    this.solrClient = solrClient;
    this.componentRepository = componentRepository;
    this.queryMapper = new SolrQueryMapper();
  }

  public QueryResult<Auditable<QuestionResource>> findAllBy(Query query)
      throws IOException {

    QueryResponse queryResponse = executeQuery(query);
    SolrDocumentList documentList = queryResponse.getResults();
    long elapsedTime = queryResponse.getElapsedTime();
    long start = documentList.getStart();
    long numFound = documentList.getNumFound();
    List<Auditable<QuestionResource>> resources = new ArrayList<>();

    for (SolrDocument document : documentList) {
      resources.add(process(document));
    }

    return new QueryResult<>(elapsedTime, start, numFound, resources);
  }

  private QueryResponse executeQuery(Query query)
      throws IOException {

    SolrQuery solrQuery = queryMapper.from(query);
    return solrClient.query(solrQuery, QUESTION_COLLECTION);
  }

  private Auditable<QuestionResource> process(SolrDocument document)
      throws IOException {

    String id = SolrUtils.getString(document, ID_FIELD);
    SolrQuestionComponentMapperFactory mapperFactory
        = new SolrQuestionComponentMapperFactory();
    componentRepository.setMapperFactory(mapperFactory);

    List<Nested<Component>> components
        = componentRepository.findAllBy(id);
    List<Component> organizedComponents
        = QuestionComponentUtils.organize(id, components);

    SolrQuestionResourceMapper resourceMapper
        = new SolrQuestionResourceMapper(organizedComponents);
    return resourceMapper.from(document);
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
    return Optional.of(process(document));
  }

  public void addAll(List<Auditable<QuestionResource>> entities)
      throws IOException {

    List<SolrInputDocument> documents = new ArrayList<>();

    for (Auditable<QuestionResource> entity : entities) {
      documents.addAll(process(entity));
    }

    solrClient.saveAll(documents, QUESTION_COLLECTION);
  }

  private List<SolrInputDocument> process(Auditable<QuestionResource> resource)
      throws IOException {

    List<SolrInputDocument> documents = new ArrayList<>();
    SolrQuestionResourceDocMapper resourceDocMapper
        = new SolrQuestionResourceDocMapper();
    documents.add(resourceDocMapper.from(resource));

    QuestionResource entity = resource.getEntity();
    documents.addAll(process(entity));
    return documents;
  }

  private List<SolrInputDocument> process(QuestionResource resource) {
    List<SolrInputDocument> documents = new ArrayList<>();
    Question question = resource.getQuestion();

    if (question instanceof CompositeQuestion) {
      String resourceId = resource.getId();
      SolrQuestionComponentDocMapper componentDocMapper
          = new SolrQuestionComponentDocMapper(resourceId, resourceId);
      CompositeQuestion compositeQuestion = (CompositeQuestion) question;
      Enumeration<QuestionComponent> enumeration
          = compositeQuestion.getComponents();

      while (enumeration.hasMoreElements()) {
        QuestionComponent element = enumeration.nextElement();
        documents.add(componentDocMapper.from(element));
      }
    }

    return documents;
  }

  @Override
  public void add(Auditable<QuestionResource> entity)
      throws IOException {

    List<SolrInputDocument> documents = process(entity);
    solrClient.saveAll(documents, QUESTION_COLLECTION);
  }

  @Override
  public void update(Auditable<QuestionResource> entity)
      throws IOException {

    List<SolrInputDocument> documents = process(entity);
    solrClient.saveAll(documents, QUESTION_COLLECTION);
  }

  @Override
  public void remove(Auditable<QuestionResource> entity)
      throws IOException {

    String id = entity.getEntity().getId();
    solrClient.deleteBy(id, QUESTION_COLLECTION);
  }
}