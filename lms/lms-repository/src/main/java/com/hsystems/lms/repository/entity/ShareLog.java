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
public final class ShareLog implements Entity, Serializable {

  private static final long serialVersionUID = -8655950641727343796L;

  private String id;

  private List<ShareEntry> shareEntries;

  private User sharedBy;

  private LocalDateTime sharedDateTime;

  ShareLog() {

  }

  public ShareLog(
      String id,
      List<ShareEntry> shareEntries,
      User sharedBy,
      LocalDateTime sharedDateTime) {

    this.id = id;
    this.shareEntries = shareEntries;
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

  public Enumeration<ShareEntry> getShareEntries() {
    return CollectionUtils.isEmpty(shareEntries)
        ? Collections.emptyEnumeration()
        : Collections.enumeration(shareEntries);
  }

  @Override
  public String toString() {
    return String.format(
        "ShareLog{id=%s, sharedBy=%s, sharedDateTime=%s, shareEntries=%s}",
        id, sharedBy, sharedDateTime, StringUtils.join(shareEntries, ","));
  }
}
