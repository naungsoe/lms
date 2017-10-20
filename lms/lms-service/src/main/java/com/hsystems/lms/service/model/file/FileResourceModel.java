package com.hsystems.lms.service.model.file;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.hsystems.lms.service.model.ResourceModel;

import java.io.Serializable;

/**
 * Created by naungsoe on 7/10/16.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public final class FileResourceModel extends ResourceModel
    implements Serializable {

  private static final long serialVersionUID = -620604907240575199L;

  private FileObjectModel fileObject;

  public FileResourceModel() {

  }

  public FileObjectModel getFileObject() {
    return fileObject;
  }

  public void setFileObject(FileObjectModel fileObject) {
    this.fileObject = fileObject;
  }
}
