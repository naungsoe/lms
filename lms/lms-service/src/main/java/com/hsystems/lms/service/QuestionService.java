package com.hsystems.lms.service;

import com.google.inject.Inject;
import com.google.inject.Provider;

import com.hsystems.lms.common.annotation.Log;
import com.hsystems.lms.common.annotation.Requires;
import com.hsystems.lms.common.patch.Patch;
import com.hsystems.lms.common.query.Criterion;
import com.hsystems.lms.common.query.Query;
import com.hsystems.lms.common.query.QueryResult;
import com.hsystems.lms.common.security.Principal;
import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.common.util.CommonUtils;
import com.hsystems.lms.common.util.DateTimeUtils;
import com.hsystems.lms.common.util.StringUtils;
import com.hsystems.lms.repository.ComponentRepository;
import com.hsystems.lms.repository.IndexRepository;
import com.hsystems.lms.repository.QuestionRepository;
import com.hsystems.lms.repository.entity.Component;
import com.hsystems.lms.repository.entity.User;
import com.hsystems.lms.repository.entity.beans.ComponentBean;
import com.hsystems.lms.repository.entity.question.CompositeQuestion;
import com.hsystems.lms.repository.entity.question.Question;
import com.hsystems.lms.repository.entity.question.QuestionResource;
import com.hsystems.lms.service.mapper.Configuration;
import com.hsystems.lms.service.model.question.ChoiceOptionModel;
import com.hsystems.lms.service.model.question.MultipleChoiceModel;
import com.hsystems.lms.service.model.question.MultipleChoiceResourceModel;
import com.hsystems.lms.service.model.question.MultipleResponseResourceModel;
import com.hsystems.lms.service.model.question.QuestionModel;
import com.hsystems.lms.service.model.question.QuestionResourceModel;

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
public class QuestionService extends ResourceService {

  private final Provider<Properties> propertiesProvider;

  private final QuestionRepository questionRepository;

  private final ComponentRepository componentRepository;

  private final IndexRepository indexRepository;

  @Inject
  QuestionService(
      Provider<Properties> propertiesProvider,
      QuestionRepository questionRepository,
      ComponentRepository componentRepository,
      IndexRepository indexRepository) {

    this.propertiesProvider = propertiesProvider;
    this.questionRepository = questionRepository;
    this.componentRepository = componentRepository;
    this.indexRepository = indexRepository;
  }

  @Log
  @Requires(AppPermission.VIEW_QUESTION)
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
  @Requires(AppPermission.VIEW_QUESTION)
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