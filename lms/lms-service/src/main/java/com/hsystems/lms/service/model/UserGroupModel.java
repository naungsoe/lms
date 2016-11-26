package com.hsystems.lms.service.model;

import java.io.Serializable;

/**
 * Created by naungsoe on 7/10/16.
 */
public class UserGroupModel implements Serializable {

  private static final long serialVersionUID = 8759461339462158047L;

  private String id;

  private String name;

  UserGroupModel() {

  }

  public UserGroupModel(
      String id,
      String name) {

    this.id = id;
    this.name = name;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }
}
