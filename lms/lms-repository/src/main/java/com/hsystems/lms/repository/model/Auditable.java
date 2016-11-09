package com.hsystems.lms.repository.model;

import java.time.LocalDateTime;

/**
 * Created by naungsoe on 5/11/16.
 */
public class Auditable {

  private User createdBy;

  private LocalDateTime createdDateTime;

  private User modifiedBy;

  private LocalDateTime modifiedDateTime;

  Auditable() {

  }

  public Auditable(
      User createdBy,
      LocalDateTime createdDateTime,
      User modifiedBy,
      LocalDateTime modifiedDateTime) {

    this.createdBy = createdBy;
    this.createdDateTime = createdDateTime;
    this.modifiedBy = modifiedBy;
    this.modifiedDateTime = modifiedDateTime;
  }

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
