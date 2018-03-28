package com.hsystems.lms.repository.entity.beans;

import com.hsystems.lms.common.annotation.IndexField;
import com.hsystems.lms.repository.entity.Component;
import com.hsystems.lms.repository.entity.lesson.ActivityComponent;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class ActivityComponentBean
    implements ComponentBean<ActivityComponent>, Serializable {

  private static final long serialVersionUID = -4873013093631706469L;

  @IndexField
  private String id;

  @IndexField
  private String title;

  @IndexField
  private String instructions;

  @IndexField
  private int order;

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

  @Override
  public ActivityComponent getComponent() {
    List<Component> components = Collections.emptyList();
    return new ActivityComponent(id, title, instructions, order, components);
  }
}
