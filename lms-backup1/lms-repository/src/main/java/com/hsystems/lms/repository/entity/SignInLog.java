package com.hsystems.lms.repository.entity;

import com.hsystems.lms.common.annotation.IndexDocument;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Created by naungsoe on 2/11/16.
 */
@IndexDocument(namespace = "lms", collection = "signinlogs")
public final class SignInLog implements Entity {

  private static final long serialVersionUID = 8078103229259222444L;

  private String id;

  private String account;

  private String sessionId;

  private String ipAddress;

  private LocalDateTime dateTime;

  private int fails;

  SignInLog() {

  }

  public SignInLog(
      String id,
      String account,
      String sessionId,
      String ipAddress,
      LocalDateTime dateTime,
      int fails) {

    this.id = id;
    this.account = account;
    this.sessionId = sessionId;
    this.ipAddress = ipAddress;
    this.dateTime = dateTime;
    this.fails = fails;
  }

  @Override
  public String getId() {
    return id;
  }

  public String getAccount() {
    return account;
  }

  public String getSessionId() {
    return sessionId;
  }

  public String getIpAddress() {
    return ipAddress;
  }

  public LocalDateTime getDateTime() {
    return dateTime;
  }

  public int getFails() {
    return fails;
  }

  @Override
  public String toString() {
    return String.format(
        "SignInLog{id=%s, account=%s, sessionId=%s, "
            + "ipAddress=%s, fails=%s, dateTime=%s}",
        id, account, sessionId, ipAddress, fails, dateTime);
  }
}
