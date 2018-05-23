package com.hsystems.lms.lesson.repository.entity;

import com.hsystems.lms.common.specification.Specification;
import com.hsystems.lms.component.Component;

public final class LessonCompositionSpecification
    implements Specification<Component> {

  @Override
  public boolean isSatisfiedBy(Component candidate) {
    boolean isActivityComponent = candidate instanceof ActivityComponent;
    return !isActivityComponent;
  }
}