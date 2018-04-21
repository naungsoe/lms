package com.hsystems.lms.repository.entity.question;

import com.hsystems.lms.common.util.CommonUtils;
import com.hsystems.lms.repository.entity.Component;
import com.hsystems.lms.repository.entity.CompositionStrategy;

public final class CompositeQuestionCompositionStrategy
    implements CompositionStrategy<Component> {

  @Override
  public void validate(Component component)
      throws IllegalArgumentException {

    boolean isCompositeQuestionComponent
        = component instanceof CompositeQuestionComponent;
    CommonUtils.checkArgument(!isCompositeQuestionComponent,
        "composite questions cannot be nested");
  }
}
