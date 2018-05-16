package com.hsystems.lms.quiz.repository.entity;

import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.component.Component;
import com.hsystems.lms.component.CompositeComponent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by naungsoe on 19/12/16.
 */
public class SectionComponent implements CompositeComponent, Serializable {

  private static final long serialVersionUID = 5493270573603027469L;

  protected String id;

  protected String title;

  protected String instructions;

  protected int order;

  protected List<Component> components;

  SectionComponent() {

  }

  public SectionComponent(
      String id,
      String title,
      String instructions,
      int order,
      List<Component> components) {

    this.id = id;
    this.title = title;
    this.instructions = instructions;
    this.order = order;
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
  public int getOrder() {
    return order;
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
    Arrays.stream(components).forEach(component -> {
      if (compositionSpecification.isSatisfiedBy(component)) {
        this.components.add(component);
      }
    });
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