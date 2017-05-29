package com.hsystems.lms.repository.entity;

import com.hsystems.lms.common.annotation.IndexCollection;
import com.hsystems.lms.common.annotation.IndexField;

import java.io.Serializable;

/**
 * Created by naungsoe on 5/11/16.
 */
@IndexCollection(name = "files")
public class FileResource extends Resource implements Serializable {

  private static final long serialVersionUID = -4200928490264612997L;

  @IndexField
  private String id;

  @IndexField
  private String name;

  @IndexField
  private String type;

  @IndexField
  private long size;

  @IndexField
  private boolean directory;

  FileResource() {

  }

  public FileResource(
      String id,
      String name,
      String type,
      long size,
      boolean directory) {

    this.id = id;
    this.name = name;
    this.type = type;
    this.size = size;
    this.directory = directory;
  }

  @Override
  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getType() {
    return type;
  }

  public long getSize() {
    return size;
  }

  public boolean isDirectory() {
    return directory;
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

    FileResource file = (FileResource) obj;
    return id.equals(file.getId());
  }

  @Override
  public String toString() {
    return String.format(
        "FileResource{id=%s, name=%s, type=s%, size=%s, directory=%s}",
        id, name, type, size, directory);
  }
}
