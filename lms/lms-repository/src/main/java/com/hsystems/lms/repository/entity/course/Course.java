package com.hsystems.lms.repository.entity.course;

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
public final class Course implements Serializable {

  private static final long serialVersionUID = 4572593347753878858L;

  @IndexField
  private String title;

  @IndexField
  private String instructions;

  @IndexField
  protected List<Component> components;

  Course() {

  }

  public Course(
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
      CourseCompositionStrategy compositionStrategy
          = new CourseCompositionStrategy();
      compositionStrategy.validate(component);
      this.components.add(component);
    });
  }

  public void removeComponent(Component component) {
    CourseCompositionStrategy compositionStrategy
        = new CourseCompositionStrategy();
    compositionStrategy.validate(component);
    this.components.remove(component);
  }
}