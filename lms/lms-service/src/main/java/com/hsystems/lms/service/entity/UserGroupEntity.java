package com.hsystems.lms.service.entity;

import com.hsystems.lms.common.Permission;
import com.hsystems.lms.repository.model.School;
import com.hsystems.lms.repository.model.User;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * Created by naungsoe on 7/10/16.
 */
public class UserGroupEntity implements Serializable {

  private static final long serialVersionUID = 8759461339462158047L;

  private String id;

  private String name;

  UserGroupEntity() {

  }

  public UserGroupEntity(
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
