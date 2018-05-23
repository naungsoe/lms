package com.hsystems.lms.lesson.repository.entity;

import com.hsystems.lms.common.specification.Specification;
import com.hsystems.lms.component.Component;
import com.hsystems.lms.question.repository.entity.QuestionComponent;

public final class ActivityCompositionSpecification
    implements Specification<Component> {

  @Override
  public boolean isSatisfiedBy(Component candidate) {
    boolean isContentComponent = candidate instanceof ContentComponent;
    boolean isQuestionComponent = candidate instanceof QuestionComponent;
    return !isContentComponent && !isQuestionComponent;
  }
}