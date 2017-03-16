package com.hsystems.lms.service.model;

import java.io.Serializable;

/**
 * Created by naungsoe on 10/9/16.
 */
public class SignInModel implements Serializable {

  private static final long serialVersionUID = 3067612161107841603L;

  private String id;

  private String password;

  private String captcha;

  private String sessionId;

  private String ipAddress;

  SignInModel() {

  }

  public SignInModel(
      String id,
      String password,
      String captcha,
      String sessionId,
      String ipAddress) {

    this.id = id;
    this.password = password;
    this.captcha = captcha;
    this.sessionId = sessionId;
    this.ipAddress = ipAddress;
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

  public String getCaptcha() {
    return captcha;
  }

  public void setCaptcha(String captcha) {
    this.captcha = captcha;
  }

  public String getSessionId() {
    return sessionId;
  }

  public void setSessionId(String sessionId) {
    this.sessionId = sessionId;
  }

  public String getIpAddress() {
    return ipAddress;
  }

  public void setIpAddress(String ipAddress) {
    this.ipAddress = ipAddress;
  }
}