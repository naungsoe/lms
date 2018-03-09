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

  private static final long serialVersionUID = -1790250092029097898L;

  @IndexField
  private String title;

  @IndexField
  private String description;

  @IndexField
  protected List<Component> components;

  Course() {

  }

  public Course(
      String title,
      String description,
      List<Component> components) {

    this.title = title;
    this.description = description;
    this.components = components;
  }

  public String getTitle() {
    return title;
  }

  public String getDescription() {
    return description;
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