package com.hsystems.lms.repository.beans;

import com.hsystems.lms.common.annotation.IndexField;
import com.hsystems.lms.repository.entity.quiz.SectionComponent;

import java.io.Serializable;

public class SectionComponentBean implements ComponentBean, Serializable {

  private static final long serialVersionUID = -2481521481131584333L;

  @IndexField
  protected String id;

  @IndexField
  protected String title;

  @IndexField
  protected String instructions;

  @IndexField
  protected int order;

  @IndexField
  private String resourceId;

  @IndexField
  private String parentId;

  SectionComponentBean() {

  }

  public SectionComponentBean(
      SectionComponent component,
      String resourceId,
      String parentId) {

    this.id = component.getId();
    this.title = component.getTitle();
    this.instructions = component.getInstructions();
    this.order = component.getOrder();
    this.resourceId = resourceId;
    this.parentId = parentId;
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
  public String getResourceId() {
    return resourceId;
  }

  @Override
  public String getParentId() {
    return parentId;
  }
}
