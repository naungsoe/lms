package com.hsystems.lms.repository.entity.quiz;

import com.hsystems.lms.common.util.CommonUtils;
import com.hsystems.lms.repository.entity.Component;
import com.hsystems.lms.repository.entity.CompositionStrategy;
import com.hsystems.lms.repository.entity.question.QuestionComponent;

import java.util.Enumeration;

public final class SectionCompositionStrategy
    implements CompositionStrategy<SectionComponent> {

  @Override
  public void validate(SectionComponent component)
      throws IllegalArgumentException {

    Enumeration<Component> enumeration = component.getComponents();

    while (enumeration.hasMoreElements()) {
      Component element = enumeration.nextElement();
      checkQuestionComponent(element);
    }
  }

  private void checkQuestionComponent(Component component) {
    boolean isQuestionComponent = component instanceof QuestionComponent;
    CommonUtils.checkArgument(isQuestionComponent, "component is not question");
  }
}
