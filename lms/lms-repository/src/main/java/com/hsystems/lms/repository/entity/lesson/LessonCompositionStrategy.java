package com.hsystems.lms.repository.entity.lesson;

import com.hsystems.lms.common.util.CommonUtils;
import com.hsystems.lms.repository.entity.Component;
import com.hsystems.lms.repository.entity.CompositionStrategy;

public final class LessonCompositionStrategy
    implements CompositionStrategy<Component> {

  @Override
  public void validate(Component component)
      throws IllegalArgumentException {

    boolean isActivityComponent = component instanceof ActivityComponent;
    CommonUtils.checkArgument(isActivityComponent, "component is not activity");

    ActivityComponent activityComponent = (ActivityComponent) component;
    ActivityCompositionStrategy strategy = new ActivityCompositionStrategy();
    strategy.validate(activityComponent);
  }
}
