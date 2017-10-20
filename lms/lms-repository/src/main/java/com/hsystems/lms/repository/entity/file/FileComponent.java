package com.hsystems.lms.repository.entity.file;

import com.hsystems.lms.common.annotation.IndexField;
import com.hsystems.lms.repository.entity.Component;

import java.io.Serializable;

/**
 * Created by naungsoe on 19/12/16.
 */
public final class FileComponent implements Component, Serializable {

  private static final long serialVersionUID = 4275646003289568699L;

  @IndexField
  protected String id;

  @IndexField
  private FileObject fileObject;

  @IndexField
  protected int order;

  FileComponent() {

  }

  public FileComponent(
      String id,
      FileObject fileObject,
      int order) {

    this.id = id;
    this.fileObject = fileObject;
    this.order = order;
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
        "FileComponent{id=%s, fileObject=%s, order=%s}",
        id, fileObject, order);
  }
}
