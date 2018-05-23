package com.hsystems.lms.quiz.repository.entity;

import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.component.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by naungsoe on 19/12/16.
 */
public final class Quiz implements Serializable {

  private static final long serialVersionUID = -5364569103332460684L;

  private String title;

  private String description;

  protected List<Component> components;

  Quiz() {

  }

  public Quiz(
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

    QuizCompositionSpecification compositionSpecification
        = new QuizCompositionSpecification();

    for (Component component : components) {
      if (compositionSpecification.isSatisfiedBy(component)) {
        this.components.add(component);
      }
    }
  }

  public void removeComponent(Component component) {
    QuizCompositionSpecification compositionSpecification
        = new QuizCompositionSpecification();

    if (compositionSpecification.isSatisfiedBy(component)) {
      this.components.remove(component);
    }
  }
}