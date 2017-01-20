package com.hsystems.lms.service.model;

import java.io.Serializable;

/**
 * Created by naungsoe on 10/9/16.
 */
public class SignUpModel extends SignInModel implements Serializable {

  private static final long serialVersionUID = -6318683344722781066L;

  private String confirmPassword;

  private String firstName;

  private String lastName;

  private String dateOfBirth;

  private String gender;

  private String mobile;

  private String email;

  SignUpModel() {

  }

  public SignUpModel(
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

  public String getConfirmPassword() {
    return confirmPassword;
  }

  public void setConfirmPassword(String confirmPassword) {
    this.confirmPassword = confirmPassword;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getDateOfBirth() {
    return dateOfBirth;
  }

  public void setDateOfBirth(String dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
  }

  public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  public String getMobile() {
    return mobile;
  }

  public void setMobile(String mobile) {
    this.mobile = mobile;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }
}
