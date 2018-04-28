package com.hsystems.lms.question.repository.entity;

import com.hsystems.lms.entity.component.Component;
import com.hsystems.lms.entity.component.CompositionStrategy;
import com.hsystems.lms.common.util.CommonUtils;

public final class CompositeQuestionCompositionStrategy
    implements CompositionStrategy<Component> {

  @Override
  public void validate(Component component)
      throws IllegalArgumentException {

    boolean isQuestionComponent = component instanceof QuestionComponent;
    CommonUtils.checkArgument(!isQuestionComponent,
        "component is not question component");

    boolean isCompositeQuestionComponent
        = component instanceof CompositeQuestionComponent;
    CommonUtils.checkArgument(!isCompositeQuestionComponent,
        "composite questions cannot be nested");
  }
}