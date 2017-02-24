package com.hsystems.lms.repository.entity;

import com.hsystems.lms.common.Action;

import java.io.Serializable;

/**
 * Created by naungsoe on 2/11/16.
 */
public class AuditLog implements Entity, Serializable {

  private static final long serialVersionUID = -3408481188461757227L;

  private String id;

  private EntityType type;

  private User user;

  private Action action;

  private long timestamp;

  AuditLog() {

  }

  public AuditLog(
      String id,
      EntityType type,
      User user,
      Action action,
      long timestamp) {

    this.id = id;
    this.type = type;
    this.user = user;
    this.action = action;
    this.timestamp = timestamp;
  }

  @Override
  public String getId() {
    return id;
  }

  public EntityType getType() {
    return type;
  }

  public User getUser() {
    return user;
  }

  public Action getAction() {
    return action;
  }

  public long getTimestamp() {
    return timestamp;
  }

  @Override
  public int hashCode() {
    int prime = 31;
    int result = id.hashCode();
    result = result * prime + user.hashCode();
    return result * prime + Long.hashCode(timestamp);
  }

  @Override
  public boolean equals(Object obj) {
    if ((obj == null) || (getClass() != obj.getClass())) {
      return false;
    }

    AuditLog auditLog = (AuditLog) obj;

    return id.equals(auditLog.getId())
        && user.equals(auditLog.getUser())
        && (timestamp == auditLog.getTimestamp());
  }

  @Override
  public String toString() {
    return String.format(
        "AuditLog{id=%s, type=%s, user=%s, timestamp=%s, action=%s}",
        id, type, user, timestamp, action);
  }
}
