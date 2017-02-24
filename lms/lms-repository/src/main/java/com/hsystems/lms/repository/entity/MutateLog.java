package com.hsystems.lms.repository.entity;

import com.hsystems.lms.common.Action;

import java.io.Serializable;

/**
 * Created by naungsoe on 2/11/16.
 */
public class MutateLog implements Entity, Serializable {

  private static final long serialVersionUID = 6681526004482435421L;

  private String id;

  private EntityType type;

  private Action action;

  private long timestamp;

  MutateLog() {

  }

  public MutateLog(
      String id,
      EntityType type,
      Action action,
      long timestamp) {

    this.id = id;
    this.type = type;
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
    return result * prime + Long.hashCode(timestamp);
  }

  @Override
  public boolean equals(Object obj) {
    if ((obj == null) || (getClass() != obj.getClass())) {
      return false;
    }

    MutateLog auditLog = (MutateLog) obj;

    return id.equals(auditLog.getId())
        && (timestamp == auditLog.getTimestamp());
  }

  @Override
  public String toString() {
    return String.format(
        "AuditLog{id=%s, type=%s, timestamp=%s, action=%s}",
        id, type, timestamp, action);
  }
}
