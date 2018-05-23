package com.hsystems.lms.lesson.repository.solr;

import com.google.inject.Inject;

import com.hsystems.lms.common.query.Query;
import com.hsystems.lms.common.query.QueryResult;
import com.hsystems.lms.component.Component;
import com.hsystems.lms.component.Nested;
import com.hsystems.lms.component.repository.solr.SolrComponentRepository;
import com.hsystems.lms.entity.Auditable;
import com.hsystems.lms.entity.Repository;
import com.hsystems.lms.lesson.repository.entity.LessonResource;
import com.hsystems.lms.quiz.repository.QuizComponentUtils;
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
public final class SolrLessonRepository
    implements Repository<Auditable<LessonResource>> {

  private static final String LESSON_COLLECTION = "lms.lessons";

  private static final String ID_FIELD = "id";

  private final SolrClient solrClient;

  private final SolrComponentRepository componentRepository;

  private final SolrQueryMapper queryMapper;

  @Inject
  SolrLessonRepository(
      SolrClient solrClient,
      SolrComponentRepository componentRepository) {

    this.solrClient = solrClient;
    this.componentRepository = componentRepository;
    this.queryMapper = new SolrQueryMapper();
  }

  public QueryResult<Auditable<LessonResource>> findAllBy(Query query)
      throws IOException {

    QueryResponse queryResponse = executeQuery(query);
    SolrDocumentList documentList = queryResponse.getResults();
    long elapsedTime = queryResponse.getElapsedTime();
    long start = documentList.getStart();
    long numFound = documentList.getNumFound();
    List<Auditable<LessonResource>> resources = new ArrayList<>();

    for (SolrDocument document : documentList) {
      resources.add(process(document));
    }

    return new QueryResult<>(elapsedTime, start, numFound, resources);
  }

  private QueryResponse executeQuery(Query query)
      throws IOException {

    SolrQuery solrQuery = queryMapper.from(query);
    return solrClient.query(solrQuery, LESSON_COLLECTION);
  }

  private Auditable<LessonResource> process(SolrDocument document)
      throws IOException {

    String id = SolrUtils.getString(document, ID_FIELD);
    SolrLessonComponentMapperFactory mapperFactory
        = new SolrLessonComponentMapperFactory();
    componentRepository.setMapperFactory(mapperFactory);

    List<Nested<Component>> components
        = componentRepository.findAllBy(id);
    List<Component> organizedComponents
        = QuizComponentUtils.organize(id, components);

    SolrLessonResourceMapper resourceMapper
        = new SolrLessonResourceMapper(organizedComponents);
    return resourceMapper.from(document);
  }

  @Override
  public Optional<Auditable<LessonResource>> findBy(String id)
      throws IOException {

    return Optional.empty();
  }

  public void addAll(List<Auditable<LessonResource>> entities)
      throws IOException {

  }

  @Override
  public void add(Auditable<LessonResource> entity)
      throws IOException {

  }

  @Override
  public void update(Auditable<LessonResource> entity)
      throws IOException {

  }

  @Override
  public void remove(Auditable<LessonResource> entity)
      throws IOException {

  }
}