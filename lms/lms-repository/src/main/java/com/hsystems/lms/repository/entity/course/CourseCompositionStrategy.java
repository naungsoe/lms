package com.hsystems.lms.repository.entity.course;

import com.hsystems.lms.common.util.CommonUtils;
import com.hsystems.lms.repository.entity.Component;
import com.hsystems.lms.repository.entity.CompositionStrategy;

public final class CourseCompositionStrategy
    implements CompositionStrategy<Component> {

  @Override
  public void validate(Component component)
      throws IllegalArgumentException {

    boolean isTopicComponent = component instanceof TopicComponent;
    CommonUtils.checkArgument(isTopicComponent, "component is not topic");

    TopicComponent topicComponent = (TopicComponent) component;
    TopicCompositionStrategy strategy = new TopicCompositionStrategy();
    strategy.validate(topicComponent);
  }
}
