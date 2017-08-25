package com.hsystems.lms.repository.entity;

import com.hsystems.lms.common.annotation.IndexCollection;

import java.io.Serializable;

/**
 * Created by naungsoe on 2/11/16.
 */
@IndexCollection(namespace = "lms", name = "mutations")
public class Mutation implements Entity, Serializable {

  private static final long serialVersionUID = 1810669316250096511L;

  private String id;

  private EntityType entityType;

  private ActionType actionType;

  private long timestamp;

  Mutation() {

  }

  public Mutation(
      String id,
      EntityType entityType,
      ActionType actionType,
      long timestamp) {

    this.id = id;
    this.entityType = entityType;
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

    Mutation auditLog = (Mutation) obj;
    return id.equals(auditLog.getId());
  }

  @Override
  public String toString() {
    return String.format(
        "AuditLog{id=%s, entityType=%s, timestamp=%s, actionType=%s}",
        id, entityType, timestamp, actionType);
  }
}
