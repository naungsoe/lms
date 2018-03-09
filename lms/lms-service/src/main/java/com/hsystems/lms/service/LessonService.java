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
import com.hsystems.lms.repository.ComponentRepository;
import com.hsystems.lms.repository.IndexRepository;
import com.hsystems.lms.repository.LessonRepository;
import com.hsystems.lms.repository.entity.Component;
import com.hsystems.lms.repository.entity.beans.ComponentBean;
import com.hsystems.lms.repository.entity.lesson.Lesson;
import com.hsystems.lms.repository.entity.lesson.LessonResource;
import com.hsystems.lms.service.mapper.Configuration;
import com.hsystems.lms.service.model.lesson.LessonResourceModel;

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
public class LessonService extends AbstractService {

  private final Provider<Properties> propertiesProvider;

  private final LessonRepository resourceRepository;

  private final ComponentRepository componentRepository;

  private final IndexRepository indexRepository;

  @Inject
  LessonService(
      Provider<Properties> propertiesProvider,
      LessonRepository resourceRepository,
      ComponentRepository componentRepository,
      IndexRepository indexRepository) {

    this.propertiesProvider = propertiesProvider;
    this.resourceRepository = resourceRepository;
    this.componentRepository = componentRepository;
    this.indexRepository = indexRepository;
  }

  @Log
  public QueryResult<LessonResourceModel> findAllBy(
      Query query, Principal principal)
      throws IOException {

    addSchoolFilter(query, principal);

    QueryResult<LessonResource> queryResult
        = indexRepository.findAllBy(query, LessonResource.class);
    List<LessonResource> lessonResources = queryResult.getItems();

    if (CollectionUtils.isEmpty(lessonResources)) {
      return new QueryResult<>(
          queryResult.getElapsedTime(),
          query.getOffset(),
          NUMBER_FOUND_ZERO,
          Collections.emptyList()
      );
    }

    Configuration configuration = Configuration.create(principal);
    List<LessonResourceModel> resourceModels
        = getLessonResourceModels(lessonResources, configuration);
    return new QueryResult<>(
        queryResult.getElapsedTime(),
        queryResult.getStart(),
        queryResult.getNumFound(),
        resourceModels
    );
  }

  private List<LessonResourceModel> getLessonResourceModels(
      List<LessonResource> lessonResources, Configuration configuration) {

    List<LessonResourceModel> resourceModels = new ArrayList<>();

    for (LessonResource lessonResource : lessonResources) {
      LessonResourceModel resourceModel
          = getLessonResourceModel(lessonResource, configuration);
      resourceModels.add(resourceModel);
    }

    return resourceModels;
  }

  private LessonResourceModel getLessonResourceModel(
      LessonResource lessonResource, Configuration configuration) {

    LessonResourceModel resourceModel = getModel(lessonResource,
        LessonResourceModel.class, configuration);
    populatedCreatedDateTime(resourceModel, lessonResource, configuration);

    if (DateTimeUtils.isNotEmpty(lessonResource.getModifiedDateTime())) {
      populatedModifiedDateTime(resourceModel, lessonResource, configuration);
    }

    return resourceModel;
  }

  @Log
  public Optional<LessonResourceModel> findBy(String id, Principal principal)
      throws IOException {

    Optional<LessonResource> resourceOptional
        = indexRepository.findBy(id, LessonResource.class);

    if (resourceOptional.isPresent()) {
      LessonResource lessonResource = resourceOptional.get();
      Lesson lesson = lessonResource.getLesson();
      List<Component> components = getComponents(id);
      lesson.addComponent(components.toArray(new Component[0]));

      Configuration configuration = Configuration.create(principal);
      LessonResourceModel resourceModel
          = getLessonResourceModel(lessonResource, configuration);
      return Optional.of(resourceModel);
    }

    return Optional.empty();
  }

  private List<Component> getComponents(String id)
      throws IOException {

    Query query = Query.create();
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
    String questionTypes = properties.getProperty("lesson.component.types");
    return Arrays.asList(questionTypes.split(","));
  }
}
