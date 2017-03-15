package com.hsystems.lms.repository.entity;

import com.hsystems.lms.common.ActionType;
import com.hsystems.lms.common.annotation.IndexCollection;

import java.io.Serializable;

/**
 * Created by naungsoe on 2/11/16.
 */
@IndexCollection(name = "mutatelogs")
public class MutateLog implements Entity, Serializable {

  private static final long serialVersionUID = 6681526004482435421L;

  private String id;

  private EntityType type;

  private ActionType actionType;

  private long timestamp;

  MutateLog() {

  }

  public MutateLog(
      String id,
      EntityType type,
      ActionType actionType,
      long timestamp) {

    this.id = id;
    this.type = type;
    this.actionType = actionType;
    this.timestamp = timestamp;
  }

  @Override
  public String getId() {
    return id;
  }

  public EntityType getType() {
    return type;
  }

  public ActionType getActionType() {
    return actionType;
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
    return id.equals(auditLog.getId());
  }

  @Override
  public String toString() {
    return String.format(
        "AuditLog{id=%s, type=%s, timestamp=%s, actionType=%s}",
        id, type, timestamp, actionType);
  }
}
