package com.hsystems.lms.repository.entity.lesson;

import com.hsystems.lms.common.util.CommonUtils;
import com.hsystems.lms.repository.entity.Component;
import com.hsystems.lms.repository.entity.CompositionStrategy;
import com.hsystems.lms.repository.entity.file.FileComponent;
import com.hsystems.lms.repository.entity.question.QuestionComponent;

import java.util.Enumeration;

public final class ActivityCompositionStrategy
    implements CompositionStrategy<ActivityComponent> {

  @Override
  public void validate(ActivityComponent component)
      throws IllegalArgumentException {

    Enumeration<Component> enumeration = component.getComponents();

    while (enumeration.hasMoreElements()) {
      Component element = enumeration.nextElement();
      checkChildComponent(element);
    }
  }

  private void checkChildComponent(Component component) {
    boolean isQuestionComponent = component instanceof QuestionComponent;
    boolean isFileComponent = component instanceof FileComponent;
    boolean isContentComponent = component instanceof ContentComponent;
    CommonUtils.checkArgument(isQuestionComponent
            || isFileComponent || isContentComponent,
        "component is not question or file or content");
  }
}