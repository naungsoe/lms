package com.hsystems.lms.course.repository.entity;

import com.hsystems.lms.common.specification.Specification;
import com.hsystems.lms.component.Component;
import com.hsystems.lms.lesson.repository.entity.LessonComponent;
import com.hsystems.lms.quiz.repository.entity.QuizComponent;

public final class TopicCompositionStrategy
    implements Specification<Component> {

  @Override
  public boolean isSatisfiedBy(Component candidate) {
    boolean isLessonComponent = candidate instanceof LessonComponent;
    boolean isQuizComponent = candidate instanceof QuizComponent;
    return !isLessonComponent && !isQuizComponent;
  }
}