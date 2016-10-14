package com.hsystems.lms.model;

import java.io.Serializable;

/**
 * Created by administrator on 10/9/16.
 */
public class SignInDetails implements Serializable {

  private static final long serialVersionUID = 3067612161107841603L;

  protected String id;

  protected String password;

  SignInDetails() {

  }

  public String getId() { return id; }

  public String getPassword() { return password; }
}
