package com.hsystems.lms.entity.resource;

import java.util.Enumeration;

/**
 * Created by naungsoe on 5/11/16.
 */
public interface Resource {

  Enumeration<Permission> getPermissions();

  void addPermission(Permission... permissions);

  Status getStatus();
}