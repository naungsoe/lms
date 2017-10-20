package com.hsystems.lms.repository.entity.file;

import com.hsystems.lms.common.annotation.IndexCollection;
import com.hsystems.lms.common.annotation.IndexField;

import java.io.Serializable;

/**
 * Created by naungsoe on 5/11/16.
 */
@IndexCollection(namespace = "lms", name = "files")
public final class FileObject implements Serializable {

  private static final long serialVersionUID = -2798310873506817051L;

  @IndexField
  protected String name;

  @IndexField
  protected String type;

  @IndexField
  protected long size;

  FileObject() {

  }

  public FileObject(
      String name,
      String type,
      long size) {

    this.name = name;
    this.type = type;
    this.size = size;
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
}
