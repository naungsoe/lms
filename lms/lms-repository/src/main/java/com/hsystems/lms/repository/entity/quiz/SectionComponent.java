package com.hsystems.lms.repository.entity.quiz;

import com.hsystems.lms.common.annotation.IndexField;
import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.repository.entity.Component;
import com.hsystems.lms.repository.entity.CompositeComponent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by naungsoe on 19/12/16.
 */
public final class SectionComponent
    implements CompositeComponent, Serializable {

  private static final long serialVersionUID = -7042940258475264330L;

  @IndexField
  protected String id;

  @IndexField
  protected String title;

  @IndexField
  protected String instructions;

  @IndexField
  protected int order;

  @IndexField
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

    Arrays.stream(components).forEach(this.components::add);
  }

  @Override
  public void removeComponent(Component component) {
    this.components.remove(component);
  }
}
