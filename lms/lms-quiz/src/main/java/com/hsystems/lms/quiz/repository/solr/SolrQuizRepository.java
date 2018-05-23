package com.hsystems.lms.quiz.repository.solr;

import com.google.inject.Inject;

import com.hsystems.lms.common.query.Query;
import com.hsystems.lms.common.query.QueryResult;
import com.hsystems.lms.component.Component;
import com.hsystems.lms.component.Nested;
import com.hsystems.lms.component.repository.solr.SolrComponentRepository;
import com.hsystems.lms.entity.Auditable;
import com.hsystems.lms.entity.Repository;
import com.hsystems.lms.quiz.repository.QuizComponentUtils;
import com.hsystems.lms.quiz.repository.entity.QuizResource;
import com.hsystems.lms.solr.SolrClient;
import com.hsystems.lms.solr.SolrQueryMapper;
import com.hsystems.lms.solr.SolrUtils;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by naungsoe on 12/10/16.
 */
public final class SolrQuizRepository
    implements Repository<Auditable<QuizResource>> {

  private static final String QUIZ_COLLECTION = "lms.quizzes";

  private static final String ID_FIELD = "id";

  private final SolrClient solrClient;

  private final SolrComponentRepository componentRepository;

  private final SolrQueryMapper queryMapper;

  @Inject
  SolrQuizRepository(
      SolrClient solrClient,
      SolrComponentRepository componentRepository) {

    this.solrClient = solrClient;
    this.componentRepository = componentRepository;
    this.queryMapper = new SolrQueryMapper();
  }

  public QueryResult<Auditable<QuizResource>> findAllBy(Query query)
      throws IOException {

    QueryResponse queryResponse = executeQuery(query);
    SolrDocumentList documentList = queryResponse.getResults();
    long elapsedTime = queryResponse.getElapsedTime();
    long start = documentList.getStart();
    long numFound = documentList.getNumFound();
    List<Auditable<QuizResource>> resources = new ArrayList<>();

    for (SolrDocument document : documentList) {
      resources.add(process(document));
    }

    return new QueryResult<>(elapsedTime, start, numFound, resources);
  }

  private QueryResponse executeQuery(Query query)
      throws IOException {

    SolrQuery solrQuery = queryMapper.from(query);
    return solrClient.query(solrQuery, QUIZ_COLLECTION);
  }

  private Auditable<QuizResource> process(SolrDocument document)
      throws IOException {

    String id = SolrUtils.getString(document, ID_FIELD);
    SolrQuizComponentMapperFactory mapperFactory
        = new SolrQuizComponentMapperFactory();
    componentRepository.setMapperFactory(mapperFactory);

    List<Nested<Component>> components
        = componentRepository.findAllBy(id);
    List<Component> organizedComponents
        = QuizComponentUtils.organize(id, components);

    SolrQuizResourceMapper resourceMapper
        = new SolrQuizResourceMapper(organizedComponents);
    return resourceMapper.from(document);
  }

  @Override
  public Optional<Auditable<QuizResource>> findBy(String id)
      throws IOException {

    return Optional.empty();
  }

  public void addAll(List<Auditable<QuizResource>> entities)
      throws IOException {

  }

  @Override
  public void add(Auditable<QuizResource> entity)
      throws IOException {

  }

  @Override
  public void update(Auditable<QuizResource> entity)
      throws IOException {

  }

  @Override
  public void remove(Auditable<QuizResource> entity)
      throws IOException {

  }
}