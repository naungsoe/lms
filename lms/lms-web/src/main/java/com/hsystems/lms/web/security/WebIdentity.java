package com.hsystems.lms.web.security;

import com.hsystems.lms.common.Permission;
import com.hsystems.lms.service.model.UserModel;

import java.io.Serializable;

/**
 * Created by naungsoe on 30/11/16.
 */
public class WebIdentity implements Serializable {

  private UserModel model;

  public <T> void setModel(T model) {
    this.model = (UserModel) model;
  }

  public boolean isAuthenticated() {
    return false;
  }

  public boolean hasPermission(Permission permission) {
    return false;
  }
}
