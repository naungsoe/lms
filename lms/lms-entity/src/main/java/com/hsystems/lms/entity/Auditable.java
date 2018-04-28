package com.hsystems.lms.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

public final class Auditable<T> implements Serializable {

  private static final long serialVersionUID = 5567615116126846611L;

  private T entity;

  private User createdBy;

  private LocalDateTime createdOn;

  private User modifiedBy;

  private LocalDateTime modifiedOn;

  public Auditable(
      T entity,
      User createdBy,
      LocalDateTime createdOn,
      User modifiedBy,
      LocalDateTime modifiedOn) {

    this.entity = entity;
    this.createdBy = createdBy;
    this.modifiedBy = modifiedBy;
    this.modifiedOn = modifiedOn;
  }

  public T getEntity() {
    return entity;
  }

  public User getCreatedBy() {
    return createdBy;
  }

  public LocalDateTime getCreatedOn() {
    return createdOn;
  }

  public User getModifiedBy() {
    return modifiedBy;
  }

  public LocalDateTime getModifiedOn() {
    return modifiedOn;
  }
}