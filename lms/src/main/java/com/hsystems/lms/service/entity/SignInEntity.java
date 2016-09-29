package com.hsystems.lms.service.entity;

import java.io.Serializable;

/**
 * Created by administrator on 10/9/16.
 */
public class SignInEntity implements Serializable {

  private static final long serialVersionUID = 3067612161107841603L;

  protected String id;

  protected String password;

  SignInEntity() {

  }

  public String getId() { return id; }

  public String getPassword() { return password; }
}
