package com.hsystems.lms.course.repository.solr;

import com.google.inject.Inject;

import com.hsystems.lms.common.query.Query;
import com.hsystems.lms.common.query.QueryResult;
import com.hsystems.lms.component.Component;
import com.hsystems.lms.component.Nested;
import com.hsystems.lms.component.repository.solr.SolrComponentRepository;
import com.hsystems.lms.course.repository.entity.CourseResource;
import com.hsystems.lms.entity.Auditable;
import com.hsystems.lms.entity.Repository;
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
public final class SolrCourseRepository
    implements Repository<Auditable<CourseResource>> {

  private static final String COURSE_COLLECTION = "lms.courses";

  private static final String ID_FIELD = "id";

  private final SolrClient solrClient;

  private final SolrComponentRepository componentRepository;

  private final SolrQueryMapper queryMapper;

  @Inject
  SolrCourseRepository(
      SolrClient solrClient,
      SolrComponentRepository componentRepository) {

    this.solrClient = solrClient;
    this.componentRepository = componentRepository;
    this.queryMapper = new SolrQueryMapper();
  }

  public QueryResult<Auditable<CourseResource>> findAllBy(Query query)
      throws IOException {

    QueryResponse queryResponse = executeQuery(query);
    SolrDocumentList documentList = queryResponse.getResults();
    long elapsedTime = queryResponse.getElapsedTime();
    long start = documentList.getStart();
    long numFound = documentList.getNumFound();
    List<Auditable<CourseResource>> resources = new ArrayList<>();

    for (SolrDocument document : documentList) {
      resources.add(process(document));
    }

    return new QueryResult<>(elapsedTime, start, numFound, resources);
  }

  private QueryResponse executeQuery(Query query)
      throws IOException {

    SolrQuery solrQuery = queryMapper.from(query);
    return solrClient.query(solrQuery, COURSE_COLLECTION);
  }

  private Auditable<CourseResource> process(SolrDocument document)
      throws IOException {

    String id = SolrUtils.getString(document, ID_FIELD);
    SolrCourseComponentMapperFactory mapperFactory
        = new SolrCourseComponentMapperFactory();
    componentRepository.setMapperFactory(mapperFactory);

    List<Nested<Component>> components
        = componentRepository.findAllBy(id);
    List<Component> organizedComponents
        = QuizComponentUtils.organize(id, components);

    SolrCourseResourceMapper resourceMapper
        = new SolrCourseResourceMapper(organizedComponents);
    return resourceMapper.from(document);
  }

  @Override
  public Optional<Auditable<CourseResource>> findBy(String id)
      throws IOException {

    return Optional.empty();
  }

  public void addAll(List<Auditable<CourseResource>> entities)
      throws IOException {

  }

  @Override
  public void add(Auditable<CourseResource> entity)
      throws IOException {

  }

  @Override
  public void update(Auditable<CourseResource> entity)
      throws IOException {

  }

  @Override
  public void remove(Auditable<CourseResource> entity)
      throws IOException {

  }
}