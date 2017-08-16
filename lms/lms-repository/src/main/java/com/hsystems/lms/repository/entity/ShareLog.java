package com.hsystems.lms.repository.entity;

import com.hsystems.lms.common.annotation.IndexCollection;
import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.common.util.StringUtils;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * Created by naungsoe on 2/11/16.
 */
@IndexCollection(namespace = "lms", name = "shares")
public class ShareLog implements Entity, Serializable {

  private static final long serialVersionUID = 6359949551941210092L;

  private String id;

  private EntityType type;

  private User sharedBy;

  private LocalDateTime sharedDateTime;

  private List<AccessControl> accessControls;

  ShareLog() {

  }

  public ShareLog(
      String id,
      EntityType type,
      User sharedBy,
      LocalDateTime sharedDateTime,
      List<AccessControl> accessControls) {

    this.id = id;
    this.type = type;
    this.sharedBy = sharedBy;
    this.sharedDateTime = sharedDateTime;
    this.accessControls = accessControls;
  }

  @Override
  public String getId() {
    return id;
  }

  public EntityType getType() {
    return type;
  }

  public User getSharedBy() {
    return sharedBy;
  }

  public LocalDateTime getSharedDateTime() {
    return sharedDateTime;
  }

  public List<AccessControl> getAccessControls() {
    return CollectionUtils.isEmpty(accessControls)
        ? Collections.emptyList()
        : Collections.unmodifiableList(accessControls);
  }

  @Override
  public int hashCode() {
    int prime = 31;
    int result = id.hashCode();
    result = result * prime + sharedBy.hashCode();
    return result * prime + sharedDateTime.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if ((obj == null) || (getClass() != obj.getClass())) {
      return false;
    }

    ShareLog shareLog = (ShareLog) obj;
    return id.equals(shareLog.getId());
  }

  @Override
  public String toString() {
    return String.format(
        "ShareLog{id=%s, type=%s, sharedBy=%s, "
            + "sharedDateTime=%s, accessControls=%s}",
        id, type, sharedBy, sharedDateTime,
        StringUtils.join(accessControls, ","));
  }
}
