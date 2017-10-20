package com.hsystems.lms.service.model.file;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.hsystems.lms.service.model.ComponentModel;

import java.io.Serializable;

/**
 * Created by naungsoe on 7/10/16.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public final class FileComponentModel
    extends ComponentModel implements Serializable {

  private static final long serialVersionUID = 1952025743352855727L;

  private FileObjectModel fileObject;

  public FileComponentModel() {

  }

  public FileObjectModel getFileObject() {
    return fileObject;
  }

  public void setFileObject(FileObjectModel fileObject) {
    this.fileObject = fileObject;
  }
}
