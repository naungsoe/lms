package com.hsystems.lms.repository.entity.beans;

import com.hsystems.lms.common.annotation.IndexField;
import com.hsystems.lms.repository.entity.Component;
import com.hsystems.lms.repository.entity.course.TopicComponent;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class TopicComponentBean
    implements ComponentBean<TopicComponent>, Serializable {

  private static final long serialVersionUID = -7176783670628773493L;

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

  TopicComponentBean() {

  }

  public TopicComponentBean(
      TopicComponent component,
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
  public TopicComponent getComponent() {
    List<Component> components = Collections.emptyList();
    return new TopicComponent(id, title, instructions, order, components);
  }
}
