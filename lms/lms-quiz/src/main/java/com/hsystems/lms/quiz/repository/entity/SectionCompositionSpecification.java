package com.hsystems.lms.quiz.repository.entity;

import com.hsystems.lms.common.specification.Specification;
import com.hsystems.lms.component.Component;
import com.hsystems.lms.question.repository.entity.QuestionComponent;

import java.util.Enumeration;

public final class SectionCompositionSpecification
    implements Specification<Component> {

  @Override
  public boolean isSatisfiedBy(Component candidate) {
    boolean isQuestionComponent = candidate instanceof QuestionComponent;
    return !isQuestionComponent;
  }
}