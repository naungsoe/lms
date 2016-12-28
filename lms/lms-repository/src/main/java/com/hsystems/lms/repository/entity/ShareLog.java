package com.hsystems.lms.repository.entity;

import com.hsystems.lms.common.EntityType;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * Created by naungsoe on 2/11/16.
 */
public class ShareLog implements Serializable {

  private static final long serialVersionUID = 6359949551941210092L;

  private String entityId;

  private EntityType entityType;

  private User sharedBy;

  private LocalDateTime sharedDateTime;

  private List<ShareLogEntry> entries;

  ShareLog() {

  }

  public ShareLog(
      String entityId,
      EntityType entityType,
      User sharedBy,
      LocalDateTime sharedDateTime,
      List<ShareLogEntry> entries) {

    this.entityId = entityId;
    this.entityType = entityType;
    this.sharedBy = sharedBy;
    this.sharedDateTime = sharedDateTime;
    this.entries = entries;
  }

  public String getEntityId() {
    return entityId;
  }

  public EntityType getEntityType() {
    return entityType;
  }

  public User getSharedBy() {
    return sharedBy;
  }

  public LocalDateTime getSharedDateTime() {
    return sharedDateTime;
  }

  public List<ShareLogEntry> getEntries() {
    return Collections.unmodifiableList(entries);
  }

  @Override
  public int hashCode() {
    int prime = 31;
    int result = entityId.hashCode();
    result = result * prime + sharedBy.hashCode();
    return result * prime + sharedDateTime.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if ((obj == null) || (getClass() != obj.getClass())) {
      return false;
    }

    ShareLog shareLog = (ShareLog) obj;
    return entityId.equals(shareLog.getEntityId())
        && sharedBy.equals(shareLog.getSharedBy())
        && sharedDateTime.equals(shareLog.getSharedDateTime());
  }

  @Override
  public String toString() {
    StringBuilder entriesBuilder = new StringBuilder();
    entries.forEach(x -> entriesBuilder.append(x).append(","));

    return String.format(
        "ShareLog{entityId=%s, entityType=%s, sharedBy=%s, "
            + "sharedDateTime=%s, entries=%s}",
        entityId, entityType, sharedBy, sharedDateTime, entriesBuilder);
  }
}
