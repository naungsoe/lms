package com.hsystems.lms.repository.entity;

import com.hsystems.lms.common.annotation.IndexCollection;
import com.hsystems.lms.common.annotation.IndexField;

import java.io.Serializable;

/**
 * Created by naungsoe on 2/11/16.
 */
@IndexCollection(namespace = "lms", name = "groups")
public class AuditLog implements Entity, Serializable {

  private static final long serialVersionUID = 3331117617965491553L;

  @IndexField
  private String id;

  @IndexField
  private EntityType entityType;

  @IndexField
  private User actionBy;

  @IndexField
  private ActionType actionType;

  @IndexField
  private long timestamp;

  AuditLog() {

  }

  public AuditLog(
      String id,
      EntityType entityType,
      User actionBy,
      ActionType actionType,
      long timestamp) {

    this.id = id;
    this.entityType = entityType;
    this.actionBy = actionBy;
    this.actionType = actionType;
    this.timestamp = timestamp;
  }

  @Override
  public String getId() {
    return id;
  }

  public EntityType getEntityType() {
    return entityType;
  }

  public User getActionBy() {
    return actionBy;
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
    result = result * prime + actionBy.hashCode();
    return result * prime + Long.hashCode(timestamp);
  }

  @Override
  public boolean equals(Object obj) {
    if ((obj == null) || (getClass() != obj.getClass())) {
      return false;
    }

    AuditLog auditLog = (AuditLog) obj;
    return id.equals(auditLog.getId());
  }

  @Override
  public String toString() {
    return String.format(
        "AuditLog{id=%s, entityType=%s, "
            + "actionBy=%s, timestamp=%s, actionType=%s}",
        id, entityType, actionBy, timestamp, actionType);
  }
}
