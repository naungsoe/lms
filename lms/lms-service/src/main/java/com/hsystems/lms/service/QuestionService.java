package com.hsystems.lms.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import com.hsystems.lms.common.annotation.Log;
import com.hsystems.lms.common.query.Criterion;
import com.hsystems.lms.common.query.Query;
import com.hsystems.lms.common.query.QueryResult;
import com.hsystems.lms.repository.AuditLogRepository;
import com.hsystems.lms.repository.IndexRepository;
import com.hsystems.lms.repository.QuestionRepository;
import com.hsystems.lms.repository.ShareLogRepository;
import com.hsystems.lms.repository.entity.Question;
import com.hsystems.lms.service.exception.ServiceException;
import com.hsystems.lms.service.mapper.Configuration;
import com.hsystems.lms.service.mapper.ModelMapper;
import com.hsystems.lms.service.model.QuestionModel;

import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Created by naungsoe on 15/10/16.
 */
@Singleton
public class QuestionService {

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
      ModelMapper mapper = getMapper();

      if (optional.isPresent()) {
        QuestionModel model = mapper.map(
            optional.get(), QuestionModel.class);
        return Optional.of(model);
      }

      return Optional.empty();

    } catch (Exception e) {
      throw new ServiceException("error retrieving question", e);
    }
  }

  private ModelMapper getMapper() {
    Configuration configuration = Configuration.create();
    return new ModelMapper(configuration);
  }

  @Log
  public QueryResult<QuestionModel> findAllBy(Query query)
      throws ServiceException {

    try {
      QueryResult<Question> queryResult = indexRepository.findAllBy(
          query, Question.class);

      if (CollectionUtils.isEmpty(queryResult.getEntities())) {
        return new QueryResult<QuestionModel>(
            queryResult.getElapsedTime(), Collections.EMPTY_LIST);
      }

      ModelMapper mapper = getMapper();
      List<QuestionModel> questionModels = new ArrayList<>();

      for (Question question : queryResult.getEntities()) {
        QuestionModel questionModel = mapper.map(question, QuestionModel.class);
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
