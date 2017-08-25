package com.hsystems.lms.repository.entity.file;

import com.hsystems.lms.common.annotation.IndexField;
import com.hsystems.lms.repository.entity.Component;

import java.io.Serializable;

/**
 * Created by naungsoe on 19/12/16.
 */
public class FileComponent implements Component, Serializable {

  private static final long serialVersionUID = 5996223382448951189L;

  @IndexField
  protected String id;

  @IndexField
  private FileResource file;

  @IndexField
  protected int order;

  FileComponent() {

  }

  public FileComponent(
      String id,
      FileResource file,
      int order) {

    this.id = id;
    this.file = file;
    this.order = order;
  }

  @Override
  public String getId() {
    return id;
  }

  public FileResource getFile() {
    return file;
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
        "FileComponent{id=%s, file=%s, order=%s}",
        id, file, order);
  }
}
