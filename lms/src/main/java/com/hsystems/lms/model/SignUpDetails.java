package com.hsystems.lms.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * Created by administrator on 10/9/16.
 */
public class SignUpDetails extends SignInDetails implements Serializable {

  private static final long serialVersionUID = -6318683344722781066L;

  protected String confirmPassword;

  protected String firstName;

  protected String lastName;

  protected String dateOfBirth;

  protected String gender;

  protected String mobile;

  protected String email;

  SignUpDetails() {

  }

  public SignUpDetails(
      String id,
      String password,
      String confirmPassword,
      String firstName,
      String lastName,
      String dateOfBirth,
      String gender,
      String mobile,
      String email) {

    this.id = id;
    this.password = password;
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
