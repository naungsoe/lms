package com.hsystems.lms.lesson.repository.entity;

import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.component.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by naungsoe on 7/10/16.
 */
public final class Lesson implements Serializable {

  private static final long serialVersionUID = 5109968244252479031L;

  private String title;

  private String description;

  protected List<Component> components;

  Lesson() {

  }

  public Lesson(
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

    LessonCompositionSpecification compositionSpecification
        = new LessonCompositionSpecification();

    for (Component component : components) {
      if (compositionSpecification.isSatisfiedBy(component)) {
        this.components.add(component);
      }
    }
  }

  public void removeComponent(Component component) {
    LessonCompositionSpecification compositionSpecification
        = new LessonCompositionSpecification();

    if (compositionSpecification.isSatisfiedBy(component)) {
      this.components.remove(component);
    }
  }
}