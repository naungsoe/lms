package com.hsystems.lms.repository.entity;

import java.io.Serializable;

/**
 * Created by naungsoe on 2/11/16.
 */
public final class Permission implements Serializable {

  private static final long serialVersionUID = 4415009143741247258L;

  private User user;

  private Privilege privilege;

  Permission() {

  }

  public Permission(
      User user,
      Privilege privilege) {

    this.user = user;
    this.privilege = privilege;
  }

  public User getUser() {
    return user;
  }

  public Privilege getPrivilege() {
    return privilege;
  }

  @Override
  public String toString() {
    return String.format(
        "Permission{user=%s, privilege=%s}",
        user, privilege);
  }
}
