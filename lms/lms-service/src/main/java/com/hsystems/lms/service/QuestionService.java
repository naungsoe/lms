package com.hsystems.lms.service;

import com.google.inject.Inject;
import com.google.inject.Provider;

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
import com.hsystems.lms.repository.entity.question.QuestionResource;
import com.hsystems.lms.service.mapper.Configuration;
import com.hsystems.lms.service.model.UserModel;
import com.hsystems.lms.service.model.question.ChoiceOptionModel;
import com.hsystems.lms.service.model.question.MultipleChoiceModel;
import com.hsystems.lms.service.model.question.MultipleChoiceResourceModel;
import com.hsystems.lms.service.model.question.QuestionModel;
import com.hsystems.lms.service.model.question.QuestionResourceModel;

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
public class QuestionService extends AbstractService {

  private final Provider<Properties> propertiesProvider;

  private final QuestionRepository questionRepository;

  private final IndexRepository indexRepository;

  @Inject
  QuestionService(
      Provider<Properties> propertiesProvider,
      QuestionRepository questionRepository,
      IndexRepository indexRepository) {

    this.propertiesProvider = propertiesProvider;
    this.questionRepository = questionRepository;
    this.indexRepository = indexRepository;
  }

  @Log
  public QueryResult<QuestionResourceModel> findAllBy(
      Query query, Principal principal)
      throws IOException {

    addSchoolFilter(query, principal);

    QueryResult<QuestionResource> queryResult
        = indexRepository.findAllBy(query, QuestionResource.class);
    List<QuestionResource> questionResources = queryResult.getItems();

    if (CollectionUtils.isEmpty(questionResources)) {
      return new QueryResult<>(
          queryResult.getElapsedTime(),
          query.getOffset(),
          NUMBER_FOUND_ZERO,
          Collections.emptyList()
      );
    }

    Configuration configuration = Configuration.create(principal);
    List<QuestionResourceModel> resourceModels
        = getQuestionResourceModels(questionResources, configuration);
    return new QueryResult<>(
        queryResult.getElapsedTime(),
        queryResult.getStart(),
        queryResult.getNumFound(),
        resourceModels
    );
  }

  private List<QuestionResourceModel> getQuestionResourceModels(
      List<QuestionResource> questionResources, Configuration configuration) {

    List<QuestionResourceModel> resourceModels = new ArrayList<>();

    for (QuestionResource questionResource : questionResources) {
      QuestionResourceModel resourceModel
          = getQuestionResourceModel(questionResource, configuration);
      LocalDateTime createdDateTime = questionResource.getCreatedDateTime();
      LocalDateTime modifiedDateTime = questionResource.getModifiedDateTime();
      String dateFormat = configuration.getDateFormat();

      if (DateTimeUtils.isToday(createdDateTime)) {
        resourceModel.setCreatedTime(
            DateTimeUtils.toPrettyTime(createdDateTime, dateFormat));
      }

      if (DateTimeUtils.isNotEmpty(modifiedDateTime)
          && DateTimeUtils.isToday(modifiedDateTime)) {

        resourceModel.setModifiedTime(
            DateTimeUtils.toPrettyTime(modifiedDateTime, dateFormat));
      }

      resourceModels.add(resourceModel);
    }

    return resourceModels;
  }

  private QuestionResourceModel getQuestionResourceModel(
      QuestionResource questionResource, Configuration configuration) {

    QuestionResourceModel resourceModel = getModel(questionResource,
        QuestionResourceModel.class, configuration);
    String dateFormat = configuration.getDateFormat();
    LocalDateTime createdDateTime = questionResource.getCreatedDateTime();
    LocalDateTime modifiedDateTime = questionResource.getModifiedDateTime();

    resourceModel.setCreatedDate(
        DateTimeUtils.toString(createdDateTime, dateFormat));

    if (DateTimeUtils.isNotEmpty(modifiedDateTime)) {
      resourceModel.setModifiedDate(DateTimeUtils.toString(
          questionResource.getModifiedDateTime(), dateFormat));
    }

    return resourceModel;
  }

  @Log
  public Optional<QuestionResourceModel> findBy(String id, Principal principal)
      throws IOException {

    Optional<QuestionResource> resourceOptional
        = indexRepository.findBy(id, QuestionResource.class);

    if (resourceOptional.isPresent()) {
      QuestionResource questionResource = resourceOptional.get();
      Configuration configuration = Configuration.create(principal);
      QuestionResourceModel resourceModel
          = getQuestionResourceModel(questionResource, configuration);
      return Optional.of(resourceModel);
    }

    return Optional.empty();
  }

  @Log
  public List<String> findAllTypes() {
    Properties properties = propertiesProvider.get();
    String questionTypes = properties.getProperty("question.types");
    return Arrays.asList(questionTypes.split(","));
  }

  @Log
  public void createMultipleChoice(
      MultipleChoiceResourceModel resourceModel,
      Principal principal)
      throws IOException {

    checkQuestionModel(resourceModel);
    populateCreatedByAndDate(resourceModel, principal);
    saveQuestionResource(resourceModel, principal);
  }

  private void checkQuestionModel(
      QuestionResourceModel resourceModel) {

    QuestionModel questionModel = resourceModel.getQuestion();
    String questionBody = questionModel.getBody();
    CommonUtils.checkNotNull(questionBody, "question body cannot be null");
    CommonUtils.checkArgument(StringUtils.isNotEmpty(questionBody),
        "question body cannot be empty");
  }

  private void checkMultipleChoiceModel(
      MultipleChoiceResourceModel resourceModel) {

    MultipleChoiceModel questionModel = resourceModel.getQuestion();
    List<ChoiceOptionModel> optionModels = questionModel.getOptions();
    CommonUtils.checkArgument(CollectionUtils.isEmpty(optionModels),
        "question options cannot be empty");
  }

  private void saveQuestionResource(
      QuestionResourceModel resourceModel, Principal principal)
      throws IOException {

    Configuration configuration = Configuration.create(principal);
    QuestionResource questionResource = getEntity(resourceModel,
        QuestionResource.class, configuration);
    questionRepository.save(questionResource);
    indexRepository.save(questionResource);
  }

  @Log
  public void saveMultipleChoice(
      MultipleChoiceResourceModel resourceModel,
      Principal principal)
      throws IOException {

    checkQuestionModel(resourceModel);
    checkMultipleChoiceModel(resourceModel);

    Optional<QuestionResource> resourceOptional = indexRepository.findBy(
        resourceModel.getId(), QuestionResource.class);

    if (resourceOptional.isPresent()) {
      QuestionResource questionResource = resourceOptional.get();
      MultipleChoiceResourceModel exResourceModel
          = getModel(questionResource, MultipleChoiceResourceModel.class);
      populateChoiceQuestionProperties(exResourceModel, resourceModel);
      populateQuestionProperties(exResourceModel, resourceModel);
      populateModifiedByAndDate(exResourceModel, principal);
      updateQuestionResource(exResourceModel, principal);
    }
  }

  private void populateQuestionProperties(
      QuestionResourceModel destModel,
      QuestionResourceModel sourceModel) {

    QuestionModel destQuestionModel = destModel.getQuestion();
    QuestionModel sourceQuestionModel = sourceModel.getQuestion();
    destQuestionModel.setBody(sourceQuestionModel.getBody());
    destQuestionModel.setHint(sourceQuestionModel.getHint());
    destQuestionModel.setExplanation(sourceQuestionModel.getExplanation());
    destModel.setSchool(sourceModel.getSchool());
    destModel.setLevels(sourceModel.getLevels());
    destModel.setSubjects(sourceModel.getSubjects());
    destModel.setKeywords(sourceModel.getKeywords());
  }

  private void populateChoiceQuestionProperties(
      MultipleChoiceResourceModel destModel,
      MultipleChoiceResourceModel sourceModel) {

    MultipleChoiceModel destQuestionModel = destModel.getQuestion();
    MultipleChoiceModel sourceQuestionModel = sourceModel.getQuestion();
    destQuestionModel.setOptions(sourceQuestionModel.getOptions());
  }

  private void updateQuestionResource(
      QuestionResourceModel resourceModel, Principal principal)
      throws IOException {

    Configuration configuration = Configuration.create(principal);
    QuestionResource questionResource = getEntity(resourceModel,
        QuestionResource.class, configuration);
    questionRepository.save(questionResource);
    indexRepository.save(questionResource);
  }

  @Log
  public void delete(String id, Principal principal)
      throws IOException {

    Optional<QuestionResource> resourceOptional
        = indexRepository.findBy(id, QuestionResource.class);

    if (resourceOptional.isPresent()) {
      QuestionResource questionResource = resourceOptional.get();
      checkDeletePreconditions(questionResource, principal);
      questionRepository.delete(questionResource);
      indexRepository.delete(questionResource);
    }
  }

  private void checkDeletePreconditions(
      QuestionResource questionResource, Principal principal) {

    UserModel userModel = (UserModel) principal;
    boolean createdByUser = userModel.getId().equals(
        questionResource.getCreatedBy().getId());
    CommonUtils.checkArgument(createdByUser,
        "current user should be question created user");
  }
}