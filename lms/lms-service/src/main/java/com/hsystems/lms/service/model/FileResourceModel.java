package com.hsystems.lms.service.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * Created by naungsoe on 7/10/16.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FileResourceModel extends AuditableModel implements Serializable {

  private static final long serialVersionUID = -4535401603567169953L;

  private String id;

  private String name;

  private String type;

  private String size;

  private boolean directory;

  public FileResourceModel() {

  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getSize() {
    return size;
  }

  public void setSize(String size) {
    this.size = size;
  }

  public boolean isDirectory() {
    return directory;
  }

  public void setDirectory(boolean directory) {
    this.directory = directory;
  }
}
