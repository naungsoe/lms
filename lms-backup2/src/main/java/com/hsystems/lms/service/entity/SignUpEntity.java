package com.hsystems.lms.service.entity;

import java.io.Serializable;

/**
 * Created by naungsoe on 10/9/16.
 */
public class SignUpEntity extends SignInEntity implements Serializable {

  private static final long serialVersionUID = -6318683344722781066L;

  private String confirmPassword;

  private String firstName;

  private String lastName;

  private String dateOfBirth;

  private String gender;

  private String mobile;

  private String email;

  SignUpEntity() {

  }

  public SignUpEntity(
      String id,
      String password,
      String confirmPassword,
      String firstName,
      String lastName,
      String dateOfBirth,
      String gender,
      String mobile,
      String email) {

    super(id, password);
    this.confirmPassword = confirmPassword;
    this.firstName = firstName;
    this.lastName = lastName;
    this.dateOfBirth = dateOfBirth;
    this.gender = gender;
    this.mobile = mobile;
    this.email = email;
  }

  public String getConfirmPassword() { return confirmPassword; }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public String getDateOfBirth() {
    return dateOfBirth;
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
