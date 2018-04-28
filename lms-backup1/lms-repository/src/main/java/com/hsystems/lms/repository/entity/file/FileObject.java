package com.hsystems.lms.repository.entity.file;

import com.hsystems.lms.common.annotation.IndexField;

import java.io.Serializable;

/**
 * Created by naungsoe on 5/11/16.
 */
public final class FileObject implements Serializable {

  private static final long serialVersionUID = -1249643626651905996L;

  @IndexField
  protected String name;

  @IndexField
  protected long size;

  @IndexField
  protected boolean directory;

  FileObject() {

  }

  public FileObject(
      String name,
      long size,
      boolean directory) {

    this.name = name;
    this.size = size;
    this.directory = directory;
  }

  public String getName() {
    return name;
  }

  public long getSize() {
    return size;
  }

  public boolean isDirectory() {
    return directory;
  }
}
