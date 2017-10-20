package com.hsystems.lms.repository.entity.course;

import com.hsystems.lms.common.util.CommonUtils;
import com.hsystems.lms.repository.entity.Component;
import com.hsystems.lms.repository.entity.CompositionStrategy;
import com.hsystems.lms.repository.entity.lesson.LessonComponent;
import com.hsystems.lms.repository.entity.quiz.QuizComponent;

import java.util.Enumeration;

public final class TopicCompositionStrategy
    implements CompositionStrategy<TopicComponent> {

  @Override
  public void validate(TopicComponent component)
      throws IllegalArgumentException {

    Enumeration<Component> enumeration = component.getComponents();

    while (enumeration.hasMoreElements()) {
      Component element = enumeration.nextElement();
      checkChildComponent(element);
    }
  }

  private void checkChildComponent(Component component) {
    boolean isLessonComponent = component instanceof LessonComponent;
    boolean isQuizComponent = component instanceof QuizComponent;
    CommonUtils.checkArgument(isLessonComponent|| isQuizComponent,
        "component is not lesson or quiz");
  }
}