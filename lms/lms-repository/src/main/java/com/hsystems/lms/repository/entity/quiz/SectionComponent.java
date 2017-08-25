package com.hsystems.lms.repository.entity.quiz;

import com.hsystems.lms.common.annotation.IndexField;
import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.common.util.StringUtils;
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
public class SectionComponent implements CompositeComponent, Serializable {

  private static final long serialVersionUID = 739829542487117998L;

  @IndexField
  protected String id;

  @IndexField
  protected String title;

  @IndexField
  protected String instructions;

  @IndexField
  protected List<Component> components;

  @IndexField
  protected int order;

  SectionComponent() {

  }

  public SectionComponent(
      String id,
      String title,
      String instructions,
      List<Component> components,
      int order) {

    this.id = id;
    this.title = title;
    this.instructions = instructions;
    this.components = components;
    this.order = order;
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

    Arrays.stream(components).forEach(this.components::add);
  }

  @Override
  public void removeComponent(Component component) {
    this.components.remove(component);
  }

  @Override
  public int getOrder() {
    return order;
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if ((obj == null) || (getClass() != obj.getClass())) {
      return false;
    }

    SectionComponent component = (SectionComponent) obj;
    return id.equals(component.getId());
  }

  @Override
  public String toString() {
    return String.format(
        "SectionComponent{id=%s, title=%s, instructions=%s, "
            + "components=%s, order=%s}",
        id, title, instructions, StringUtils.join(components, ","), order);
  }
}
