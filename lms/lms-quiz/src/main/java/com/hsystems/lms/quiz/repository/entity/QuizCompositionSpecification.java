package com.hsystems.lms.quiz.repository.entity;

import com.hsystems.lms.common.specification.Specification;
import com.hsystems.lms.component.Component;

public final class QuizCompositionSpecification
    implements Specification<Component> {

  @Override
  public boolean isSatisfiedBy(Component candidate) {
    boolean isSectionComponent = candidate instanceof SectionComponent;
    return !isSectionComponent;
  }
}