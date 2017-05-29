package com.hsystems.lms.repository.entity;

import com.hsystems.lms.common.annotation.IndexField;

import java.io.Serializable;

/**
 * Created by naungsoe on 19/12/16.
 */
public class FileComponent implements Component, Serializable {

  private static final long serialVersionUID = -6449371376495474202L;

  @IndexField
  protected String id;

  @IndexField
  protected int order;

  @IndexField
  private FileResource file;

  FileComponent() {

  }

  public FileComponent(
      String id,
      int order,
      FileResource file) {

    this.id = id;
    this.order = order;
    this.file = file;
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
    return ComponentType.FILE;
  }

  public FileResource getFile() {
    return file;
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

    FileComponent component = (FileComponent) obj;
    return id.equals(component.getId());
  }

  @Override
  public String toString() {
    return String.format(
        "FileComponent{id=%s, order=%s, file=%s}",
        id, order, file);
  }
}
