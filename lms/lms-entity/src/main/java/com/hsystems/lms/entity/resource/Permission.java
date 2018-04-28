package com.hsystems.lms.entity.resource;

import com.hsystems.lms.common.security.Principal;

import java.io.Serializable;

/**
 * Created by naungsoe on 2/11/16.
 */
public final class Permission implements Serializable {

  private static final long serialVersionUID = -8221833371515134307L;

  private Principal principal;

  private Privilege privilege;

  Permission() {

  }

  public Permission(
      Principal principal,
      Privilege privilege) {

    this.principal = principal;
    this.privilege = privilege;
  }

  public Principal getPrincipal() {
    return principal;
  }

  public Privilege getPrivilege() {
    return privilege;
  }
}