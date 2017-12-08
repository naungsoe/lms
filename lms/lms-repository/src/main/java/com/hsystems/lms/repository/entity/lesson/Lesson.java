package com.hsystems.lms.repository.entity.lesson;

import com.hsystems.lms.common.annotation.IndexField;
import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.repository.entity.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by naungsoe on 7/10/16.
 */
public final class Lesson implements Serializable {

  private static final long serialVersionUID = 6290193694866026328L;

  @IndexField
  private String title;

  @IndexField
  private String instructions;

  protected List<Component> components;

  Lesson() {

  }

  public Lesson(
      String title,
      String instructions,
      List<Component> components) {

    this.title = title;
    this.instructions = instructions;
    this.components = components;
  }

  public String getTitle() {
    return title;
  }

  public String getInstructions() {
    return instructions;
  }

  public Enumeration<Component> getComponents() {
    return CollectionUtils.isEmpty(components)
        ? Collections.emptyEnumeration()
        : Collections.enumeration(components);
  }

  public void addComponent(Component... components) {
    if (CollectionUtils.isEmpty(this.components)) {
      this.components = new ArrayList<>();
    }

    Arrays.stream(components).forEach(component -> {
      LessonCompositionStrategy compositionStrategy
          = new LessonCompositionStrategy();
      compositionStrategy.validate(component);
      this.components.add(component);
    });
  }

  public void removeComponent(Component component) {
    LessonCompositionStrategy compositionStrategy
        = new LessonCompositionStrategy();
    compositionStrategy.validate(component);
    this.components.remove(component);
  }
}