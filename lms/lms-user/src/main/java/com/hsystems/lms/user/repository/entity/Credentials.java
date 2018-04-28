package com.hsystems.lms.user.repository.entity;

import java.io.Serializable;

/**
 * Created by naungsoe on 8/8/16.
 */
public final class Credentials implements Serializable {

  private static final long serialVersionUID = -6732272999355906048L;

  private String account;

  private String password;

  private String salt;

  Credentials() {

  }

  public Credentials(
      String account,
      String password,
      String salt) {

    this.account = account;
    this.password = password;
    this.salt = salt;
  }

  public String getAccount() {
    return account;
  }

  public String getPassword() {
    return password;
  }

  public String getSalt() {
    return salt;
  }
}