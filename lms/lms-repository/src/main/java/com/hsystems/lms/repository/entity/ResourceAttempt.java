package com.hsystems.lms.repository.entity;

import com.hsystems.lms.common.annotation.IndexCollection;
import com.hsystems.lms.common.annotation.IndexField;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Created by naungsoe on 5/11/16.
 */
@IndexCollection(namespace = "lms", name = "attempts")
public abstract class ResourceAttempt implements Entity, Serializable {

  @IndexField
  protected String id;

  @IndexField
  protected User attemptedBy;

  @IndexField
  protected LocalDateTime attemptedDateTime;

  @Override
  public String getId() {
    return id;
  }

  public User getAttemptedBy() {
    return attemptedBy;
  }

  public LocalDateTime getAttemptedDateTime() {
    return attemptedDateTime;
  }
}