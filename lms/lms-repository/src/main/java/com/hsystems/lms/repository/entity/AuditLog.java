package com.hsystems.lms.repository.entity;

import com.hsystems.lms.common.Action;
import com.hsystems.lms.common.EntityType;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Created by naungsoe on 2/11/16.
 */
public class AuditLog implements Serializable {

  private static final long serialVersionUID = -3408481188461757227L;

  private String entityId;

  private EntityType entityType;

  private User user;

  private long timestamp;

  private Action action;

  AuditLog() {

  }

  public AuditLog(
      String entityId,
      EntityType entityType,
      User user,
      long timestamp,
      Action action) {

    this.entityId = entityId;
    this.entityType = entityType;
    this.user = user;
    this.timestamp = timestamp;
    this.action = action;
  }

  public String getEntityId() { return entityId; }

  public EntityType getEntityType() { return entityType; }

  public User getUser() {
    return user;
  }

  public long getTimestamp() {
    return timestamp;
  }

  public Action getAction() {
    return action;
  }
}
