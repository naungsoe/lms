package com.hsystems.lms.service.model;

import java.io.Serializable;

/**
 * Created by naungsoe on 7/10/16.
 */
public class FileComponentModel
    extends ComponentModel implements Serializable {

  private FileResourceModel file;

  public FileComponentModel() {

  }

  public FileResourceModel getFile() {
    return file;
  }

  public void setFile(FileResourceModel file) {
    this.file = file;
  }
}
