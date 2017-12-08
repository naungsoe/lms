package com.hsystems.lms.service;

import com.google.inject.Inject;

import com.hsystems.lms.common.annotation.Log;
import com.hsystems.lms.common.query.Criterion;
import com.hsystems.lms.common.query.Query;
import com.hsystems.lms.common.query.QueryResult;
import com.hsystems.lms.common.security.Principal;
import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.common.util.DateTimeUtils;
import com.hsystems.lms.repository.ComponentRepository;
import com.hsystems.lms.repository.CourseRepository;
import com.hsystems.lms.repository.IndexRepository;
import com.hsystems.lms.repository.entity.Component;
import com.hsystems.lms.repository.entity.beans.ComponentBean;
import com.hsystems.lms.repository.entity.course.Course;
import com.hsystems.lms.repository.entity.course.CourseResource;
import com.hsystems.lms.repository.entity.lesson.LessonResource;
import com.hsystems.lms.service.mapper.Configuration;
import com.hsystems.lms.service.model.course.CourseResourceModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Created by naungsoe on 15/10/16.
 */
public class CourseService extends AbstractService {

  private final CourseRepository resourceRepository;

  private final ComponentRepository componentRepository;

  private final IndexRepository indexRepository;

  @Inject
  CourseService(
      CourseRepository resourceRepository,
      ComponentRepository componentRepository,
      IndexRepository indexRepository) {

    this.resourceRepository = resourceRepository;
    this.componentRepository = componentRepository;
    this.indexRepository = indexRepository;
  }

  @Log
  public QueryResult<CourseResourceModel> findAllBy(
      Query query, Principal principal)
      throws IOException {

    addSchoolFilter(query, principal);

    QueryResult<CourseResource> queryResult
        = indexRepository.findAllBy(query, LessonResource.class);
    List<CourseResource> lessonResources = queryResult.getItems();

    if (CollectionUtils.isEmpty(lessonResources)) {
      return new QueryResult<>(
          queryResult.getElapsedTime(),
          query.getOffset(),
          query.getLimit(),
          Collections.emptyList()
      );
    }

    Configuration configuration = Configuration.create(principal);
    List<CourseResourceModel> resourceModels
        = getCourseResourceModels(lessonResources, configuration);
    return new QueryResult<>(
        queryResult.getElapsedTime(),
        queryResult.getStart(),
        queryResult.getNumFound(),
        resourceModels
    );
  }

  private List<CourseResourceModel> getCourseResourceModels(
      List<CourseResource> courseResources, Configuration configuration) {

    List<CourseResourceModel> resourceModels = new ArrayList<>();

    for (CourseResource courseResource : courseResources) {
      CourseResourceModel resourceModel
          = getCourseResourceModel(courseResource, configuration);
      resourceModels.add(resourceModel);
    }

    return resourceModels;
  }

  private CourseResourceModel getCourseResourceModel(
      CourseResource courseResource, Configuration configuration) {

    CourseResourceModel resourceModel = getModel(courseResource,
        CourseResourceModel.class, configuration);
    populatedCreatedDateTime(resourceModel, courseResource, configuration);

    if (DateTimeUtils.isNotEmpty(courseResource.getModifiedDateTime())) {
      populatedModifiedDateTime(resourceModel, courseResource, configuration);
    }

    return resourceModel;
  }

  @Log
  public Optional<CourseResourceModel> findBy(String id, Principal principal)
      throws IOException {

    Optional<CourseResource> resourceOptional
        = indexRepository.findBy(id, CourseResource.class);

    if (resourceOptional.isPresent()) {
      CourseResource courseResource = resourceOptional.get();
      Course course = courseResource.getCourse();
      List<Component> components = getComponents(id);
      course.addComponent(components.toArray(new Component[0]));

      Configuration configuration = Configuration.create(principal);
      CourseResourceModel resourceModel
          = getCourseResourceModel(courseResource, configuration);
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
}
