package com.hsystems.lms.service;

import com.google.inject.Inject;

import com.hsystems.lms.common.annotation.Log;
import com.hsystems.lms.common.query.Query;
import com.hsystems.lms.common.query.QueryResult;
import com.hsystems.lms.repository.AuditLogRepository;
import com.hsystems.lms.repository.IndexRepository;
import com.hsystems.lms.repository.QuestionRepository;
import com.hsystems.lms.repository.ShareLogRepository;
import com.hsystems.lms.repository.entity.Question;
import com.hsystems.lms.service.mapper.Configuration;
import com.hsystems.lms.service.model.QuestionModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Created by naungsoe on 15/10/16.
 */
public class QuestionService extends BaseService {

  private final QuestionRepository questionRepository;

  private final AuditLogRepository auditLogRepository;

  private final ShareLogRepository shareLogRepository;

  private final IndexRepository indexRepository;

  @Inject
  QuestionService(
      QuestionRepository questionRepository,
      AuditLogRepository auditLogRepository,
      ShareLogRepository shareLogRepository,
      IndexRepository indexRepository) {

    this.questionRepository = questionRepository;
    this.auditLogRepository = auditLogRepository;
    this.shareLogRepository = shareLogRepository;
    this.indexRepository = indexRepository;
  }

  @Log
  public Optional<QuestionModel> findBy(String id)
      throws IOException {

    return findBy(id, Configuration.create());
  }

  @Log
  public Optional<QuestionModel> findBy(
      String id, Configuration configuration)
      throws IOException {

    Optional<Question> questionOptional = questionRepository.findBy(id);

    if (questionOptional.isPresent()) {
      Question question = questionOptional.get();
      QuestionModel model = getModel(question,
          QuestionModel.class, configuration);
      return Optional.of(model);
    }

    return Optional.empty();
  }

  @Log
  public QueryResult<QuestionModel> findAllBy(Query query)
      throws IOException {

    return findAllBy(query, Configuration.create());
  }

  @Log
  public QueryResult<QuestionModel> findAllBy(
      Query query, Configuration configuration)
      throws IOException {

    QueryResult<Question> queryResult
        = indexRepository.findAllBy(query, Question.class);
    List<Question> questions = queryResult.getEntities();

    if (questions.isEmpty()) {
      return new QueryResult<>(queryResult.getElapsedTime(),
          Collections.emptyList());
    }

    List<QuestionModel> questionModels = new ArrayList<>();

    for (Question question : questions) {
      QuestionModel questionModel = getModel(question,
          QuestionModel.class, configuration);
      questionModels.add(questionModel);
    }

    return new QueryResult<>(
        queryResult.getElapsedTime(), questionModels);
  }

  @Log
  public void update(String id)
      throws IOException {

    Optional<Question> questionOptional
        = questionRepository.findBy(id);

    if (questionOptional.isPresent()) {
      indexRepository.save(questionOptional.get());
    }
  }
}
