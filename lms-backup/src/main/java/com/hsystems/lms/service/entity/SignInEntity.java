package com.hsystems.lms.service.entity;

import java.io.Serializable;

/**
 * Created by naungsoe on 10/9/16.
 */
public class SignInEntity implements Serializable {

  private static final long serialVersionUID = 3067612161107841603L;

  private String id;

  private String password;

  SignInEntity() {

  }

  public SignInEntity(String id, String password) {
    this.id = id;
    this.password = password;
  }

  public String getId() { return id; }

  public String getPassword() { return password; }
}
