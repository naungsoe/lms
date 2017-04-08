package com.hsystems.lms.repository.entity;

import com.hsystems.lms.common.annotation.IndexField;

import java.time.LocalDateTime;

/**
 * Created by naungsoe on 5/11/16.
 */
public abstract class Auditable implements Entity {

  @IndexField
  protected User createdBy;

  @IndexField
  protected LocalDateTime createdDateTime;

  @IndexField
  protected User modifiedBy;

  @IndexField
  protected LocalDateTime modifiedDateTime;

  public User getCreatedBy() {
    return createdBy;
  }

  public LocalDateTime getCreatedDateTime() {
    return createdDateTime;
  }

  public User getModifiedBy() {
    return modifiedBy;
  }

  public LocalDateTime getModifiedDateTime() {
    return modifiedDateTime;
  }
}
