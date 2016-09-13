package com.hsystems.lms.service.entity;

/**
 * Created by administrator on 10/9/16.
 */
public class AccountEntity extends UserEntity {

  protected String password;

  protected String confirmPassword;

  protected String oldPassword;

  protected String salt;

  protected AccountEntity() {

  }

  public String getPassword() { return password; }

  public String getConfirmPassword() { return confirmPassword; }

  public String getOldPassword() { return oldPassword; }

  public String getSalt() { return salt; }

  public void setSalt(String value) { salt = value; }
}
