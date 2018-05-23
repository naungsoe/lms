package com.hsystems.lms.quiz.repository.entity;

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
public final class SectionComponent
    implements CompositeComponent, Serializable {

  private static final long serialVersionUID = 4286502504214585885L;

  private String id;

  private String title;

  private String instructions;

  private List<Component> components;

  SectionComponent() {

  }

  public SectionComponent(
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

    SectionCompositionSpecification compositionSpecification
        = new SectionCompositionSpecification();

    for (Component component : components) {
      if (compositionSpecification.isSatisfiedBy(component)) {
        this.components.add(component);
      }
    }
  }

  @Override
  public void removeComponent(Component component) {
    SectionCompositionSpecification compositionSpecification
        = new SectionCompositionSpecification();

    if (compositionSpecification.isSatisfiedBy(component)) {
      this.components.remove(component);
    }
  }
}