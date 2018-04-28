package com.hsystems.lms.repository.entity.quiz;

import com.hsystems.lms.common.util.CommonUtils;
import com.hsystems.lms.repository.entity.Component;
import com.hsystems.lms.repository.entity.CompositionStrategy;

public final class QuizCompositionStrategy
    implements CompositionStrategy<Component> {

  @Override
  public void validate(Component component)
      throws IllegalArgumentException {

    boolean isSectionComponent = component instanceof SectionComponent;
    CommonUtils.checkArgument(isSectionComponent, "component is not section");

    SectionComponent sectionComponent = (SectionComponent) component;
    SectionCompositionStrategy strategy = new SectionCompositionStrategy();
    strategy.validate(sectionComponent);
  }
}
