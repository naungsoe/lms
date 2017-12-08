package com.hsystems.lms.repository.entity;

import com.hsystems.lms.common.annotation.IndexDocument;

import java.io.Serializable;

/**
 * Created by naungsoe on 2/11/16.
 */
@IndexDocument(namespace = "lms", collection = "mutations")
public final class Mutation implements Entity, Serializable {

  private static final long serialVersionUID = 2286300390937074376L;

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
  public String toString() {
    return String.format(
        "AuditLog{id=%s, entityType=%s, actionType=%s, timestamp=%s}",
        id, entityType, actionType, timestamp);
  }
}
