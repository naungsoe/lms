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
import com.hsystems.lms.repository.entity.question.CompositeQuestion;
import com.hsystems.lms.repository.entity.question.MultipleChoice;
import com.hsystems.lms.repository.entity.question.Question;
import com.hsystems.lms.service.mapper.Configuration;
import com.hsystems.lms.service.model.CompositeQuestionModel;
import com.hsystems.lms.service.model.MultipleChoiceModel;
import com.hsystems.lms.service.model.QuestionComponentModel;
import com.hsystems.lms.service.model.QuestionModel;
import com.hsystems.lms.service.model.QuestionOptionModel;
import com.hsystems.lms.service.model.UserModel;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

/**
 * Created by naungsoe on 15/10/16.
 */
public class QuestionService extends BaseService {

  private final Properties properties;

  private final QuestionRepository questionRepository;

  private final IndexRepository indexRepository;

  @Inject
  QuestionService(
      Properties properties,
      QuestionRepository questionRepository,
      IndexRepository indexRepository) {

    this.properties = properties;
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
  public void create(
      CompositeQuestionModel questionModel, Principal principal)
      throws IOException {

    checkQuestionModel(questionModel);
    checkCompositeQuestionModel(questionModel);
    populateCreatedByAndDate(questionModel, principal);
    saveQuestion(questionModel, principal, CompositeQuestion.class);
  }

  private void checkQuestionModel(QuestionModel questionModel) {
    String questionBody = questionModel.getBody();
    CommonUtils.checkNotNull(questionBody, "question body cannot be null");
    CommonUtils.checkArgument(StringUtils.isNotEmpty(questionBody),
        "question body cannot be empty");
  }

  private void checkCompositeQuestionModel(
      CompositeQuestionModel questionModel) {

    List<QuestionComponentModel> models = questionModel.getComponents();
    CommonUtils.checkNotNull(models, "question components cannot be null");
    CommonUtils.checkArgument(CollectionUtils.isNotEmpty(models),
        "question components cannot be empty");
  }

  private void populateCreatedByAndDate(
      QuestionModel questionModel, Principal principal) {

    String dateTime = DateTimeUtils.toString(LocalDateTime.now(),
        principal.getDateTimeFormat());
    questionModel.setCreatedBy((UserModel) principal);
    questionModel.setCreatedDateTime(dateTime);
  }

  private <T extends Question> void saveQuestion(
      QuestionModel questionModel, Principal principal, Class<T> type)
      throws IOException {

    Configuration configuration = Configuration.create(principal);
    T question = getEntity(questionModel, type, configuration);
    questionRepository.save(question);
    indexRepository.save(question);
  }

  @Log
  public void create(
      MultipleChoiceModel questionModel, Principal principal)
      throws IOException {

    checkQuestionModel(questionModel);
    checkMultipleChoiceModel(questionModel);
    populateCreatedByAndDate(questionModel, principal);
    saveQuestion(questionModel, principal, MultipleChoice.class);
  }

  private void checkMultipleChoiceModel(MultipleChoiceModel questionModel) {
    List<QuestionOptionModel> models = questionModel.getOptions();
    CommonUtils.checkNotNull(models, "question options cannot be null");
    CommonUtils.checkArgument(CollectionUtils.isNotEmpty(models),
        "question options cannot be empty");
  }

  @Log
  public void save(
      CompositeQuestionModel questionModel, Principal principal)
      throws IOException {

    checkQuestionModel(questionModel);
    checkCompositeQuestionModel(questionModel);

    Optional<CompositeQuestion> questionOptional = indexRepository.findBy(
        questionModel.getId(), CompositeQuestion.class);

    if (questionOptional.isPresent()) {
      CompositeQuestion question = questionOptional.get();
      CompositeQuestionModel exModel
          = getModel(question, CompositeQuestionModel.class);
      copyQuestionProperties(exModel, questionModel);
      populateModifiedByAndDate(exModel, principal);
      saveQuestion(exModel, principal, CompositeQuestion.class);
    }
  }

  private void copyQuestionProperties(
      QuestionModel destModel, QuestionModel sourceModel) {

    destModel.setBody(sourceModel.getBody());
    destModel.setHint(sourceModel.getHint());
    destModel.setExplanation(sourceModel.getExplanation());
    destModel.setSchool(sourceModel.getSchool());
    destModel.setLevels(sourceModel.getLevels());
    destModel.setSubjects(sourceModel.getSubjects());
    destModel.setKeywords(sourceModel.getKeywords());
  }

  private void populateModifiedByAndDate(
      QuestionModel questionModel, Principal principal) {

    questionModel.setModifiedBy((UserModel) principal);
    questionModel.setModifiedDateTime(DateTimeUtils.toString(
        LocalDateTime.now(), principal.getDateTimeFormat()));
  }

  @Log
  public void save(
      MultipleChoiceModel questionModel, Principal principal)
      throws IOException {

    checkQuestionModel(questionModel);
    checkMultipleChoiceModel(questionModel);

    Optional<MultipleChoice> questionOptional = indexRepository.findBy(
        questionModel.getId(), MultipleChoice.class);

    if (questionOptional.isPresent()) {
      MultipleChoice question = questionOptional.get();
      MultipleChoiceModel exModel
          = getModel(question, MultipleChoiceModel.class);
      copyQuestionProperties(exModel, questionModel);
      populateModifiedByAndDate(exModel, principal);
      saveQuestion(exModel, principal, MultipleChoice.class);
    }
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
    boolean createdByUser = question.getCreatedBy()
        .getId().equals(userModel.getId());
    CommonUtils.checkArgument(createdByUser,
        "current user should be question created user");
  }

  @Log
  public List<String> findAllTypes() {
    String questionTypes = properties.getProperty("question.types");
    return Arrays.asList(questionTypes.split(","));
  }
}