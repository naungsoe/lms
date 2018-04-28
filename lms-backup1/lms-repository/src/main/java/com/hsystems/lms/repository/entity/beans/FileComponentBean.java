package com.hsystems.lms.repository.entity.beans;

import com.hsystems.lms.common.annotation.IndexField;
import com.hsystems.lms.repository.entity.file.FileComponent;
import com.hsystems.lms.repository.entity.file.FileObject;

public class FileComponentBean implements ComponentBean<FileComponent> {

  private static final long serialVersionUID = -5340775270955542074L;

  @IndexField
  private String id;

  @IndexField
  private FileObject fileObject;

  @IndexField
  protected int order;

  @IndexField
  private String resourceId;

  @IndexField
  private String parentId;

  FileComponentBean() {

  }

  public FileComponentBean(
      FileComponent component,
      String resourceId,
      String parentId) {

    this.id = component.getId();
    this.fileObject = component.getFileObject();
    this.order = component.getOrder();
    this.resourceId = resourceId;
    this.parentId = parentId;
  }

  @Override
  public String getId() {
    return id;
  }

  public FileObject getFileObject() {
    return fileObject;
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
  public FileComponent getComponent() {
    return new FileComponent(id, fileObject, order);
  }
}