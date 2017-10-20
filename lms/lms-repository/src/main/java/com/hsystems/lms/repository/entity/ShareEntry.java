package com.hsystems.lms.repository.entity;

import java.io.Serializable;

/**
 * Created by naungsoe on 2/11/16.
 */
public final class ShareEntry implements Serializable {

  private static final long serialVersionUID = 5918154532815469595L;

  private User user;

  private SharePermission permission;

  ShareEntry() {

  }

  public ShareEntry(
      User user,
      SharePermission permission) {

    this.user = user;
    this.permission = permission;
  }

  public User getUser() {
    return user;
  }

  public SharePermission getPermission() {
    return permission;
  }

  @Override
  public String toString() {
    return String.format(
        "ShareEntry{user=%s, permission=%s}",
        user, permission);
  }
}
