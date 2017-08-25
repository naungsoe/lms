package com.hsystems.lms.repository.entity;

import com.hsystems.lms.common.annotation.IndexCollection;
import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.common.util.StringUtils;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by naungsoe on 2/11/16.
 */
@IndexCollection(namespace = "lms", name = "sharelogs")
public class ShareLog implements Entity, Serializable {

  private static final long serialVersionUID = -1443558206059695568L;

  private String id;

  private User sharedBy;

  private LocalDateTime sharedDateTime;

  private List<ResourcePermission> permissions;

  ShareLog() {

  }

  public ShareLog(
      String id,
      User sharedBy,
      LocalDateTime sharedDateTime,
      List<ResourcePermission> permissions) {

    this.id = id;
    this.sharedBy = sharedBy;
    this.sharedDateTime = sharedDateTime;
    this.permissions = permissions;
  }

  @Override
  public String getId() {
    return id;
  }

  public User getSharedBy() {
    return sharedBy;
  }

  public LocalDateTime getSharedDateTime() {
    return sharedDateTime;
  }

  public Enumeration<ResourcePermission> getPermissions() {
    return CollectionUtils.isEmpty(permissions)
        ? Collections.emptyEnumeration()
        : Collections.enumeration(permissions);
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

    ShareLog log = (ShareLog) obj;
    return id.equals(log.getId());
  }

  @Override
  public String toString() {
    return String.format(
        "ShareLog{id=%s, sharedBy=%s, sharedDateTime=%s, permissions=%s}",
        id, sharedBy, sharedDateTime, StringUtils.join(permissions, ","));
  }
}
