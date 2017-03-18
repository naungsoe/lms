package com.hsystems.lms.repository.entity;

import com.hsystems.lms.common.IndexFieldType;
import com.hsystems.lms.common.annotation.IndexField;
import com.hsystems.lms.common.util.ListUtils;
import com.hsystems.lms.common.util.StringUtils;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * Created by naungsoe on 19/12/16.
 */
public class Section implements Serializable, Component {

  private static final long serialVersionUID = -8886998378935720413L;

  @IndexField(type = IndexFieldType.IDENTITY)
  private String id;

  @IndexField(type = IndexFieldType.INTEGER)
  private int order;

  @IndexField(type = IndexFieldType.STRING)
  private String instructions;

  @IndexField(type = IndexFieldType.LIST)
  private List<Component> components;

  Section() {

  }

  public Section(
      String id,
      int order,
      String instructions,
      List<Component> components) {

    this.id = id;
    this.order = order;
    this.instructions = instructions;
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

  public String getInstructions() {
    return instructions;
  }

  public List<Component> getComponents() {
    return ListUtils.isEmpty(components)
        ? Collections.emptyList()
        : Collections.unmodifiableList(components);
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

    Section section = (Section) obj;
    return id.equals(section.getId());
  }

  @Override
  public String toString() {
    return String.format(
        "Section{id=%s, order=%s, instructions=%s, components=%s}",
        id, order, instructions, StringUtils.join(components, ","));
  }
}
