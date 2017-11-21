package com.hsystems.lms.repository.entity;

import java.io.Serializable;

/**
 * Created by naungsoe on 2/11/16.
 */
public final class PermissionSet implements Serializable {

  private static final long serialVersionUID = 153860840208808248L;

  private User user;

  private Permission permission;

  PermissionSet() {

  }

  public PermissionSet(
      User user,
      Permission permission) {

    this.user = user;
    this.permission = permission;
  }

  public User getUser() {
    return user;
  }

  public Permission getPermission() {
    return permission;
  }

  @Override
  public String toString() {
    return String.format(
        "PermissionSet{user=%s, permission=%s}",
        user, permission);
  }
}
