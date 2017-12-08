package com.hsystems.lms.repository.entity;

import com.hsystems.lms.common.annotation.IndexDocument;
import com.hsystems.lms.common.annotation.IndexField;

import java.io.Serializable;

/**
 * Created by naungsoe on 2/11/16.
 */
@IndexDocument(namespace = "lms", collection = "groups")
public final class AuditLog implements Entity, Serializable {

  private static final long serialVersionUID = 3128314081684414981L;

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
  public String toString() {
    return String.format(
        "AuditLog{id=%s, entityType=%s, actionBy=%s, "
            + "actionType=%s, timestamp=%s}",
        id, entityType, actionBy, actionType, timestamp);
  }
}
