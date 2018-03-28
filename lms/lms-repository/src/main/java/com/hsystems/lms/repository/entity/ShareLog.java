package com.hsystems.lms.repository.entity;

import com.hsystems.lms.common.annotation.IndexDocument;
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
@IndexDocument(namespace = "lms", collection = "sharelogs")
public final class ShareLog implements Entity, Serializable {

  private static final long serialVersionUID = 7872836504845254592L;

  private String id;

  private List<ResourcePermission> permissions;

  private User sharedBy;

  private LocalDateTime sharedDateTime;

  ShareLog() {

  }

  public ShareLog(
      String id,
      List<ResourcePermission> permissions,
      User sharedBy,
      LocalDateTime sharedDateTime) {

    this.id = id;
    this.permissions = permissions;
    this.sharedBy = sharedBy;
    this.sharedDateTime = sharedDateTime;
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
  public String toString() {
    return String.format(
        "ShareLog{id=%s, sharedBy=%s, sharedDateTime=%s, permissions=%s}",
        id, sharedBy, sharedDateTime, StringUtils.join(permissions, ","));
  }
}
