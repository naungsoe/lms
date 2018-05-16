package com.hsystems.lms.resource;

import com.hsystems.lms.entity.User;

import java.io.Serializable;

/**
 * Created by naungsoe on 2/11/16.
 */
public final class Permission implements Serializable {

  private static final long serialVersionUID = -1770666665101975503L;

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
}