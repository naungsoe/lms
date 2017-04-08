package com.hsystems.lms.service.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * Created by naungsoe on 5/11/16.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class AuditableModel implements Serializable {

  private UserModel createdBy;

  private String createdDateTime;

  private UserModel modifiedBy;

  private String modifiedDateTime;

  public UserModel getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(UserModel createdBy) {
    this.createdBy = createdBy;
  }

  public String getCreatedDateTime() {
    return createdDateTime;
  }

  public void setCreatedDateTime(String createdDateTime) {
    this.createdDateTime = createdDateTime;
  }

  public UserModel getModifiedBy() {
    return modifiedBy;
  }

  public void setModifiedBy(UserModel modifiedBy) {
    this.modifiedBy = modifiedBy;
  }

  public String getModifiedDateTime() {
    return modifiedDateTime;
  }

  public void setModifiedDateTime(String modifiedDateTime) {
    this.modifiedDateTime = modifiedDateTime;
  }
}
