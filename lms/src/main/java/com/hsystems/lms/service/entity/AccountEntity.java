package com.hsystems.lms.service.entity;

import java.io.Serializable;

/**
 * Created by administrator on 10/9/16.
 */
public class AccountEntity extends UserEntity implements Serializable {

  private static final long serialVersionUID = -6318683344722781066L;

  protected String password;

  protected String confirmPassword;

  protected String oldPassword;

  protected String salt;

  AccountEntity() {

  }

  public String getPassword() { return password; }

  public String getConfirmPassword() { return confirmPassword; }

  public String getOldPassword() { return oldPassword; }

  public String getSalt() { return salt; }

  public void setSalt(String value) { salt = value; }
}
