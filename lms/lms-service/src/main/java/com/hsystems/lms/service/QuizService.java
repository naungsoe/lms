package com.hsystems.lms.service;

import com.google.inject.Inject;
import com.google.inject.Provider;

import com.hsystems.lms.common.annotation.Log;
import com.hsystems.lms.common.query.Criterion;
import com.hsystems.lms.common.query.Query;
import com.hsystems.lms.common.query.QueryResult;
import com.hsystems.lms.common.security.Principal;
import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.common.util.DateTimeUtils;
import com.hsystems.lms.repository.IndexRepository;
import com.hsystems.lms.repository.QuizRepository;
import com.hsystems.lms.repository.entity.Component;
import com.hsystems.lms.repository.entity.beans.ComponentBean;
import com.hsystems.lms.repository.entity.quiz.Quiz;
import com.hsystems.lms.repository.entity.quiz.QuizResource;
import com.hsystems.lms.service.mapper.Configuration;
import com.hsystems.lms.service.model.quiz.QuizResourceModel;

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
public class QuizService extends ResourceService {

  private final Provider<Properties> propertiesProvider;

  private final QuizRepository quizRepository;

  private final IndexRepository indexRepository;

  @Inject
  QuizService(
      Provider<Properties> propertiesProvider,
      QuizRepository quizRepository,
      IndexRepository indexRepository) {

    this.propertiesProvider = propertiesProvider;
    this.quizRepository = quizRepository;
    this.indexRepository = indexRepository;
  }

  @Log
  public QueryResult<QuizResourceModel> findAllBy(
      Query query, Principal principal)
      throws IOException {

    addSchoolFilter(query, principal);

    QueryResult<QuizResource> queryResult
        = indexRepository.findAllBy(query, QuizResource.class);
    List<QuizResource> quizResources = queryResult.getItems();

    if (CollectionUtils.isEmpty(quizResources)) {
      return new QueryResult<>(
          queryResult.getElapsedTime(),
          query.getOffset(),
          NUMBER_FOUND_ZERO,
          Collections.emptyList()
      );
    }

    Configuration configuration = Configuration.create(principal);
    List<QuizResourceModel> resourceModels
        = getQuizResourceModels(quizResources, configuration);
    return new QueryResult<>(
        queryResult.getElapsedTime(),
        queryResult.getStart(),
        queryResult.getNumFound(),
        resourceModels
    );
  }

  private List<QuizResourceModel> getQuizResourceModels(
      List<QuizResource> quizResources, Configuration configuration) {

    List<QuizResourceModel> resourceModels = new ArrayList<>();

    for (QuizResource quizResource : quizResources) {
      QuizResourceModel resourceModel
          = getQuizResourceModel(quizResource, configuration);
      resourceModels.add(resourceModel);
    }

    return resourceModels;
  }

  private QuizResourceModel getQuizResourceModel(
      QuizResource quizResource, Configuration configuration) {

    QuizResourceModel resourceModel = getModel(quizResource,
        QuizResourceModel.class, configuration);
    populateCreatedDateTime(resourceModel, quizResource, configuration);

    if (DateTimeUtils.isNotEmpty(quizResource.getModifiedDateTime())) {
      populateModifiedDateTime(resourceModel, quizResource, configuration);
    }

    return resourceModel;
  }

  @Log
  public Optional<QuizResourceModel> findBy(String id, Principal principal)
      throws IOException {

    Optional<QuizResource> resourceOptional
        = indexRepository.findBy(id, QuizResource.class);

    if (resourceOptional.isPresent()) {
      QuizResource quizResource = resourceOptional.get();
      Quiz quiz = quizResource.getQuiz();
      List<Component> components = getComponents(id);
      quiz.addComponent(components.toArray(new Component[0]));

      Configuration configuration = Configuration.create(principal);
      QuizResourceModel resourceModel
          = getQuizResourceModel(quizResource, configuration);
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
  public List<String> findAllComponentTypes() {
    Properties properties = propertiesProvider.get();
    String questionTypes = properties.getProperty("quiz.component.types");
    return Arrays.asList(questionTypes.split(","));
  }
}