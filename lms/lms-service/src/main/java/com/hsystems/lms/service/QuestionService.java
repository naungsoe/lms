package com.hsystems.lms.service;

import com.google.inject.Inject;

import com.hsystems.lms.common.Action;
import com.hsystems.lms.common.annotation.Log;
import com.hsystems.lms.common.query.Query;
import com.hsystems.lms.common.query.QueryResult;
import com.hsystems.lms.common.util.CommonUtils;
import com.hsystems.lms.common.util.DateTimeUtils;
import com.hsystems.lms.repository.IndexRepository;
import com.hsystems.lms.repository.MutateLogRepository;
import com.hsystems.lms.repository.QuestionRepository;
import com.hsystems.lms.repository.entity.EntityType;
import com.hsystems.lms.repository.entity.MutateLog;
import com.hsystems.lms.repository.entity.Question;
import com.hsystems.lms.repository.entity.User;
import com.hsystems.lms.service.mapper.Configuration;
import com.hsystems.lms.service.model.QuestionModel;
import com.hsystems.lms.service.model.UserModel;

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

  private final MutateLogRepository mutateLogRepository;

  private final IndexRepository indexRepository;

  @Inject
  QuestionService(
      QuestionRepository questionRepository,
      MutateLogRepository mutateLogRepository,
      IndexRepository indexRepository) {

    this.questionRepository = questionRepository;
    this.mutateLogRepository = mutateLogRepository;
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

    Optional<MutateLog> mutateLogOptional = mutateLogRepository.findBy(id);

    if (!mutateLogOptional.isPresent()) {
      return Optional.empty();
    }

    MutateLog mutateLog = mutateLogOptional.get();
    Optional<Question> questionOptional
        = questionRepository.findBy(id, mutateLog.getTimestamp());

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

    if (queryResult.getItems().isEmpty()) {
      return new QueryResult<>(queryResult.getElapsedTime(),
          Collections.emptyList());
    }

    List<QuestionModel> questionModels
        = getQuestionModels(queryResult.getItems(), configuration);
    return new QueryResult<>(queryResult.getElapsedTime(), questionModels);
  }

  private List<QuestionModel> getQuestionModels(
      List<Question> questions, Configuration configuration) {

    List<QuestionModel> questionModels = new ArrayList<>();

    for (Question question : questions) {
      QuestionModel questionModel = getModel(question,
          QuestionModel.class, configuration);
      questionModels.add(questionModel);
    }

    return questionModels;
  }

  @Log
  public void create(QuestionModel questionModel)
      throws IOException {

    create(questionModel, Configuration.create());
  }

  @Log
  public void create(
      QuestionModel questionModel, Configuration configuration)
      throws IOException {

    Question question = getEntity(questionModel,
        Question.class, configuration);

    long timestamp = DateTimeUtils.getCurrentMilliseconds();
    MutateLog mutateLog = new MutateLog(question.getId(),
        EntityType.QUESTION, Action.CREATED, timestamp);

    save(question, mutateLog);
  }

  private void save(Question question, MutateLog mutateLog)
      throws IOException {

    try {
      questionRepository.save(question, mutateLog.getTimestamp());
      mutateLogRepository.save(mutateLog, mutateLog.getTimestamp());

    } catch (IOException e) {
      mutateLogRepository.delete(mutateLog, mutateLog.getTimestamp());
      questionRepository.delete(question, mutateLog.getTimestamp());
      throw e;
    }

    indexRepository.save(question);
  }

  @Log
  public void save(QuestionModel questionModel, UserModel userModel)
      throws IOException {

    save(questionModel, userModel, Configuration.create());
  }

  @Log
  public void save(
      QuestionModel questionModel, UserModel userModel,
      Configuration configuration)
      throws IOException {

    Question question = getEntity(questionModel,
        Question.class, configuration);
    checkMutatePreconditions(question, userModel);

    long timestamp = DateTimeUtils.getCurrentMilliseconds();
    MutateLog mutateLog = new MutateLog(question.getId(),
        EntityType.QUESTION, Action.MODIFIED, timestamp);

    save(question, mutateLog);
  }

  @Log
  public void delete(String id, UserModel userModel)
      throws IOException, IllegalAccessException {

    Optional<MutateLog> mutateLogOptional
        = mutateLogRepository.findBy(id, Long.MIN_VALUE);
    CommonUtils.checkArgument(
        mutateLogOptional.isPresent(),
        "error retrieving question");

    MutateLog mutateLog = mutateLogOptional.get();
    Optional<Question> questionOptional
        = questionRepository.findBy(id, mutateLog.getTimestamp());

    if (questionOptional.isPresent()) {
      Question question = questionOptional.get();
      checkMutatePreconditions(question, userModel);

      long timestamp = DateTimeUtils.getCurrentMilliseconds();
      mutateLog = new MutateLog(question.getId(),
          EntityType.QUESTION, Action.DELETED, timestamp);

      mutateLogRepository.save(mutateLog, timestamp);
      indexRepository.delete(question);
    }
  }

  private void checkMutatePreconditions(
      Question question, UserModel userModel) {

    User createdBy = question.getCreatedBy();
    CommonUtils.checkArgument(
        createdBy.getId().equals(userModel.getId()),
        "error mutating question");
  }
}