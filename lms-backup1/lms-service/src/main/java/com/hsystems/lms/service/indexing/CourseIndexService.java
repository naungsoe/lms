package com.hsystems.lms.service.indexing;

import com.google.inject.Inject;

import com.hsystems.lms.common.annotation.Log;
import com.hsystems.lms.common.annotation.Requires;
import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.repository.ComponentRepository;
import com.hsystems.lms.repository.CourseRepository;
import com.hsystems.lms.repository.IndexRepository;
import com.hsystems.lms.repository.entity.Component;
import com.hsystems.lms.repository.entity.beans.ComponentBean;
import com.hsystems.lms.repository.entity.course.CourseResource;
import com.hsystems.lms.service.AppPermission;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Created by naungsoe on 15/10/16.
 */
public class CourseIndexService extends IndexAbstractService {

  private final IndexRepository indexRepository;

  private final CourseRepository courseRepository;

  private final ComponentRepository componentRepository;

  @Inject
  CourseIndexService(
      IndexRepository indexRepository,
      CourseRepository courseRepository,
      ComponentRepository componentRepository) {

    this.indexRepository = indexRepository;
    this.courseRepository = courseRepository;
    this.componentRepository = componentRepository;
  }

  @Log
  @Requires(AppPermission.ADMINISTRATION)
  public void indexAllBy(String schoolId)
      throws IOException {

    String lastId = schoolId;
    int numFound;

    do {
      List<CourseResource> courseResources = courseRepository.findAllBy(
          schoolId, lastId, INDEX_LIMIT);

      for (CourseResource courseResource : courseResources) {
        String resourceId = courseResource.getId();
        List<Component> components = componentRepository.findAllBy(resourceId);
        indexComponents(components, resourceId, resourceId);
      }

      indexRepository.save(courseResources);

      numFound = courseResources.size();
      lastId = courseResources.get(numFound - 1).getId();

    } while (!isLastPage(numFound));
  }

  private void indexComponents(
      List<Component> components, String resourceId, String parentId)
      throws IOException {

    List<ComponentBean> componentBeans
        = getComponentBeans(components, resourceId, parentId);

    if (CollectionUtils.isNotEmpty(componentBeans)) {
      indexRepository.save(componentBeans);
    }
  }

  @Log
  @Requires(AppPermission.ADMINISTRATION)
  public void indexBy(String id)
      throws IOException {

    Optional<CourseResource> resourceOptional
        = courseRepository.findBy(id);

    if (resourceOptional.isPresent()) {
      CourseResource courseResource = resourceOptional.get();
      String resourceId = courseResource.getId();
      List<Component> components = componentRepository.findAllBy(resourceId);
      indexComponents(components, resourceId, resourceId);
      indexRepository.save(courseResource);
    }
  }
}