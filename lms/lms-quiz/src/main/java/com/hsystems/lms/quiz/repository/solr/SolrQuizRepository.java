package com.hsystems.lms.quiz.repository.solr;

import com.google.inject.Inject;

import com.hsystems.lms.common.query.Criterion;
import com.hsystems.lms.common.query.Query;
import com.hsystems.lms.common.query.QueryResult;
import com.hsystems.lms.component.repository.solr.SolrComponentRepository;
import com.hsystems.lms.entity.Auditable;
import com.hsystems.lms.entity.Repository;
import com.hsystems.lms.question.repository.entity.QuestionResource;
import com.hsystems.lms.quiz.repository.entity.QuizResource;
import com.hsystems.lms.solr.SolrClient;
import com.hsystems.lms.solr.SolrQueryMapper;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Created by naungsoe on 12/10/16.
 */
public final class SolrQuizRepository
    implements Repository<Auditable<QuizResource>> {

  private static final String QUIZ_COLLECTION = "lms.quizzes";

  private static final String TYPE_NAME_FIELD = "typeName";

  private static final String COMPONENTS_FIELD = "question.components";

  private final SolrClient solrClient;

  private final SolrQueryMapper queryMapper;

  private final SolrComponentRepository componentRepository;

  @Inject
  SolrQuizRepository(
      SolrClient solrClient,
      SolrComponentRepository componentRepository) {

    this.solrClient = solrClient;
    this.componentRepository = componentRepository;

    String typeName = QuizResource.class.getSimpleName();
    this.queryMapper = new SolrQueryMapper(typeName);
  }

  public QueryResult<Auditable<QuizResource>> findAllBy(Query query)
      throws IOException {

    return null;
  }

  private QueryResponse executeQuery(Query query)
      throws IOException {

    String typeName = QuestionResource.class.getSimpleName();
    query.addCriterion(Criterion.createEqual(TYPE_NAME_FIELD, typeName));

    SolrQuery solrQuery = queryMapper.from(query);
    return solrClient.query(solrQuery, QUIZ_COLLECTION);
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