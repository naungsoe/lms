package com.hsystems.lms.service.model;

import java.io.Serializable;

/**
 * Created by naungsoe on 10/9/16.
 */
public class SignInModel implements Serializable {

  private static final long serialVersionUID = 3067612161107841603L;

  private String id;

  private String password;

  SignInModel() {

  }

  public SignInModel(String id, String password) {
    this.id = id;
    this.password = password;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
