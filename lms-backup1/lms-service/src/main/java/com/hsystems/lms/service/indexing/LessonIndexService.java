package com.hsystems.lms.service.indexing;

import com.google.inject.Inject;

import com.hsystems.lms.common.annotation.Log;
import com.hsystems.lms.common.annotation.Requires;
import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.repository.ComponentRepository;
import com.hsystems.lms.repository.IndexRepository;
import com.hsystems.lms.repository.LessonRepository;
import com.hsystems.lms.repository.entity.Component;
import com.hsystems.lms.repository.entity.beans.ComponentBean;
import com.hsystems.lms.repository.entity.lesson.LessonResource;
import com.hsystems.lms.service.AppPermission;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Created by naungsoe on 15/10/16.
 */
public class LessonIndexService extends IndexAbstractService {

  private final IndexRepository indexRepository;

  private final LessonRepository lessonRepository;

  private final ComponentRepository componentRepository;

  @Inject
  LessonIndexService(
      IndexRepository indexRepository,
      LessonRepository lessonRepository,
      ComponentRepository componentRepository) {

    this.indexRepository = indexRepository;
    this.lessonRepository = lessonRepository;
    this.componentRepository = componentRepository;
  }

  @Log
  @Requires(AppPermission.ADMINISTRATION)
  public void indexAllBy(String schoolId)
      throws IOException {

    String lastId = schoolId;
    int numFound;

    do {
      List<LessonResource> lessonResources = lessonRepository.findAllBy(
          schoolId, lastId, INDEX_LIMIT);

      for (LessonResource lessonResource : lessonResources) {
        String resourceId = lessonResource.getId();
        List<Component> components = componentRepository.findAllBy(resourceId);
        indexComponents(components, resourceId, resourceId);
      }

      indexRepository.save(lessonResources);

      numFound = lessonResources.size();
      lastId = lessonResources.get(numFound - 1).getId();

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

    Optional<LessonResource> resourceOptional
        = lessonRepository.findBy(id);

    if (resourceOptional.isPresent()) {
      LessonResource lessonResource = resourceOptional.get();
      String resourceId = lessonResource.getId();
      List<Component> components = componentRepository.findAllBy(resourceId);
      indexComponents(components, resourceId, resourceId);
      indexRepository.save(lessonResource);
    }
  }
}