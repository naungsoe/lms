package com.hsystems.lms.question.service.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.hsystems.lms.resource.Privilege;
import com.hsystems.lms.school.service.model.UserModel;

import java.io.Serializable;

/**
 * Created by naungsoe on 2/11/16.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public final class PermissionModel implements Serializable {

  private static final long serialVersionUID = -1725420757785887602L;

  private UserModel user;

  private Privilege privilege;

  public PermissionModel() {

  }

  public UserModel getUser() {
    return user;
  }

  public void setUser(UserModel user) {
    this.user = user;
  }

  public Privilege getPrivilege() {
    return privilege;
  }

  public void setPrivilege(Privilege privilege) {
    this.privilege = privilege;
  }
}