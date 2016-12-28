package com.hsystems.lms.repository.entity;

import com.hsystems.lms.common.Permission;

import java.io.Serializable;

/**
 * Created by naungsoe on 2/11/16.
 */
public class ShareLogEntry implements Serializable {

  private static final long serialVersionUID = -506715044250609004L;

  private User user;

  private Permission permission;

  ShareLogEntry() {

  }

  public ShareLogEntry(
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
  public int hashCode() {
    int prime = 31;
    int result = user.hashCode();
    return result * prime + permission.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if ((obj == null) || (getClass() != obj.getClass())) {
      return false;
    }

    ShareLogEntry entry = (ShareLogEntry) obj;
    return user.equals(entry.getUser())
        && permission.equals(permission);
  }

  @Override
  public String toString() {
    return String.format(
        "ShareLog{user=%s, permission=%s}",
        user, permission);
  }
}
