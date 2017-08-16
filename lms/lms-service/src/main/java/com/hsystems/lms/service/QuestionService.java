package com.hsystems.lms.service;

import com.google.inject.Inject;

import com.hsystems.lms.common.annotation.Log;
import com.hsystems.lms.common.query.Query;
import com.hsystems.lms.common.query.QueryResult;
import com.hsystems.lms.common.security.Principal;
import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.common.util.CommonUtils;
import com.hsystems.lms.common.util.DateTimeUtils;
import com.hsystems.lms.common.util.StringUtils;
import com.hsystems.lms.repository.IndexRepository;
import com.hsystems.lms.repository.QuestionRepository;
import com.hsystems.lms.repository.entity.question.Question;
import com.hsystems.lms.repository.entity.question.QuestionType;
import com.hsystems.lms.service.mapper.Configuration;
import com.hsystems.lms.service.model.QuestionModel;
import com.hsystems.lms.service.model.UserModel;

import java.io.IOException;
import java.time.LocalDateTime;
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
  public QueryResult<QuestionModel> findAllBy(Query query, Principal principal)
      throws IOException {

    addSchoolFilter(query, principal);

    QueryResult<Question> queryResult
        = indexRepository.findAllBy(query, Question.class);

    if (CollectionUtils.isEmpty(queryResult.getItems())) {
      return new QueryResult<>(
          queryResult.getElapsedTime(),
          query.getOffset(),
          query.getLimit(),
          Collections.emptyList()
      );
    }

    Configuration configuration = Configuration.create(principal);
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
      QuestionModel questionModel = getQuestionModel(question, configuration);
      LocalDateTime createdDateTime = question.getCreatedDateTime();
      LocalDateTime modifiedDateTime = question.getModifiedDateTime();

      if (DateTimeUtils.isToday(createdDateTime)) {
        questionModel.setCreatedTime(
            DateTimeUtils.toPrettyTime(createdDateTime));
      }

      if (DateTimeUtils.isNotEmpty(modifiedDateTime)
          && DateTimeUtils.isToday(modifiedDateTime)) {

        questionModel.setModifiedTime(
            DateTimeUtils.toPrettyTime(modifiedDateTime));
      }

      questionModels.add(questionModel);
    }

    return questionModels;
  }

  private QuestionModel getQuestionModel(
      Question question, Configuration configuration) {

    QuestionModel questionModel = getModel(question,
        QuestionModel.class, configuration);
    String dateFormat = configuration.getDateFormat();
    LocalDateTime createdDateTime = question.getCreatedDateTime();
    LocalDateTime modifiedDateTime = question.getModifiedDateTime();

    questionModel.setCreatedDate(
        DateTimeUtils.toString(createdDateTime, dateFormat));

    if (DateTimeUtils.isNotEmpty(modifiedDateTime)) {
      questionModel.setModifiedDate(DateTimeUtils.toString(
          question.getModifiedDateTime(), dateFormat));
    }

    return questionModel;
  }

  @Log
  public Optional<QuestionModel> findBy(String id, Principal principal)
      throws IOException {

    Optional<Question> questionOptional
        = indexRepository.findBy(id, Question.class);

    if (questionOptional.isPresent()) {
      Question question = questionOptional.get();
      Configuration configuration = Configuration.create(principal);
      QuestionModel questionModel = getQuestionModel(question, configuration);
      return Optional.of(questionModel);
    }

    return Optional.empty();
  }

  @Log
  public void create(QuestionModel questionModel, Principal principal)
      throws IOException {

    checkQuestionModel(questionModel);
    questionModel.setCreatedBy((UserModel) principal);
    questionModel.setCreatedDateTime(DateTimeUtils.toString(
        LocalDateTime.now(), principal.getDateTimeFormat()));
    saveQuestion(questionModel, principal);
  }

  private void checkQuestionModel(QuestionModel questionModel) {
    CommonUtils.checkNotNull(questionModel.getBody(),
        "question body cannot be null");
    CommonUtils.checkArgument(StringUtils.isNotEmpty(questionModel.getBody()),
        "question body cannot be empty");
    CommonUtils.checkNotNull(questionModel.getOptions(),
        "question options cannot be null");
    CommonUtils.checkArgument(
        CollectionUtils.isNotEmpty(questionModel.getOptions()),
        "question options cannot be empty");
  }

  private void saveQuestion(QuestionModel questionModel, Principal principal)
      throws IOException {

    Configuration configuration = Configuration.create(principal);
    Question question = getEntity(questionModel, Question.class, configuration);
    questionRepository.save(question);
    indexRepository.save(question);
  }

  @Log
  public void save(QuestionModel questionModel, Principal principal)
      throws IOException {

    checkQuestionModel(questionModel);

    Optional<Question> questionOptional = indexRepository.findBy(
        questionModel.getId(), Question.class);

    if (questionOptional.isPresent()) {
      Question question = questionOptional.get();
      QuestionModel origQuestionModel = getModel(question, QuestionModel.class);
      origQuestionModel.setModifiedBy((UserModel) principal);
      origQuestionModel.setModifiedDateTime(DateTimeUtils.toString(
          LocalDateTime.now(), principal.getDateTimeFormat()));
      copyProperties(origQuestionModel, questionModel);
      saveQuestion(questionModel, principal);
    }
  }

  private void copyProperties(
      QuestionModel destModel, QuestionModel sourceModel) {

    destModel.setType(sourceModel.getType());
    destModel.setBody(sourceModel.getBody());
    destModel.setHint(sourceModel.getHint());
    destModel.setExplanation(sourceModel.getExplanation());
    destModel.setOptions(sourceModel.getOptions());
    destModel.setComponents(sourceModel.getComponents());
    destModel.setSchool(sourceModel.getSchool());
    destModel.setLevels(sourceModel.getLevels());
    destModel.setSubjects(sourceModel.getSubjects());
    destModel.setKeywords(sourceModel.getKeywords());
  }

  @Log
  public void delete(String id, Principal principal)
      throws IOException, IllegalAccessException {

    Optional<Question> questionOptional
        = indexRepository.findBy(id, Question.class);

    if (questionOptional.isPresent()) {
      Question question = questionOptional.get();
      checkDeletePreconditions(question, principal);

      questionRepository.delete(question);
      indexRepository.delete(question);
    }
  }

  private void checkDeletePreconditions(
      Question question, Principal principal) {

    UserModel userModel = (UserModel) principal;
    CommonUtils.checkArgument(question.getCreatedBy()
            .getId().equals(userModel.getId()),
        "current user should be question created user");
  }

  @Log
  public List<QuestionType> findAllTypes() {
    return Arrays.asList(QuestionType.values());
  }
}