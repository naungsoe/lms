package com.hsystems.lms.service.indexing;

import com.google.inject.Inject;

import com.hsystems.lms.common.annotation.Log;
import com.hsystems.lms.common.annotation.Requires;
import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.repository.ComponentRepository;
import com.hsystems.lms.repository.IndexRepository;
import com.hsystems.lms.repository.QuestionRepository;
import com.hsystems.lms.repository.entity.Component;
import com.hsystems.lms.repository.entity.beans.ComponentBean;
import com.hsystems.lms.repository.entity.question.CompositeQuestion;
import com.hsystems.lms.repository.entity.question.Question;
import com.hsystems.lms.repository.entity.question.QuestionResource;
import com.hsystems.lms.service.AppPermission;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Created by naungsoe on 15/10/16.
 */
public class QuestionIndexService extends IndexAbstractService {

  private final IndexRepository indexRepository;

  private final QuestionRepository questionRepository;

  private final ComponentRepository componentRepository;

  @Inject
  QuestionIndexService(
      IndexRepository indexRepository,
      QuestionRepository questionRepository,
      ComponentRepository componentRepository) {

    this.indexRepository = indexRepository;
    this.questionRepository = questionRepository;
    this.componentRepository = componentRepository;
  }

  @Log
  @Requires(AppPermission.ADMINISTRATION)
  public void indexAllBy(String schoolId)
      throws IOException {

    String lastId = schoolId;
    int numFound;

    do {
      List<QuestionResource> questionResources = questionRepository.findAllBy(
          schoolId, lastId, INDEX_LIMIT);

      for (QuestionResource questionResource : questionResources) {
        Question question = questionResource.getQuestion();

        if (question instanceof CompositeQuestion) {
          String resourceId = questionResource.getId();
          List<Component> components
              = componentRepository.findAllBy(resourceId);
          indexComponents(components, resourceId, resourceId);
        }
      }

      indexRepository.save(questionResources);

      numFound = questionResources.size();
      lastId = questionResources.get(numFound - 1).getId();

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

    Optional<QuestionResource> resourceOptional
        = questionRepository.findBy(id);

    if (resourceOptional.isPresent()) {
      QuestionResource questionResource = resourceOptional.get();
      Question question = questionResource.getQuestion();

      if (question instanceof CompositeQuestion) {
        String resourceId = questionResource.getId();
        List<Component> components = componentRepository.findAllBy(resourceId);
        indexComponents(components, resourceId, resourceId);
      }
      indexRepository.save(questionResource);
    }
  }
}