package com.hsystems.lms.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import com.hsystems.lms.common.annotation.Log;
import com.hsystems.lms.common.query.Query;
import com.hsystems.lms.common.query.QueryResult;
import com.hsystems.lms.repository.AuditLogRepository;
import com.hsystems.lms.repository.IndexRepository;
import com.hsystems.lms.repository.QuestionRepository;
import com.hsystems.lms.repository.ShareLogRepository;
import com.hsystems.lms.repository.entity.Question;
import com.hsystems.lms.service.exception.ServiceException;
import com.hsystems.lms.service.model.QuestionModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Created by naungsoe on 15/10/16.
 */
@Singleton
public class QuestionService extends BaseService {

  private QuestionRepository questionRepository;

  private AuditLogRepository auditLogRepository;

  private ShareLogRepository shareLogRepository;

  private IndexRepository indexRepository;

  @Inject
  QuestionService(
      QuestionRepository questionRepository,
      AuditLogRepository auditLogRepository,
      ShareLogRepository shareLogRepository,
      IndexRepository indexRepository) {

    this.questionRepository = questionRepository;
    this.auditLogRepository = auditLogRepository;
    this.auditLogRepository = auditLogRepository;
    this.indexRepository = indexRepository;
  }

  @Log
  public Optional<QuestionModel> findBy(String id)
      throws ServiceException {

    try {
      Optional<Question> optional = questionRepository.findBy(id);

      if (optional.isPresent()) {
        Question question = optional.get();
        QuestionModel model = getModel(question, QuestionModel.class);
        return Optional.of(model);
      }

      return Optional.empty();

    } catch (Exception e) {
      throw new ServiceException("error retrieving question", e);
    }
  }

  @Log
  public QueryResult<QuestionModel> findAllBy(Query query)
      throws ServiceException {

    try {
      QueryResult<Question> queryResult
          = indexRepository.findAllBy(query, Question.class);
      List<Question> questions = queryResult.getEntities();

      if (questions.isEmpty()) {
        return new QueryResult<QuestionModel>(
            queryResult.getElapsedTime(), Collections.emptyList());
      }

      List<QuestionModel> questionModels = new ArrayList<>();

      for (Question question : questions) {
        QuestionModel questionModel = getModel(question, QuestionModel.class);
        questionModels.add(questionModel);
      }

      return new QueryResult<QuestionModel>(
          queryResult.getElapsedTime(), questionModels);

    } catch (Exception e) {
      throw new ServiceException("error retrieving question", e);
    }
  }

  @Log
  public void update(String id)
      throws ServiceException {

    try {
      Optional<Question> optional = questionRepository.findBy(id);

      if (optional.isPresent()) {
        indexRepository.save(optional.get());
      }

    } catch (Exception e) {
      throw new ServiceException("error updating question", e);
    }
  }
}
