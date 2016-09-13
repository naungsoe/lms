package com.hsystems.lms.service.entity;

import com.hsystems.lms.DateTimeUtils;
import com.hsystems.lms.domain.model.User;

/**
 * Created by administrator on 8/8/16.
 */
public class UserEntity {

  protected String id;

  protected String firstName;

  protected String lastName;

  protected String birthday;

  protected String gender;

  protected String mobile;

  protected String email;

  protected UserEntity() {

  }

  public UserEntity(User user) {
    this.id = user.getId();
    this.firstName = user.getFirstName();
    this.lastName = user.getLastName();
    this.birthday = DateTimeUtils.getString(user.getBirthday());
    this.gender = user.getGender();
    this.mobile = user.getMobile();
    this.email = user.getEmail();
  }

  public String getId() {
    return id;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public String getBirthday() {
    return birthday;
  }

  public String getGender() {
    return gender;
  }

  public String getMobile() {
    return mobile;
  }

  public String getEmail() {
    return email;
  }
}
