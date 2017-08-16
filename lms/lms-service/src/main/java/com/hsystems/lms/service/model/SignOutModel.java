package com.hsystems.lms.service.model;

import java.io.Serializable;

/**
 * Created by naungsoe on 10/9/16.
 */
public class SignOutModel implements Serializable {

  private static final long serialVersionUID = 1077844931304588567L;

  private String account;

  private String sessionId;

  private String ipAddress;

  SignOutModel() {

  }

  public SignOutModel(
      String account,
      String sessionId,
      String ipAddress) {

    this.account = account;
    this.sessionId = sessionId;
    this.ipAddress = ipAddress;
  }

  public String getAccount() {
    return account;
  }

  public void setAccount(String account) {
    this.account = account;
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
