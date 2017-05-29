package com.hsystems.lms.repository.entity;

import com.hsystems.lms.common.annotation.IndexField;
import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.common.util.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by naungsoe on 19/12/16.
 */
public class SectionComponent implements CompositeComponent, Serializable {

  private static final long serialVersionUID = -3814625872821804684L;

  @IndexField
  protected String id;

  @IndexField
  protected int order;

  @IndexField
  private String title;

  @IndexField
  private String instructions;

  @IndexField
  private List<Component> components;

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

  @Override
  public int getOrder() {
    return order;
  }

  @Override
  public ComponentType getType() {
    return ComponentType.SECTION;
  }

  public String getTitle() {
    return title;
  }

  public String getInstructions() {
    return instructions;
  }

  @Override
  public List<Component> getComponents() {
    return CollectionUtils.isEmpty(components)
        ? Collections.emptyList()
        : Collections.unmodifiableList(components);
  }

  @Override
  public void addComponent(Component... component) {
    if (CollectionUtils.isEmpty(components)) {
      components = new ArrayList<>();
    }

    components.addAll(Arrays.asList(component));
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

    SectionComponent section = (SectionComponent) obj;
    return id.equals(section.getId());
  }

  @Override
  public String toString() {
    return String.format(
        "SectionComponent{id=%s, title=s%, "
            + "instructions=%s, order=%s, components=%s}",
        id, title, instructions, order,
        StringUtils.join(components, ","));
  }
}
