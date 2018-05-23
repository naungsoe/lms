package com.hsystems.lms.course.repository.entity;

import com.hsystems.lms.common.specification.Specification;
import com.hsystems.lms.component.Component;

public final class CourseCompositionSpecification
    implements Specification<Component> {

  @Override
  public boolean isSatisfiedBy(Component candidate) {
    boolean isActivityComponent = candidate instanceof TopicComponent;
    return !isActivityComponent;
  }
}