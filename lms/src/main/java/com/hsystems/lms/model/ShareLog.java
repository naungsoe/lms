package com.hsystems.lms.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by naungsoe on 2/11/16.
 */
public class ShareLog implements Serializable {

  private static final long serialVersionUID = 6359949551941210092L;

  private User user;

  private List<Permission> permissions;

  private

  ShareLog() {

  }

  public ShareLog(
      User user,
      List<Permission> permissions) {

    this.user = user;
    this.permissions = permissions;
  }

  public User getUser() {
    return user;
  }

  public List<Permission> getPermissions() {
    return permissions;
  }
}
