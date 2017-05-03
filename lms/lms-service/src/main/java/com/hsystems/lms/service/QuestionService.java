package com.hsystems.lms.service;

import com.google.inject.Inject;

import com.hsystems.lms.common.annotation.Log;
import com.hsystems.lms.common.query.Criterion;
import com.hsystems.lms.common.query.Query;
import com.hsystems.lms.common.query.QueryResult;
import com.hsystems.lms.common.util.CommonUtils;
import com.hsystems.lms.repository.IndexRepository;
import com.hsystems.lms.repository.QuestionRepository;
import com.hsystems.lms.repository.entity.Question;
import com.hsystems.lms.repository.entity.QuestionType;
import com.hsystems.lms.repository.entity.User;
import com.hsystems.lms.service.mapper.Configuration;
import com.hsystems.lms.service.model.QuestionModel;
import com.hsystems.lms.service.model.UserModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Created by naungsoe on 15/10/16.
 */
public class QuestionService extends BaseService {

  private final QuestionRepository questionRepository;

  private final IndexRepository indexRepository;

  @Inject
  QuestionService(
      QuestionRepository questionRepository,
      IndexRepository indexRepository) {

    this.questionRepository = questionRepository;
    this.indexRepository = indexRepository;
  }

  @Log
  public Optional<QuestionModel> findBy(
      String id, UserModel userModel)
      throws IOException {

    Optional<Question> questionOptional
        = indexRepository.findBy(id, Question.class);

    if (questionOptional.isPresent()) {
      Question question = questionOptional.get();
      Configuration configuration = Configuration.create(userModel);
      QuestionModel model = getModel(question,
          QuestionModel.class, configuration);
      return Optional.of(model);
    }

    return Optional.empty();
  }

  @Log
  public QueryResult<QuestionModel> findAllBy(
      Query query, UserModel userModel)
      throws IOException {

    String schoolId = userModel.getSchool().getId();
    query.addCriterion(Criterion.createEqual("school.id", schoolId));

    QueryResult<Question> queryResult
        = indexRepository.findAllBy(query, Question.class);

    if (queryResult.getItems().isEmpty()) {
      return new QueryResult<>(
          queryResult.getElapsedTime(),
          query.getOffset(),
          query.getLimit(),
          Collections.emptyList()
      );
    }

    Configuration configuration = Configuration.create(userModel);
    List<Question> questions = queryResult.getItems();
    return new QueryResult<>(
        queryResult.getElapsedTime(),
        queryResult.getStart(),
        queryResult.getNumFound(),
        getQuestionModels(questions, configuration)
    );
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
  public void create(
      QuestionModel questionModel, UserModel userModel)
      throws IOException {

    Configuration configuration = Configuration.create(userModel);
    Question question = getEntity(questionModel, Question.class, configuration);
    questionRepository.save(question);
    indexRepository.save(question);
  }

  @Log
  public void save(
      QuestionModel questionModel, UserModel userModel)
      throws IOException {

    Configuration configuration = Configuration.create(userModel);
    Question question = getEntity(questionModel, Question.class, configuration);
    checkMutatePreconditions(question, userModel);
    questionRepository.save(question);
    indexRepository.save(question);
  }

  @Log
  public void delete(String id, UserModel userModel)
      throws IOException, IllegalAccessException {

    Optional<Question> questionOptional
        = indexRepository.findBy(id, Question.class);

    if (questionOptional.isPresent()) {
      Question question = questionOptional.get();
      checkMutatePreconditions(question, userModel);

      questionRepository.delete(question);
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

  @Log
  public List<QuestionType> findAllTypes() {
    return Arrays.asList(QuestionType.values());
  }
}