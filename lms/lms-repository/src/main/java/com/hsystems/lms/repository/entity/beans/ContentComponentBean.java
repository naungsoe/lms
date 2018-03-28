package com.hsystems.lms.repository.entity.beans;

import com.hsystems.lms.common.annotation.IndexField;
import com.hsystems.lms.repository.entity.lesson.ContentComponent;

import java.io.Serializable;

public class ContentComponentBean
    implements ComponentBean<ContentComponent>, Serializable {

  private static final long serialVersionUID = 8722347746596733895L;

  @IndexField
  protected String id;

  @IndexField
  private String content;

  @IndexField
  protected int order;

  @IndexField
  private String resourceId;

  @IndexField
  private String parentId;

  ContentComponentBean() {

  }

  public ContentComponentBean(
      ContentComponent component,
      String resourceId,
      String parentId) {

    this.id = component.getId();
    this.content = component.getContent();
    this.order = component.getOrder();
    this.resourceId = resourceId;
    this.parentId = parentId;
  }

  @Override
  public String getId() {
    return id;
  }

  public String getContent() {
    return content;
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
  public ContentComponent getComponent() {
    return new ContentComponent(id, content, order);
  }
}