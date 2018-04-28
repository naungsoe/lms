package com.hsystems.lms.service;

import com.google.inject.Inject;
import com.google.inject.Provider;

import com.hsystems.lms.common.logging.annotation.Log;
import com.hsystems.lms.entity.repository.patch.Patch;
import com.hsystems.lms.entity.repository.query.Criterion;
import com.hsystems.lms.entity.repository.query.Query;
import com.hsystems.lms.entity.repository.query.QueryResult;
import com.hsystems.lms.common.security.Principal;
import com.hsystems.lms.common.security.annotation.Requires;
import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.common.util.CommonUtils;
import com.hsystems.lms.common.util.DateTimeUtils;
import com.hsystems.lms.common.util.StringUtils;
import com.hsystems.lms.question.repository.QuestionRepository;
import com.hsystems.lms.question.repository.entity.QuestionResource;
import com.hsystems.lms.question.service.QuestionPermission;
import com.hsystems.lms.question.service.model.QuestionResourceModel;
import com.hsystems.lms.repository.entity.Component;
import com.hsystems.lms.repository.entity.beans.ComponentBean;
import com.hsystems.lms.repository.entity.question.CompositeQuestion;
import com.hsystems.lms.repository.entity.question.Question;
import com.hsystems.lms.school.repository.entity.School;
import com.hsystems.lms.school.repository.entity.User;
import com.hsystems.lms.service.mapper.Configuration;
import com.hsystems.lms.service.model.question.ChoiceOptionModel;
import com.hsystems.lms.service.model.question.MultipleChoiceModel;
import com.hsystems.lms.service.model.question.MultipleChoiceResourceModel;
import com.hsystems.lms.service.model.question.MultipleResponseResourceModel;
import com.hsystems.lms.service.model.question.QuestionModel;
import com.hsystems.lms.user.repository.entity.SchoolUser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

/**
 * Created by naungsoe on 15/10/16.
 */
public final class QuestionService {

  private static final String FIELD_SCHOOL_ID = "schoolId";

  private static final int NUMBER_FOUND_ZERO = 0;

  private final Provider<Properties> propertiesProvider;

  private final QuestionRepository questionRepository;

  @Inject
  QuestionService(
      Provider<Properties> propertiesProvider,
      QuestionRepository questionRepository) {

    this.propertiesProvider = propertiesProvider;
    this.questionRepository = questionRepository;
  }

  @Log
  @Requires(QuestionPermission.VIEW_QUESTION)
  public QueryResult<QuestionResourceModel> findAllBy(
      Query query, SchoolUser schoolUser)
      throws IOException {

    School school = schoolUser.getSchool();
    Criterion criterion = Criterion.createEqual(
        FIELD_SCHOOL_ID, school.getId());
    query.addCriterion(criterion);

    QueryResult<QuestionResource> queryResult
        = questionRepository.findAllBy(query);
    List<QuestionResource> resources = queryResult.getItems();

    if (CollectionUtils.isEmpty(resources)) {
      return new QueryResult<>(
          queryResult.getElapsedTime(),
          query.getOffset(),
          NUMBER_FOUND_ZERO,
          Collections.emptyList()
      );
    }


    List<QuestionResourceModel> resourceModels
        = getQuestionResourceModels(resources, schoolUser);
    return new QueryResult<>(
        queryResult.getElapsedTime(),
        queryResult.getStart(),
        queryResult.getNumFound(),
        resourceModels
    );
  }

  private List<QuestionResourceModel> getQuestionResourceModels(
      List<QuestionResource> questionResources, SchoolUser schoolUser) {

    List<QuestionResourceModel> resourceModels = new ArrayList<>();

    for (QuestionResource questionResource : questionResources) {
      QuestionResourceModel resourceModel
          = getQuestionResourceModel(questionResource, configuration);
      resourceModels.add(resourceModel);
    }

    return resourceModels;
  }

  private QuestionResourceModel getQuestionResourceModel(
      QuestionResource questionResource, Configuration configuration) {

    QuestionResourceModel resourceModel = getModel(questionResource,
        QuestionResourceModel.class, configuration);
    populateCreatedDateTime(resourceModel, questionResource, configuration);

    if (DateTimeUtils.isNotEmpty(questionResource.getModifiedDateTime())) {
      populateModifiedDateTime(resourceModel, questionResource, configuration);
    }

    return resourceModel;
  }

  @Log
  @Requires(AppPermission.VIEW_QUESTION)
  public Optional<QuestionResourceModel> findBy(String id, Principal principal)
      throws IOException {

    Optional<QuestionResource> resourceOptional
        = indexRepository.findBy(id, QuestionResource.class);

    if (resourceOptional.isPresent()) {
      QuestionResource questionResource = resourceOptional.get();
      Question question = questionResource.getQuestion();

      if (question instanceof CompositeQuestion) {
        CompositeQuestion composite = (CompositeQuestion) question;
        List<Component> components = getComponents(id);
        composite.addComponent(components.toArray(new Component[0]));
      }

      Configuration configuration = Configuration.create(principal);
      QuestionResourceModel resourceModel
          = getQuestionResourceModel(questionResource, configuration);
      return Optional.of(resourceModel);
    }

    return Optional.empty();
  }

  private List<Component> getComponents(String id)
      throws IOException {

    Query query = new Query();
    query.addCriterion(Criterion.createEqual("resourceId", id));
    QueryResult<ComponentBean> queryResult
        = indexRepository.findAllBy(query, ComponentBean.class);
    List<ComponentBean> componentBeans = queryResult.getItems();

    if (CollectionUtils.isEmpty(componentBeans)) {
      return Collections.emptyList();
    }

    List<Component> components = getComponents(componentBeans);
    return components;
  }

  @Log
  @Requires("VIEW_QUESTION")
  public List<String> findAllQuestionTypes() {
    Properties properties = propertiesProvider.get();
    String questionTypes = properties.getProperty("question.question.types");
    return Arrays.asList(questionTypes.split(","));
  }

  @Log
  @Requires(AppPermission.EDIT_QUESTION)
  public void create(
      MultipleChoiceResourceModel resourceModel,
      Principal principal)
      throws IOException {

    checkQuestionModel(resourceModel);
    checkMultipleChoiceModel(resourceModel);
    populateCreatedByAndDate(resourceModel, principal);
    createQuestionResource(resourceModel, principal);
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

  private void createQuestionResource(
      QuestionResourceModel resourceModel, Principal principal)
      throws IOException {

    Configuration configuration = Configuration.create(principal);
    QuestionResource questionResource = getEntity(resourceModel,
        QuestionResource.class, configuration);
    questionRepository.create(questionResource);
    indexRepository.save(questionResource);
  }

  @Log
  @Requires(AppPermission.EDIT_QUESTION)
  public void create(
      MultipleResponseResourceModel resourceModel,
      Principal principal)
      throws IOException {


  }

  @Log
  @Requires(AppPermission.EDIT_QUESTION)
  public void executeUpdate(Patch patch, Principal principal)
      throws IOException {

    User modifiedBy = getUser(principal);
    questionRepository.executeUpdate(patch, modifiedBy);

    String documentId = patch.getDocumentId();
    Optional<QuestionResource> resourceOptional
        = questionRepository.findBy(documentId);

    if (resourceOptional.isPresent()) {
      QuestionResource questionResource = resourceOptional.get();
      indexRepository.save(questionResource);
    }
  }

  @Log
  public void delete(String id, Principal principal)
      throws IOException {

    Optional<QuestionResource> resourceOptional
        = indexRepository.findBy(id, QuestionResource.class);

    if (resourceOptional.isPresent()) {
      QuestionResource questionResource = resourceOptional.get();
      checkDeletePrivilege(questionResource, principal,
          "current user is not owner of the resource");
      questionRepository.delete(questionResource);
      indexRepository.delete(questionResource);
    }
  }
}