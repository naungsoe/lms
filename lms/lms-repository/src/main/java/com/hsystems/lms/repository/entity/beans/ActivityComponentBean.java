package com.hsystems.lms.repository.entity.beans;

import com.hsystems.lms.common.annotation.IndexField;
import com.hsystems.lms.repository.entity.lesson.ActivityComponent;

import java.io.Serializable;

public class ActivityComponentBean implements ComponentBean, Serializable {

  private static final long serialVersionUID = -2982013864114667100L;

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

  ActivityComponentBean() {

  }

  public ActivityComponentBean(
      ActivityComponent component,
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
