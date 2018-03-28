package com.hsystems.lms.repository.entity;

import java.io.Serializable;

/**
 * Created by naungsoe on 2/11/16.
 */
public final class ResourcePermission implements Serializable {

  private static final long serialVersionUID = 153860840208808248L;

  private User user;

  private Privilege privilege;

  ResourcePermission() {

  }

  public ResourcePermission(
      User user,
      Privilege privilege) {

    this.user = user;
    this.privilege = privilege;
  }

  public User getUser() {
    return user;
  }

  public boolean isViewable() {
    return (privilege == Privilege.VIEW)
        || (privilege == Privilege.EDIT);
  }

  public boolean isEditable() {
    return (privilege == Privilege.EDIT);
  }

  @Override
  public String toString() {
    return String.format(
        "ResourcePermission{user=%s, privilege=%s}",
        user, privilege);
  }
}
