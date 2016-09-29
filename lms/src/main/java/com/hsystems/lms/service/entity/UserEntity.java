package com.hsystems.lms.service.entity;

import com.hsystems.lms.DateTimeUtils;
import com.hsystems.lms.annotation.Field;
import com.hsystems.lms.domain.model.User;

import java.io.Serializable;
import java.util.List;

/**
 * Created by administrator on 8/8/16.
 */
public class UserEntity implements Serializable {

  private static final long serialVersionUID = 4608346504487806702L;

  @Field
  protected String id;

  @Field
  protected String firstName;

  @Field
  protected String lastName;

  @Field
  protected String birthday;

  @Field
  protected String gender;

  @Field
  protected String mobile;

  @Field
  protected String email;

  @Field
  protected List<String> permissions;

  UserEntity() {

  }

  public UserEntity(User user) {
    this.id = user.getId();
    this.firstName = user.getFirstName();
    this.lastName = user.getLastName();
    this.birthday = DateTimeUtils.getString(user.getBirthday());
    this.gender = user.getGender();
    this.mobile = user.getMobile();
    this.email = user.getEmail();
    this.permissions = user.getPermissions();
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

  public List<String> getPermissions() { return permissions; }
}
