package com.hsystems.lms.service.indexing;

import com.google.inject.Inject;

import com.hsystems.lms.common.annotation.Log;
import com.hsystems.lms.common.annotation.Requires;
import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.repository.ComponentRepository;
import com.hsystems.lms.repository.IndexRepository;
import com.hsystems.lms.repository.QuizRepository;
import com.hsystems.lms.repository.entity.Component;
import com.hsystems.lms.repository.entity.beans.ComponentBean;
import com.hsystems.lms.repository.entity.quiz.QuizResource;
import com.hsystems.lms.service.AppPermission;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Created by naungsoe on 15/10/16.
 */
public class QuizIndexService extends IndexAbstractService {

  private final IndexRepository indexRepository;

  private final QuizRepository quizRepository;

  private final ComponentRepository componentRepository;

  @Inject
  QuizIndexService(
      IndexRepository indexRepository,
      QuizRepository quizRepository,
      ComponentRepository componentRepository) {

    this.indexRepository = indexRepository;
    this.quizRepository = quizRepository;
    this.componentRepository = componentRepository;
  }

  @Log
  @Requires(AppPermission.ADMINISTRATION)
  public void indexAllBy(String schoolId)
      throws IOException {

    String lastId = schoolId;
    int numFound;

    do {
      List<QuizResource> quizResources = quizRepository.findAllBy(
          schoolId, lastId, INDEX_LIMIT);

      for (QuizResource quizResource : quizResources) {
        String resourceId = quizResource.getId();
        List<Component> components = componentRepository.findAllBy(resourceId);
        indexComponents(components, resourceId, resourceId);
      }

      indexRepository.save(quizResources);

      numFound = quizResources.size();
      lastId = quizResources.get(numFound - 1).getId();

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

    Optional<QuizResource> resourceOptional
        = quizRepository.findBy(id);

    if (resourceOptional.isPresent()) {
      QuizResource quizResource = resourceOptional.get();
      String resourceId = quizResource.getId();
      List<Component> components = componentRepository.findAllBy(resourceId);
      indexComponents(components, resourceId, resourceId);
      indexRepository.save(quizResource);
    }
  }
}