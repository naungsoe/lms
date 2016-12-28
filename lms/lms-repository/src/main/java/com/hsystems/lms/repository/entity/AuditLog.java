package com.hsystems.lms.repository.entity;

import com.hsystems.lms.common.Action;
import com.hsystems.lms.common.EntityType;

import java.io.Serializable;

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

  @Override
  public int hashCode() {
    int prime = 31;
    int result = entityId.hashCode();
    result = result * prime + user.hashCode();
    return result * prime + Long.hashCode(timestamp);
  }

  @Override
  public boolean equals(Object obj) {
    if ((obj == null) || (getClass() != obj.getClass())) {
      return false;
    }

    AuditLog auditLog = (AuditLog) obj;
    return entityId.equals(auditLog.getEntityId())
        && user.equals(auditLog.getUser())
        && (timestamp == auditLog.getTimestamp());
  }

  @Override
  public String toString() {
    return String.format(
        "AuditLog{entityId=%s, entityType=%s, user=%s, "
            + "timestamp=%s, action=%s}",
        entityId, entityType, user, timestamp, action);
  }
}
