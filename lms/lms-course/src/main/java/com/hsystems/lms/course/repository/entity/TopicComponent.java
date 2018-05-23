package com.hsystems.lms.course.repository.entity;

import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.component.Component;
import com.hsystems.lms.component.CompositeComponent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by naungsoe on 19/12/16.
 */
public final class TopicComponent implements CompositeComponent, Serializable {

  private static final long serialVersionUID = -1845573507866773815L;

  protected String id;

  protected String title;

  protected String instructions;

  protected List<Component> components;

  TopicComponent() {

  }

  public TopicComponent(
      String id,
      String title,
      String instructions,
      List<Component> components) {

    this.id = id;
    this.title = title;
    this.instructions = instructions;
    this.components = components;
  }

  @Override
  public String getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public String getInstructions() {
    return instructions;
  }

  @Override
  public Enumeration<Component> getComponents() {
    return CollectionUtils.isEmpty(components)
        ? Collections.emptyEnumeration()
        : Collections.enumeration(components);
  }

  @Override
  public void addComponent(Component... components) {
    if (CollectionUtils.isEmpty(this.components)) {
      this.components = new ArrayList<>();
    }

    TopicCompositionStrategy compositionStrategy
        = new TopicCompositionStrategy();

    for (Component component : components) {
      if (compositionStrategy.isSatisfiedBy(component)) {
        this.components.add(component);
      }
    }
  }

  @Override
  public void removeComponent(Component component) {
    TopicCompositionStrategy compositionStrategy
        = new TopicCompositionStrategy();

    if (compositionStrategy.isSatisfiedBy(component)) {
      this.components.remove(component);
    }
  }
}
